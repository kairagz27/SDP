package kz.agzam.carwash.subsystem;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class WashBayScheduler {
    private final int bayCount;
    private final Map<UUID, Slot> reservations = new HashMap<>();

    public WashBayScheduler(int bayCount) {
        if (bayCount <= 0) {
            throw new IllegalArgumentException("At least one bay is required");
        }
        this.bayCount = bayCount;
    }

    public int reserve(UUID bookingId, LocalDateTime start, int minutes) {
        Objects.requireNonNull(bookingId, "Booking ID is required");
        Objects.requireNonNull(start, "Start time is required");
        if (minutes <= 0 || reservations.containsKey(bookingId)) {
            throw new IllegalArgumentException("Invalid duration or duplicate reservation");
        }
        LocalDateTime end = start.plusMinutes(minutes);
        for (int bay = 1; bay <= bayCount; bay++) {
            if (isFree(bay, start, end)) {
                reservations.put(bookingId, new Slot(bay, start, end));
                return bay;
            }
        }
        throw new IllegalStateException("No wash bay is available at " + start);
    }

    private boolean isFree(int bay, LocalDateTime start, LocalDateTime end) {
        return reservations.values().stream().noneMatch(slot ->
                slot.bay() == bay && start.isBefore(slot.end()) && slot.start().isBefore(end));
    }

    public void release(UUID bookingId) {
        reservations.remove(bookingId);
    }

    public int reservationCount() {
        return reservations.size();
    }

    private record Slot(int bay, LocalDateTime start, LocalDateTime end) {
    }
}
