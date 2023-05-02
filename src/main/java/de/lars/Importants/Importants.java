package de.lars.Importants;

import de.lars.Importants.Commands.*;
import de.lars.Importants.Commands.Homes.DelHomeCommand;
import de.lars.Importants.Commands.Homes.HomeCommand;
import de.lars.Importants.Commands.Homes.SetHomeCommand;
import de.lars.Importants.Commands.Kits.KitCommand;
import de.lars.Importants.Commands.Kits.KitCreateCommand;
import de.lars.Importants.Commands.Kits.KitDeleteCommand;
import de.lars.Importants.Commands.Message.*;
import de.lars.Importants.Commands.Moderation.*;
import de.lars.Importants.Commands.Moderation.Warn.WarnCommand;
import de.lars.Importants.Commands.Moderation.Warn.WarnManager;
import de.lars.Importants.Commands.Shortcuts.AnvilCommand;
import de.lars.Importants.Commands.Shortcuts.CraftCommand;
import de.lars.Importants.Commands.Shortcuts.EnderchestCommand;
import de.lars.Importants.Commands.Shortcuts.GamemodeCommand;
import de.lars.Importants.Commands.Shortcuts.Report.CheckCommand;
import de.lars.Importants.Commands.Shortcuts.Report.DeleteReportCommand;
import de.lars.Importants.Commands.Shortcuts.Report.ReportCommand;
import de.lars.Importants.Commands.Shortcuts.Report.ReportManager;
import de.lars.Importants.Commands.SitCommands.SitCommand;
import de.lars.Importants.Commands.Spawn.*;
import de.lars.Importants.Commands.Teleports.*;
import de.lars.Importants.Commands.Time.DayCommand;
import de.lars.Importants.Commands.Time.NightCommand;
import de.lars.Importants.Commands.Time.TimeCommand;
import de.lars.Importants.Commands.Warps.DelWarpCommand;
import de.lars.Importants.Commands.Warps.SetWarpCommand;
import de.lars.Importants.Commands.Warps.WarpCommand;
import de.lars.Importants.Commands.World.WorldCommand;
import de.lars.Importants.Economy.Commands.weather.RainCommand;
import de.lars.Importants.Economy.Commands.weather.SunCommand;
import de.lars.Importants.Economy.Commands.weather.ThunderCommand;
import de.lars.Importants.Economy.Commands.weather.WeatherCommand;
import de.lars.Importants.Economy.Commands.BalanceCommand;
import de.lars.Importants.Economy.Commands.PayCommand;
import de.lars.Importants.Economy.Commands.QuickSellCommand;
import de.lars.Importants.Economy.DataProvider;
import de.lars.Importants.Economy.Commands.EconomyCommand;
import de.lars.Importants.Economy.EconomyService;
import de.lars.Importants.Economy.FileDataProvider;
import de.lars.Importants.Handlers.MessageHandler;
import de.lars.Importants.Managers.*;
import de.lars.Importants.config.Configuration;
import de.lars.Importants.listener.*;
import de.lars.Importants.listener.ChatManagerListenerPackage.ChatBot.ChatBotListener;
import de.lars.Importants.listener.ChatManagerListenerPackage.ChatColorListener;
import de.lars.Importants.listener.ChatManagerListenerPackage.ChatManagerListener;
import de.lars.Importants.listener.ChatManagerListenerPackage.JoinLeaveListener;
import de.lars.Importants.listener.ChatManagerListenerPackage.LinkBlocker;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class Importants extends JavaPlugin implements Listener {

    private Configuration config;
    private Map<UUID, Inventory> extraInventories;

    private static Importants instance;
    private HomeManager homeManager;
    private WarnManager warnManager;
    private WarpManager warpManager;
    private KitManager kitManager;
    private MessageHandler messageHandler;

    private ReportManager reportManager;
    private static List<String> blockedCommands = new ArrayList<>();
    private static List<String> blockedWords = new ArrayList<>();

    private Map<Player, Integer> playtimeMap;
    private BackCommand backCommand;
    public static ArrayList<String> mute = new ArrayList<String>();


    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static Economy economy = null;

    private EconomyService economyService;

    private EconomyCommand economyCommand;
    private static Map<UUID, Double> balances = new HashMap<>();
    private static Permission perms = null;
    private static Chat chat = null;
    private WarnCommand warnCommand;
    private Map<String, Integer> warnings;
    private String messagesLanguage;
    private String templateFileName;


    @Override
    public void onEnable() {
        File dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File accountsFile = new File(dataFolder, "accounts.json");
        if (!accountsFile.exists()) {
            try {
                accountsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            File dataFile = new File("data/accounts.json");
            DataProvider dataProvider = new FileDataProvider(dataFile);
            this.config = new Configuration(new File(getDataFolder(), "config.yml"));
            this.config.setTemplateName("/config.yml");
            this.config.load();
            Currency currency = Currency.getInstance(config.getString("economy"));
            economy = (Economy) new EconomyService(currency, dataProvider);
            instance = this;
            kitManager = new KitManager(this);
            messageHandler = new MessageHandler(this);
            ItemsOnJoin itemsOnJoin = new ItemsOnJoin(this);
            warpManager = new WarpManager(this);
            homeManager = new HomeManager(this);
            reportManager = new ReportManager(this);
            backCommand = new BackCommand(this);
            warnManager = new WarnManager(this);
            economyService = new EconomyService(currency, dataProvider);
            economyCommand = new EconomyCommand(economyService, this);
            blockedCommands = this.config.getStringList("blocked-commands");
            blockedWords = this.config.getStringList("blocked-words");
            CatchMobCommand catchMobCommand = new CatchMobCommand(this);
            getServer().getPluginManager().registerEvents(new DeathListener(this), this);
            getCommand("catchmob").setExecutor(catchMobCommand);
            ChatManagerListener chatManager = new ChatManagerListener(this);
            getServer().getPluginManager().registerEvents(chatManager, this);
            registerListener();
            registerCommands();
            registerModerationCommands();
            registerShortcuts();
            registerTeleportCommands();
            registerSpawnCommands();
            registerMessageCommands();
            registerWarpCommands();
            registerHomeCommands();
            registerEconomyCommands();
            registerKitCommands();
            registerSitCommand();
            reportManager.loadReports();
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled because no Vault Dependency found.", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        }

        @Override
        public void onDisable () {
            this.reportManager.saveReports();
        }

        public static Importants getInstance () {
            return instance;
        }


        public WarpManager getWarpManager () {
            return warpManager;
        }

        public KitManager getKitManager () {
            return kitManager;
        }

        public ReportManager getReportManager () {
            return reportManager;
        }


        public HomeManager getHomeManager () {
            return homeManager;
        }


        private void registerCommands () {
            getCommand("feed").setExecutor((CommandExecutor) new FeedCommand(this));
            getCommand("itemrename").setExecutor((CommandExecutor) new ItemRenameCommand(this));
            getCommand("invsee").setExecutor((CommandExecutor) new InventorySeeCommand(this));
            getCommand("heal").setExecutor((CommandExecutor) new HealCommand(this));
            getCommand("godmode").setExecutor((CommandExecutor) new GodModeCommand(this));
            getCommand("suicide").setExecutor((CommandExecutor) new SuicideCommand(this));
            getCommand("tnt").setExecutor((CommandExecutor) new TNTCommand(this));
            getCommand("fireball").setExecutor((CommandExecutor) new FireballCommand(this));
            getCommand("vanish").setExecutor((CommandExecutor) new VanishCommand(this));
            getCommand("fly").setExecutor((CommandExecutor) new FlyCommand(this));
            getCommand("phead").setExecutor((CommandExecutor) new PlayerHeadCommand(this));
            getCommand("back").setExecutor(backCommand);
            getCommand("time").setExecutor(new TimeCommand(this));
            getCommand("day").setExecutor(new DayCommand(this));
            getCommand("night").setExecutor(new NightCommand(this));
            getCommand("weather").setExecutor(new WeatherCommand(this));
            getCommand("sun").setExecutor(new SunCommand(this));
            getCommand("thunder").setExecutor(new ThunderCommand(this));
            getCommand("rain").setExecutor(new RainCommand(this));
            getCommand("spawnmob").setExecutor(new SpawnMobCommand(this));
            getCommand("killall").setExecutor(new KillallCommand(this));
            getCommand("repair").setExecutor(new RepairCommand(this));
            getCommand("clear").setExecutor(new ClearCommand(this));
            getCommand("jump").setExecutor(new JumpCommand(this));
            getCommand("ping").setExecutor(new PingCommand(this));
            getCommand("nick").setExecutor(new NickCommand(this));
            getCommand("afk").setExecutor(new AFKCommand(this));
            getCommand("signedit").setExecutor(new SigneditCommand(this));
            getCommand("world").setExecutor(new WorldCommand(this));
            getCommand("rtp").setExecutor(new RTPCommand(this));
            getCommand("ireload").setExecutor(new ReloadCommand(this, config));
            getCommand("report").setExecutor(new ReportCommand(reportManager, this));
            getCommand("delreport").setExecutor(new DeleteReportCommand(reportManager, this));
            getCommand("checkreport").setExecutor(new CheckCommand(reportManager, this));
            getCommand("fake").setExecutor(new FakeCommand(this));
            getCommand("exhealth").setExecutor(new ExHealthCommand(this));
            getCommand("killingstats").setExecutor(new KillingStatsCommand(this));
    }
        private void registerModerationCommands () {
            getCommand("ban-ip").setExecutor((CommandExecutor) new BanIPCommand(this));
            getCommand("unban-ip").setExecutor((CommandExecutor) new UnbanIpCommand(this));
            getCommand("tempban").setExecutor((CommandExecutor) new TempBanCommand(this));
            getCommand("ban").setExecutor((CommandExecutor) new BanCommand(this));
            getCommand("kick").setExecutor((CommandExecutor) new KickCommand(this));
            getCommand("unban").setExecutor((CommandExecutor) new UnbanCommand(this));
            getCommand("importants").setExecutor(new HelpCommand(this));
            getCommand("hat").setExecutor(new HatCommand(this));
            getCommand("pinfo").setExecutor(new PlayerInfoCommand(this));
            this.warnCommand = new WarnCommand(this);
            this.getCommand("warn").setExecutor(this.warnCommand);
            getCommand("kickall").setExecutor(new KickAllCommand(this));
    }

        private void registerShortcuts () {
            getCommand("gm").setExecutor((CommandExecutor) new GamemodeCommand(this));
            getCommand("craft").setExecutor((CommandExecutor) new CraftCommand(this));
            getCommand("ec").setExecutor((CommandExecutor) new EnderchestCommand(this));
            getCommand("anvil").setExecutor((CommandExecutor) new AnvilCommand(this));
        }

        private void registerEconomyCommands(){
            getCommand("economy").setExecutor(economyCommand);
            getCommand("balance").setExecutor(new BalanceCommand(economyService, this));
            getCommand("money").setExecutor(new BalanceCommand(economyService, this));
            getCommand("eco").setExecutor(economyCommand);
            getCommand("pay").setExecutor(new PayCommand(economyService, this));
            getCommand("quicksell").setExecutor(new QuickSellCommand(this, economyService));
    }
        private void registerTeleportCommands () {
            getCommand("tpa").setExecutor((CommandExecutor) new TpaCommand(this));
            getCommand("tpahere").setExecutor((CommandExecutor) new TpahereCommand(this));
            getCommand("tpaccept").setExecutor((CommandExecutor) new TpAcceptCommand(this));
            getCommand("tpdecline").setExecutor((CommandExecutor) new TpDeclineCommand(this));

        }

        private void registerSpawnCommands () {
            getCommand("spawn").setExecutor(new SpawnCommand(this));
            getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
            getCommand("hub").setExecutor(new HubCommand(this));
            getCommand("sethub").setExecutor(new SetHubCommand(this));
            getCommand("lobby").setExecutor((CommandExecutor) new LobbyCommand(this));
            getCommand("setlobby").setExecutor((CommandExecutor) new SetLobbyCommand(this));
            getCommand("setall").setExecutor((CommandExecutor) new SetAllCommand(this));
        }

        private void registerWarpCommands () {
            getCommand("delwarp").setExecutor(new DelWarpCommand(this));
            getCommand("setwarp").setExecutor(new SetWarpCommand(this));
            getCommand("warp").setExecutor(new WarpCommand(this));
        }

        private void registerHomeCommands () {
            getCommand("sethome").setExecutor(new SetHomeCommand(this));
            getCommand("delhome").setExecutor(new DelHomeCommand(this));
            getCommand("home").setExecutor(new HomeCommand(homeManager, this));
        }

        private void registerSitCommand(){
            getCommand("sit").setExecutor(new SitCommand(this));
        }
        private void registerKitCommands () {
            getCommand("kit").setExecutor(new KitCommand(kitManager, this));
            getCommand("kitcreate").setExecutor(new KitCreateCommand(kitManager, this));
            getCommand("kitdelete").setExecutor(new KitDeleteCommand(kitManager, this));
        }
        private void registerMessageCommands () {
            getCommand("msg").setExecutor((CommandExecutor) new MessageCommand(this));
            getCommand("broadcast").setExecutor(new BroadcastCommand(this));
            getCommand("announce").setExecutor(new AnnounceCommand(this));
            getCommand("clearchat").setExecutor(new ClearchatCommand(this));
            getCommand("filter").setExecutor(new FilterCommand(this));
        }

        private void registerListener() {
            AFKListener afkListener = new AFKListener(config, this);
            getServer().getPluginManager().registerEvents(new TreeCuttingListener(), this);
            DeathChestListener deathChestListener = new DeathChestListener(config, this);
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(economyService), this);
            getServer().getPluginManager().registerEvents(new BackListener(backCommand), this);
            getServer().getPluginManager().registerEvents(deathChestListener, this);
            getServer().getPluginManager().registerEvents(afkListener, this);
            getServer().getPluginManager().registerEvents(this, this);
            getServer().getPluginManager().registerEvents(new SpawnerListener(this), this);
            getServer().getPluginManager().registerEvents(new OreMiningListener(this), this);
            getServer().getPluginManager().registerEvents(new ChatColorListener(), this);
            getServer().getPluginManager().registerEvents(new SignColorListener(), this);
            getServer().getPluginManager().registerEvents(new MobCatcherListener(), this);
            getServer().getPluginManager().registerEvents(new DropPlayerHead(config), this);
            getServer().getPluginManager().registerEvents(new CarryListener(), this);
            getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);
            getServer().getPluginManager().registerEvents(new SeatListener(), this);
            LinkBlocker linkBlocker = new LinkBlocker(config, this);
            getServer().getPluginManager().registerEvents(linkBlocker, this);
            getServer().getPluginManager().registerEvents(new ClearLaggListener(config), this);
            getServer().getPluginManager().registerEvents(new ChatBotListener(this), this);
            getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
        }


        public MessageHandler getMessageHandler () {
            return messageHandler;
        }

        @Override
        public Configuration getConfig () {
            return config;
        }

        @EventHandler
        public void onChat (AsyncPlayerChatEvent event){
            Player player = event.getPlayer();
            if (player.hasPermission("Importants.filter")) {
                return;
            }
            String message = event.getMessage().toLowerCase();
            for (String blockedWord : blockedWords) {
                if (message.contains(blockedWord)) {
                    player.sendMessage(this.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                            + this.getMessageHandler()
                            .getMessage("not_allowed")
                            .orElse("§cThis Word/Command is not allowed"));
                    event.setCancelled(true);
                }
            }
        }

        @EventHandler
        public void onBlockedCommand (PlayerCommandPreprocessEvent event){
            Player player = event.getPlayer();
            if (player.hasPermission("Importants.filter")) {
                return;
            }
            String message = event.getMessage().split(" ")[0].substring(1);

            for (String blockedCommand : blockedCommands) {
                if (blockedCommand.equalsIgnoreCase(message)) {
                    if (blockedCommand.contains(message)) {
                        player.sendMessage(this.getMessageHandler().getMessage("prefix").orElse("§0§l[§eImportants§0§l]") + " "
                                + this.getMessageHandler()
                                .getMessage("not_allowed")
                                .orElse("§cThis Word/Command is not allowed"));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

        public void addBlockedCommand (String cmd){
            blockedCommands.add(cmd);
            config.set("blocked-commands", blockedCommands);
            config.save();
        }

        public void addBlockedWord (String word){
            blockedWords.add(word);
            config.set("blocked-words", blockedWords);
            config.save();
        }

        public void reloadConfig () {
            Configuration Configuration = null;
            config = Configuration;
            config.load();
        }

        private void loadConfig () {
            getConfig().options().copyDefaults(true);
            saveConfig();

            for (String key : getConfig().getConfigurationSection("Playtime").getKeys(false)) {
                int playtime = Integer.parseInt(key);
                String command = getConfig().getString("Playtime." + key + ".command");
                String permission = getConfig().getString("Playtime." + key + ".permission");
                String message = getConfig().getString("Playtime." + key + ".message");
            }
            blockedCommands = config.getStringList("blocked-commands");
            blockedWords = config.getStringList("blocked-words");
        }


        private boolean setupChat() {
            RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
            chat = rsp.getProvider();
            return chat != null;
        }

        private boolean setupPermissions() {
            RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
            perms = rsp.getProvider();
            return perms != null;
        }


        public static Economy getEconomy() {
            return econ;
        }

        public static Permission getPermissions() {
            return perms;
        }

        public static Chat getChat() {
            return chat;
        }
    public static boolean hasAccount() {
        Player player = Bukkit.getPlayer("Spielername");
        UUID uniqueId = player.getUniqueId();
        return balances.containsKey(player.getUniqueId());
    }

    public static double getBalance(Player player) {
        return balances.getOrDefault(player.getUniqueId(), 0.0);
    }

    public static void deposit(Player player, double amount) {
        economy.depositPlayer(player, amount);
        balances.put(player.getUniqueId(), getBalance(player) + amount);
    }

    public static boolean withdraw(Player player, double amount) {
        if (getBalance(player) >= amount) {
            economy.withdrawPlayer(player, amount);
            balances.put(player.getUniqueId(), getBalance(player) - amount);
            return true;
        }
        return false;
    }

    private static boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            economy = rsp.getProvider();
        }
        return (economy != null);
    }

}







