package kz.agzam.carwash.client;

import java.time.LocalDateTime;
import kz.agzam.carwash.api.BookingException;
import kz.agzam.carwash.api.BookingReceipt;
import kz.agzam.carwash.api.WashRequest;
import kz.agzam.carwash.facade.CarWashFacade;

public final class Main {
    public static void main(String[] args) {
        CarWashFacade carWash = CarWashFacade.createDemo();
        LocalDateTime start = LocalDateTime.of(2099, 10, 18, 10, 0);
        BookingReceipt first = carWash.bookWash(new WashRequest("Agzam", "PREMIUM", start));
        BookingReceipt second = carWash.bookWash(new WashRequest("Agzam", "BASIC", start));
        printBooking(first);
        printBooking(second);
        try {
            carWash.bookWash(new WashRequest("Agzam", "BASIC", start));
        } catch (BookingException failure) {
            System.out.println("Expected failure: " + failure.getMessage());
        }
        carWash.cancelBooking(first.bookingId());
        System.out.println("First booking cancelled and payment refunded.");
        try {
            carWash.bookWash(new WashRequest("LowBalance", "PREMIUM", start));
        } catch (BookingException failure) {
            System.out.println("Expected failure: " + failure.getMessage());
        }
        BookingReceipt replacement = carWash.bookWash(new WashRequest("Agzam", "PREMIUM", start));
        printBooking(replacement);
    }

    private static void printBooking(BookingReceipt receipt) {
        System.out.printf("Booked %s | Bay %d | %s to %s | %s KZT%n",
                receipt.service(), receipt.bay(), receipt.start(), receipt.end(), receipt.total());
    }
}
