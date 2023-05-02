package de.lars.Importants.Economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.*;
import java.util.Currency;

public class EconomyService implements Economy {
    private final Currency currency;
    private final DataProvider dataProvider;

    private final Map<UUID, PlayerAccount> playerAccounts;


    public EconomyService(Currency currency, DataProvider dataProvider) {
        this.currency = currency;
        this.dataProvider = dataProvider;
        this.playerAccounts = new HashMap<>();
    }

    public Currency getCurrency() {
        return currency;
    }

    public void deposit(UUID playerId, double amount) {
        PlayerAccount account = dataProvider.getPlayerAccount(playerId);
        if (account != null) {
            account.deposit(amount);
            dataProvider.savePlayerAccount(account);
        }
    }

    public boolean withdraw(UUID playerId, double amount) {
        PlayerAccount account = dataProvider.getPlayerAccount(playerId);
        if (account != null && account.withdraw(amount)) {
            dataProvider.savePlayerAccount(account);
            return true;
        }
        return false;
    }

    public double getBalance(UUID playerId) {
        PlayerAccount account = dataProvider.getPlayerAccount(playerId);
        if (account != null) {
            return account.getBalance();
        }
        return 0.0;
    }

    public boolean transfer(UUID senderId, UUID receiverId, double amount) {
        PlayerAccount senderAccount = dataProvider.getPlayerAccount(senderId);
        PlayerAccount receiverAccount = dataProvider.getPlayerAccount(receiverId);

        if (senderAccount != null && receiverAccount != null && senderAccount.withdraw(amount)) {
            receiverAccount.deposit(amount);
            dataProvider.savePlayerAccount(senderAccount);
            dataProvider.savePlayerAccount(receiverAccount);
            return true;
        }
        return false;
    }

    public boolean hasEnough(UUID playerId, double amount) {
        PlayerAccount account = dataProvider.getPlayerAccount(playerId);
        return account != null && account.hasEnough(amount);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String s) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public double getBalance(String s) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return 0;
    }

    @Override
    public double getBalance(String s, String s1) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return 0;
    }

    @Override
    public boolean has(String s, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        return null;
    }
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        Player player = Bukkit.getPlayerExact(s);
        if (player == null) {
            return false;
        }
        UUID uuid = player.getUniqueId();
        if (dataProvider.getPlayerAccount(uuid) != null) {
            return false;
        }
        PlayerAccount account = new PlayerAccount(uuid, 0.0);
        dataProvider.savePlayerAccount(account);
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        UUID uuid = offlinePlayer.getUniqueId();
        if (dataProvider.getPlayerAccount(uuid) != null || playerAccounts.containsKey(uuid)) {
            return false;
        }
        PlayerAccount account = new PlayerAccount(uuid, 0.0);
        dataProvider.savePlayerAccount(account);
        playerAccounts.put(uuid, account);
        return true;
    }

    public boolean createPlayerAccount(String playerName, String playerUUID) {
        UUID uuid = UUID.fromString(playerUUID);
        if (dataProvider.getPlayerAccount(uuid) != null) {
            return false;
        }
        PlayerAccount account = new PlayerAccount(uuid, 0.0);
        dataProvider.savePlayerAccount(account);
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        UUID uuid = offlinePlayer.getUniqueId();
        if (dataProvider.getPlayerAccount(uuid) != null) {
            return false;
        }
        PlayerAccount account = new PlayerAccount(uuid, 0.0);
        dataProvider.savePlayerAccount(account);
        return true;
    }

    public void setBalance(UUID playerId, double amount) {
        PlayerAccount account = dataProvider.getPlayerAccount(playerId);
        if (account != null) {
            account.setBalance(amount);
            dataProvider.savePlayerAccount(account);
        }
    }
}