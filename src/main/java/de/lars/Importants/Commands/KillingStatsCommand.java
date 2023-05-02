package de.lars.Importants.Commands;

import de.lars.Importants.Importants;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class KillingStatsCommand implements CommandExecutor {

    private final Importants plugin;

    public KillingStatsCommand(Importants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Dieser Command kann nur von einem Spieler ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            int deaths = player.getStatistic(Statistic.DEATHS);
            int playerKills = player.getStatistic(Statistic.PLAYER_KILLS);
            int mobKills = player.getStatistic(Statistic.MOB_KILLS);

            player.sendMessage(ChatColor.GREEN + "--------- Killing Stats ---------");
            player.sendMessage(ChatColor.GOLD + "Gestorben: " + deaths + " mal");
            player.sendMessage(ChatColor.GOLD + "Spieler getötet: " + playerKills + " mal");
            player.sendMessage(ChatColor.GOLD + "Mobs getötet: " + mobKills + " mal");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("monster")) {
                int mobKills = player.getStatistic(Statistic.KILL_ENTITY, EntityType.ZOMBIE) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.SKELETON) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.CREEPER) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.SPIDER) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.ENDERMAN) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.ZOMBIFIED_PIGLIN) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.WITCH) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.BLAZE) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.MAGMA_CUBE) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.HUSK);

                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_title_monster").orElse("§7--------- §aKilling Stats §7- §aMonster §7---------"));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_monster_kills").orElse("§aTotal monster kills: %monster%").replace("%monster%",String.valueOf(mobKills)));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_zombie_kills").orElse("§aZombies killed: %zombies%").replace("%monster%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.ZOMBIE))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_skeleton_kills").orElse("§aSkeletons killed: %skeletons%").replace("%skeletons%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.SKELETON))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_creeper_kills").orElse("§aCreepers killed: %creeper%").replace("%creeper%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.CREEPER))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_spider_kills").orElse("§aSpiders killed: %spider%").replace("%spider%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.SPIDER))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_enderman_kills").orElse("§aEndermans killed: %creeper%").replace("%enderman%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.ENDERMAN))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_zombified_piglin_kills").orElse("§aZombified Piglins killed: %zombified_piglins%").replace("%zombified_piglins%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.ZOMBIFIED_PIGLIN))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_witch_kills").orElse("§aWitches killed: %zombified_piglins%").replace("%witch%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.WITCH))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_blaze_kills").orElse("§aBlazes killed: %blaze%").replace("%blaze%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.BLAZE))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_magma_kills").orElse("§aMagma killed: %magma%").replace("%magma%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.MAGMA_CUBE))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_husk_kills").orElse("§aHusks killed: %husk%").replace("%husk%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.HUSK))));
            } else if (args[0].equalsIgnoreCase("mobs")) {
                int mobKills = player.getStatistic(Statistic.KILL_ENTITY, EntityType.PIG) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.COW) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.SHEEP) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.CHICKEN) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.RABBIT) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.HORSE) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.DONKEY) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.MULE) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.LLAMA) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.POLAR_BEAR) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.PANDA) +
                        player.getStatistic(Statistic.KILL_ENTITY, EntityType.FOX);
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_title_mob").orElse("§7--------- §aKilling Stats §7- §aMobs §7---------"));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_mob_kills").orElse("§aTotal mob kills: %mobs%").replace("%mobs%",String.valueOf(mobKills)));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_pig_kills").orElse("§aPigs killed: %pig%").replace("%pig%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.PIG))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_cow_kills").orElse("§aCows killed: %cow%").replace("%cow%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.COW))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_sheep_kills").orElse("§aSheeps killed: %sheep%").replace("%sheep%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.SHEEP))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_chicken_kills").orElse("§aChicken killed: %chicken%").replace("%chicken%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.CHICKEN))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_rabbit_kills").orElse("§aRabbits killed: %rabbit%").replace("%rabbit%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.RABBIT))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_horse_kills").orElse("§aHorse killed: %horse%").replace("%horse%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.HORSE))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_donkey_kills").orElse("§aDonkey killed: %donkey%").replace("%donkey%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.DONKEY))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_mule_kills").orElse("§aMules killed: %mule%").replace("%mule%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.MULE))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_llama_kills").orElse("§aLLamas killed: %llama%").replace("%llama%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.LLAMA))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_polar_bear_kills").orElse("§aPolar Bears killed: %polar_bear%").replace("%polar_bear%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.POLAR_BEAR))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_panda_kills").orElse("§aPanda killed: %panda%").replace("%panda%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.PANDA))));
                player.sendMessage(plugin.getMessageHandler().getMessage("killing_stats_total_fox_kills").orElse("§aFox killed: %fox%").replace("%fox%",String.valueOf(player.getStatistic(Statistic.KILL_ENTITY, EntityType.FOX))));
            }
        }
        return false;
    }
}