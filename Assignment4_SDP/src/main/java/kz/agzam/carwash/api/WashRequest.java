package kz.agzam.carwash.api;

import java.time.LocalDateTime;
import java.util.Objects;

public record WashRequest(String customer, String service, LocalDateTime start) {
    public WashRequest {
        if (customer == null || customer.isBlank()) {
            throw new IllegalArgumentException("Customer must not be blank");
        }
        if (service == null || service.isBlank()) {
            throw new IllegalArgumentException("Service must not be blank");
        }
        customer = customer.strip();
        service = service.strip();
        Objects.requireNonNull(start, "Start time is required");
    }
}
