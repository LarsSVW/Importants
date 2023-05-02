package de.lars.Importants.Economy;

import java.util.List;
import java.util.UUID;

public interface DataProvider {
    void savePlayerAccount(PlayerAccount account);
    PlayerAccount getPlayerAccount(UUID playerId);
    List<PlayerAccount> getAllPlayerAccounts();
    void saveTransaction(Transaction transaction);
    List<Transaction> getPlayerTransactions(UUID playerId);
}