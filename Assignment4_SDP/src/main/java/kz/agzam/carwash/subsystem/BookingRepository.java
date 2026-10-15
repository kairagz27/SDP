package kz.agzam.carwash.subsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import kz.agzam.carwash.api.BookingReceipt;

public class BookingRepository {
    private final Map<UUID, BookingReceipt> bookings = new HashMap<>();

    public void save(BookingReceipt booking) {
        Objects.requireNonNull(booking, "Booking is required");
        if (bookings.putIfAbsent(booking.bookingId(), booking) != null) {
            throw new IllegalStateException("Booking already exists");
        }
    }

    public BookingReceipt find(UUID bookingId) {
        Objects.requireNonNull(bookingId, "Booking ID is required");
        BookingReceipt booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found: " + bookingId);
        }
        return booking;
    }

    public void delete(UUID bookingId) {
        bookings.remove(bookingId);
    }

    public int size() {
        return bookings.size();
    }
}
