package kz.agzam.carwash.advanced;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import kz.agzam.carwash.api.BookingReceipt;
import kz.agzam.carwash.subsystem.BookingRepository;
import kz.agzam.carwash.subsystem.ChemicalInventory;
import kz.agzam.carwash.subsystem.PaymentGateway;
import kz.agzam.carwash.subsystem.ServiceCatalog;
import kz.agzam.carwash.subsystem.ServiceQuote;
import kz.agzam.carwash.subsystem.WashBayScheduler;

public final class DirectSubsystemExample {
    public static void main(String[] args) {
        ServiceCatalog catalog = new ServiceCatalog();
        WashBayScheduler scheduler = new WashBayScheduler(1);
        ChemicalInventory inventory = new ChemicalInventory(10);
        PaymentGateway payments = new PaymentGateway(Map.of("Expert", new BigDecimal("10000.00")));
        BookingRepository bookings = new BookingRepository();
        UUID id = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2099, 10, 18, 10, 0);
        try {
            ServiceQuote quote = catalog.quote("PREMIUM");
            int bay = scheduler.reserve(id, start, quote.minutes());
            inventory.reserve(id, quote.chemicalUnits());
            payments.charge(id, "Expert", quote.price());
            bookings.save(new BookingReceipt(id, "Expert", quote.name(), start,
                    start.plusMinutes(quote.minutes()), bay, quote.price()));
            System.out.println("Direct booking: " + bookings.find(id).service());
        } finally {
            payments.refund(id);
            inventory.release(id);
            scheduler.release(id);
            bookings.delete(id);
        }
        System.out.println("Advanced example cleaned up all resources.");
    }
}
