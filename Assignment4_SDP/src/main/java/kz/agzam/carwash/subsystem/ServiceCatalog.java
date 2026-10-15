package kz.agzam.carwash.subsystem;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

public final class ServiceCatalog {
    private final Map<String, ServiceQuote> services = Map.of(
            "BASIC", new ServiceQuote("BASIC", 30, 1, new BigDecimal("3000.00")),
            "PREMIUM", new ServiceQuote("PREMIUM", 60, 3, new BigDecimal("6000.00")));

    public ServiceQuote quote(String service) {
        if (service == null) {
            throw new IllegalArgumentException("Service is required");
        }
        ServiceQuote quote = services.get(service.strip().toUpperCase(Locale.ROOT));
        if (quote == null) {
            throw new IllegalArgumentException("Unknown wash service: " + service);
        }
        return quote;
    }
}
