package de.lars.Importants.Economy;

import java.util.UUID;

public class PlayerAccount {
    private final UUID playerId;
    private double balance;

    public PlayerAccount(UUID playerId, double balance) {
        this.playerId = playerId;
        this.balance = balance;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
    public boolean hasEnough(double amount) {
        return balance >= amount;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
}