package kz.agzam.carwash.facade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import kz.agzam.carwash.api.BookingException;
import kz.agzam.carwash.api.BookingReceipt;
import kz.agzam.carwash.api.WashRequest;
import kz.agzam.carwash.subsystem.BookingRepository;
import kz.agzam.carwash.subsystem.ChemicalInventory;
import kz.agzam.carwash.subsystem.PaymentGateway;
import kz.agzam.carwash.subsystem.ServiceCatalog;
import kz.agzam.carwash.subsystem.WashBayScheduler;

public final class CarWashTests {
    private static final LocalDateTime START = LocalDateTime.of(2099, 10, 18, 10, 0);
    private static int passed;

    public static void main(String[] args) {
        run("Premium booking updates all subsystem state", CarWashTests::successfulBooking);
        run("Basic service has its own price and duration", CarWashTests::basicBooking);
        run("Two bays accept simultaneous bookings", CarWashTests::parallelSlots);
        run("Full schedule rejects without extra charges", CarWashTests::fullSchedule);
        run("Partial time overlap is rejected", CarWashTests::overlap);
        run("Adjacent time slots are accepted", CarWashTests::adjacentSlots);
        run("Insufficient chemicals release the reserved bay", CarWashTests::stockFailure);
        run("Declined payment restores chemicals and bay", CarWashTests::paymentFailure);
        run("Storage failure refunds payment and restores resources", CarWashTests::storageFailure);
        run("Cancellation refunds and restores all resources", CarWashTests::cancellation);
        run("Repeated cancellation cannot refund twice", CarWashTests::repeatedCancellation);
        run("Unknown booking does not change state", CarWashTests::unknownCancellation);
        run("Unknown service leaves state unchanged", CarWashTests::unknownService);
        run("Blank customer is rejected", () -> expect(IllegalArgumentException.class,
                () -> new WashRequest(" ", "BASIC", START)));
        run("Blank service is rejected", () -> expect(IllegalArgumentException.class,
                () -> new WashRequest("Agzam", " ", START)));
        run("Null time is rejected", () -> expect(NullPointerException.class,
                () -> new WashRequest("Agzam", "BASIC", null)));
        run("Null request is rejected", () -> expect(NullPointerException.class,
                () -> fixture(1, 10, "20000", new BookingRepository()).facade().bookWash(null)));
        run("Service names are normalized", CarWashTests::normalizedNames);
        run("Resources released twice are restored only once", CarWashTests::idempotentRelease);
        run("Subsystem constructors reject invalid state", CarWashTests::invalidConfiguration);
        run("A later success is possible after a failed payment", CarWashTests::successAfterFailure);
        run("Zero stock rejects even a basic service", CarWashTests::zeroStock);
        System.out.println("ALL " + passed + " TESTS PASSED");
    }

    private static void successfulBooking() {
        Fixture f = fixture(2, 10, "20000", new BookingRepository());
        BookingReceipt receipt = f.facade().bookWash(request("PREMIUM", START));
        check(receipt.bay() == 1 && receipt.end().equals(START.plusMinutes(60)), "Wrong slot");
        check(receipt.total().compareTo(new BigDecimal("6000")) == 0, "Wrong price");
        check(f.bookings().find(receipt.bookingId()).equals(receipt), "Receipt was not stored");
        state(f, 1, 7, "14000", 1);
    }

    private static void basicBooking() {
        Fixture f = fixture(1, 10, "20000", new BookingRepository());
        BookingReceipt receipt = f.facade().bookWash(request("BASIC", START));
        check(receipt.end().equals(START.plusMinutes(30)), "Wrong basic duration");
        check(receipt.total().compareTo(new BigDecimal("3000")) == 0, "Wrong basic price");
        state(f, 1, 9, "17000", 1);
    }

    private static void parallelSlots() {
        Fixture f = fixture(2, 10, "20000", new BookingRepository());
        BookingReceipt first = f.facade().bookWash(request("BASIC", START));
        BookingReceipt second = f.facade().bookWash(request("BASIC", START));
        check(first.bay() != second.bay(), "Bookings share a bay");
        check(!first.bookingId().equals(second.bookingId()), "Duplicate booking IDs");
        state(f, 2, 8, "14000", 2);
    }

    private static void fullSchedule() {
        Fixture f = fixture(1, 10, "20000", new BookingRepository());
        f.facade().bookWash(request("BASIC", START));
        expect(BookingException.class, () -> f.facade().bookWash(request("BASIC", START)));
        state(f, 1, 9, "17000", 1);
    }

    private static void overlap() {
        Fixture f = fixture(1, 10, "20000", new BookingRepository());
        f.facade().bookWash(request("PREMIUM", START));
        expect(BookingException.class,
                () -> f.facade().bookWash(request("BASIC", START.plusMinutes(45))));
        expect(BookingException.class,
                () -> f.facade().bookWash(request("BASIC", START.minusMinutes(15))));
        state(f, 1, 7, "14000", 1);
    }

    private static void adjacentSlots() {
        Fixture f = fixture(1, 10, "20000", new BookingRepository());
        f.facade().bookWash(request("BASIC", START));
        f.facade().bookWash(request("BASIC", START.plusMinutes(30)));
        state(f, 2, 8, "14000", 2);
    }

    private static void stockFailure() {
        Fixture f = fixture(1, 2, "20000", new BookingRepository());
        expect(BookingException.class, () -> f.facade().bookWash(request("PREMIUM", START)));
        state(f, 0, 2, "20000", 0);
        f.facade().bookWash(request("BASIC", START));
        state(f, 1, 1, "17000", 1);
    }

