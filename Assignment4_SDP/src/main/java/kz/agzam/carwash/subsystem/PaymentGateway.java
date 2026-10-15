package kz.agzam.carwash.subsystem;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class PaymentGateway {
    private final Map<String, BigDecimal> balances = new HashMap<>();
    private final Map<UUID, Charge> charges = new HashMap<>();

    public PaymentGateway(Map<String, BigDecimal> initialBalances) {
        initialBalances.forEach((customer, balance) -> {
            if (customer == null || customer.isBlank() || balance == null || balance.signum() < 0) {
                throw new IllegalArgumentException("Invalid customer balance");
            }
            balances.put(customer, balance);
        });
    }

    public void charge(UUID bookingId, String customer, BigDecimal amount) {
        Objects.requireNonNull(bookingId, "Booking ID is required");
        Objects.requireNonNull(amount, "Amount is required");
        if (amount.signum() <= 0 || charges.containsKey(bookingId)) {
            throw new IllegalArgumentException("Invalid amount or duplicate payment");
        }
        BigDecimal balance = balanceOf(customer);
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance for " + customer);
        }
        balances.put(customer, balance.subtract(amount));
        charges.put(bookingId, new Charge(customer, amount));
    }

    public void refund(UUID bookingId) {
        Charge charge = charges.remove(bookingId);
        if (charge != null) {
            balances.put(charge.customer(), balanceOf(charge.customer()).add(charge.amount()));
        }
    }

    public BigDecimal balanceOf(String customer) {
        return balances.getOrDefault(customer, BigDecimal.ZERO);
    }

    private record Charge(String customer, BigDecimal amount) {
    }
}
