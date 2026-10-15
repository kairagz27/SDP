package kz.agzam.carwash.subsystem;

import java.math.BigDecimal;
import java.util.Objects;

public record ServiceQuote(String name, int minutes, int chemicalUnits, BigDecimal price) {
    public ServiceQuote {
        Objects.requireNonNull(name, "Name is required");
        Objects.requireNonNull(price, "Price is required");
        if (name.isBlank() || minutes <= 0 || chemicalUnits <= 0 || price.signum() <= 0) {
            throw new IllegalArgumentException("Invalid service quote");
        }
    }
}
