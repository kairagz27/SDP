package kz.agzam.carwash.subsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class ChemicalInventory {
    private int availableUnits;
    private final Map<UUID, Integer> reservations = new HashMap<>();

    public ChemicalInventory(int initialUnits) {
        if (initialUnits < 0) {
            throw new IllegalArgumentException("Stock must not be negative");
        }
        availableUnits = initialUnits;
    }

    public void reserve(UUID bookingId, int units) {
        Objects.requireNonNull(bookingId, "Booking ID is required");
        if (units <= 0 || reservations.containsKey(bookingId)) {
            throw new IllegalArgumentException("Invalid units or duplicate stock reservation");
        }
        if (availableUnits < units) {
            throw new IllegalStateException("Not enough wash chemicals");
        }
        reservations.put(bookingId, units);
        availableUnits -= units;
    }

    public void release(UUID bookingId) {
        Integer units = reservations.remove(bookingId);
        if (units != null) {
            availableUnits += units;
        }
    }

    public int availableUnits() {
        return availableUnits;
    }
}
