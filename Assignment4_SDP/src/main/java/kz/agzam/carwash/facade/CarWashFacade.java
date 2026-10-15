package kz.agzam.carwash.facade;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import kz.agzam.carwash.api.BookingException;
import kz.agzam.carwash.api.BookingReceipt;
import kz.agzam.carwash.api.WashRequest;
import kz.agzam.carwash.subsystem.BookingRepository;
import kz.agzam.carwash.subsystem.ChemicalInventory;
import kz.agzam.carwash.subsystem.PaymentGateway;
import kz.agzam.carwash.subsystem.ServiceCatalog;
import kz.agzam.carwash.subsystem.ServiceQuote;
import kz.agzam.carwash.subsystem.WashBayScheduler;

public final class CarWashFacade {
    private final ServiceCatalog catalog;
    private final WashBayScheduler scheduler;
    private final ChemicalInventory inventory;
    private final PaymentGateway payments;
    private final BookingRepository bookings;

    CarWashFacade(ServiceCatalog catalog, WashBayScheduler scheduler,
                  ChemicalInventory inventory, PaymentGateway payments,
                  BookingRepository bookings) {
        this.catalog = Objects.requireNonNull(catalog);
        this.scheduler = Objects.requireNonNull(scheduler);
        this.inventory = Objects.requireNonNull(inventory);
        this.payments = Objects.requireNonNull(payments);
        this.bookings = Objects.requireNonNull(bookings);
    }

    public static CarWashFacade createDemo() {
        return new CarWashFacade(new ServiceCatalog(), new WashBayScheduler(2),
                new ChemicalInventory(20),
                new PaymentGateway(Map.of("Agzam", new BigDecimal("20000.00"),
                        "LowBalance", new BigDecimal("1000.00"))),
                new BookingRepository());
    }

    public BookingReceipt bookWash(WashRequest request) {
        Objects.requireNonNull(request, "Wash request is required");
        UUID bookingId = UUID.randomUUID();
        try {
            ServiceQuote quote = catalog.quote(request.service());
            int bay = scheduler.reserve(bookingId, request.start(), quote.minutes());
            inventory.reserve(bookingId, quote.chemicalUnits());
            payments.charge(bookingId, request.customer(), quote.price());
            BookingReceipt receipt = new BookingReceipt(bookingId, request.customer(),
                    quote.name(), request.start(), request.start().plusMinutes(quote.minutes()),
                    bay, quote.price());
            bookings.save(receipt);
            return receipt;
        } catch (RuntimeException cause) {
            releaseResources(bookingId);
            bookings.delete(bookingId);
            throw new BookingException("Cannot book wash for " + request.customer()
                    + ": " + cause.getMessage(), cause);
        }
    }

    public void cancelBooking(UUID bookingId) {
        try {
            bookings.find(bookingId);
            releaseResources(bookingId);
            bookings.delete(bookingId);
        } catch (RuntimeException cause) {
            throw new BookingException("Cannot cancel booking: " + cause.getMessage(), cause);
        }
    }

    private void releaseResources(UUID bookingId) {
        payments.refund(bookingId);
        inventory.release(bookingId);
        scheduler.release(bookingId);
    }
}
