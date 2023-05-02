package de.lars.Importants.Economy;

import java.util.UUID;

public class Transaction {
    private final UUID fromPlayerId;
    private final UUID toPlayerId;
    private final double amount;

    public Transaction(UUID fromPlayerId, UUID toPlayerId, double amount) {
        this.fromPlayerId = fromPlayerId;
        this.toPlayerId = toPlayerId;
        this.amount = amount;
    }

    public UUID getFromPlayerId() {
        return fromPlayerId;
    }

    public UUID getToPlayerId() {
        return toPlayerId;
    }

    public double getAmount() {
        return amount;
    }
}
