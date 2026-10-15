package kz.agzam.carwash.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookingReceipt(UUID bookingId, String customer, String service,
                             LocalDateTime start, LocalDateTime end,
                             int bay, BigDecimal total) {
}
