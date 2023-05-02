package de.lars.Importants.Economy;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileDataProvider implements DataProvider {

    private final File dataFile;

    public FileDataProvider(File dataFile) {
        this.dataFile = dataFile;
        if (!dataFile.getParentFile().exists()){
            dataFile.getParentFile().mkdirs();
        }
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public PlayerAccount getPlayerAccount(UUID playerId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(playerId.toString())) {
                    return new PlayerAccount(playerId, Double.parseDouble(parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PlayerAccount> getAllPlayerAccounts() {
        return null;
    }

    @Override
    public void saveTransaction(Transaction transaction) {

    }

    @Override
    public List<Transaction> getPlayerTransactions(UUID playerId) {
        return null;
    }

    @Override
    public void savePlayerAccount(PlayerAccount account) {
        Map<UUID, PlayerAccount> accounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                accounts.put(UUID.fromString(parts[0]), new PlayerAccount(UUID.fromString(parts[0]), Double.parseDouble(parts[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        accounts.put(account.getPlayerId(), account);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            for (PlayerAccount a : accounts.values()) {
                writer.write(a.getPlayerId().toString() + ":" + a.getBalance() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}