    private static void paymentFailure() {
        Fixture f = fixture(1, 10, "1000", new BookingRepository());
        BookingException error = expect(BookingException.class,
                () -> f.facade().bookWash(request("PREMIUM", START)));
        check(error.getCause() instanceof IllegalStateException, "Original cause is missing");
        check(error.getMessage().contains("Insufficient balance"), "Failure context is missing");
        state(f, 0, 10, "1000", 0);
    }

    private static void storageFailure() {
        BookingRepository failing = new BookingRepository() {
            @Override
            public void save(BookingReceipt booking) {
                throw new IllegalStateException("Simulated storage failure");
            }
        };
        Fixture f = fixture(1, 10, "20000", failing);
        expect(BookingException.class, () -> f.facade().bookWash(request("PREMIUM", START)));
        state(f, 0, 10, "20000", 0);
    }

    private static void cancellation() {
        Fixture f = fixture(1, 10, "20000", new BookingRepository());
        BookingReceipt receipt = f.facade().bookWash(request("PREMIUM", START));
        f.facade().cancelBooking(receipt.bookingId());
        state(f, 0, 10, "20000", 0);
        expect(IllegalArgumentException.class, () -> f.bookings().find(receipt.bookingId()));
    }

    private static void repeatedCancellation() {
        Fixture f = fixture(1, 10, "20000", new BookingRepository());
        BookingReceipt receipt = f.facade().bookWash(request("BASIC", START));
        f.facade().cancelBooking(receipt.bookingId());
        expect(BookingException.class, () -> f.facade().cancelBooking(receipt.bookingId()));
        state(f, 0, 10, "20000", 0);
    }

    private static void unknownCancellation() {
        Fixture f = fixture(1, 10, "20000", new BookingRepository());
        expect(BookingException.class, () -> f.facade().cancelBooking(UUID.randomUUID()));
        state(f, 0, 10, "20000", 0);
    }

    private static void unknownService() {
        Fixture f = fixture(1, 10, "20000", new BookingRepository());
        expect(BookingException.class, () -> f.facade().bookWash(request("DELUXE", START)));
        state(f, 0, 10, "20000", 0);
    }

    private static void normalizedNames() {
        Fixture f = fixture(1, 10, "20000", new BookingRepository());
        BookingReceipt receipt = f.facade().bookWash(new WashRequest(" Agzam ", " premium ", START));
        check(receipt.customer().equals("Agzam") && receipt.service().equals("PREMIUM"),
                "Names were not normalized");
    }

    private static void idempotentRelease() {
        Fixture f = fixture(1, 10, "20000", new BookingRepository());
        BookingReceipt receipt = f.facade().bookWash(request("BASIC", START));
        f.facade().cancelBooking(receipt.bookingId());
        f.payments().refund(receipt.bookingId());
        f.inventory().release(receipt.bookingId());
        f.scheduler().release(receipt.bookingId());
        state(f, 0, 10, "20000", 0);
    }

    private static void invalidConfiguration() {
        expect(IllegalArgumentException.class, () -> new WashBayScheduler(0));
        expect(IllegalArgumentException.class, () -> new ChemicalInventory(-1));
        expect(IllegalArgumentException.class,
                () -> new PaymentGateway(Map.of("Agzam", new BigDecimal("-1"))));
    }

    private static void successAfterFailure() {
        Fixture f = fixture(1, 10, "4000", new BookingRepository());
        expect(BookingException.class, () -> f.facade().bookWash(request("PREMIUM", START)));
        f.facade().bookWash(request("BASIC", START));
        state(f, 1, 9, "1000", 1);
    }

    private static void zeroStock() {
        Fixture f = fixture(1, 0, "20000", new BookingRepository());
        expect(BookingException.class, () -> f.facade().bookWash(request("BASIC", START)));
        state(f, 0, 0, "20000", 0);
    }

    private static WashRequest request(String service, LocalDateTime start) {
        return new WashRequest("Agzam", service, start);
    }

    private static Fixture fixture(int bays, int units, String balance, BookingRepository bookings) {
        WashBayScheduler scheduler = new WashBayScheduler(bays);
        ChemicalInventory inventory = new ChemicalInventory(units);
        PaymentGateway payments = new PaymentGateway(Map.of("Agzam", new BigDecimal(balance)));
        CarWashFacade facade = new CarWashFacade(new ServiceCatalog(), scheduler, inventory,
                payments, bookings);
        return new Fixture(facade, scheduler, inventory, payments, bookings);
    }

    private static void state(Fixture f, int reservations, int units, String balance, int count) {
        check(f.scheduler().reservationCount() == reservations, "Wrong schedule state");
        check(f.inventory().availableUnits() == units, "Wrong stock state");
        check(f.payments().balanceOf("Agzam").compareTo(new BigDecimal(balance)) == 0,
                "Wrong balance");
        check(f.bookings().size() == count, "Wrong repository state");
    }

    private static <T extends Throwable> T expect(Class<T> type, Runnable action) {
        try {
            action.run();
        } catch (Throwable failure) {
            if (type.isInstance(failure)) {
                return type.cast(failure);
            }
            throw new AssertionError("Expected " + type.getSimpleName(), failure);
        }
        throw new AssertionError("Expected " + type.getSimpleName() + " but no exception was thrown");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void run(String name, Runnable test) {
        test.run();
        passed++;
        System.out.println("PASS " + passed + ": " + name);
    }

    private record Fixture(CarWashFacade facade, WashBayScheduler scheduler,
                           ChemicalInventory inventory, PaymentGateway payments,
                           BookingRepository bookings) {
    }
}
