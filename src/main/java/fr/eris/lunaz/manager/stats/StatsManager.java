package fr.eris.lunaz.manager.stats;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.playerdata.data.PlayerStats;
import fr.eris.lunaz.manager.stats.data.Stats;
import fr.eris.lunaz.manager.stats.data.TemporaryStats;
import fr.eris.lunaz.manager.stats.task.StatsUpdater;
import fr.eris.lunaz.manager.stats.task.TemporaryStatsUpdater;
import fr.eris.manager.Manager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StatsManager extends Manager {

    private StatsUpdater statsUpdater;
    private TemporaryStatsUpdater temporaryStatsUpdater;

    public void start() {
        statsUpdater = new StatsUpdater();
        temporaryStatsUpdater = new TemporaryStatsUpdater();
        statsUpdater.start();
        temporaryStatsUpdater.start();
    }

    public void stop() {
        statsUpdater.stop();
        temporaryStatsUpdater.stop();
    }

    public void addTemporaryStats(Player player, TemporaryStats temporaryStats, TemporaryStatsUpdater.Action todoIfAlreadyContains) {
        temporaryStatsUpdater.addTemporaryStats(player, temporaryStats, todoIfAlreadyContains);
    }

    public Stats getGlobalStats(Player player) {
        return getPlayerStats(player).addStats(getPlayerArmorStats(player)).addStats(getPlayerMainHandItemStats(player)).addStats(getTemporaryBuff(player));
    }
    
    public Stats getTemporaryBuff(Player player) {
        return temporaryStatsUpdater.getTemporaryStats(player);
    }

    public Stats getPlayerArmorStats(Player player) {
        return getPlayerHelmetStats(player).addStats(getPlayerChestplateStats(player))
                .addStats(getPlayerLeggingsStats(player)).addStats(getPlayerBootsStats(player));
    }

    public Stats getPlayerHelmetStats(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        if(helmet == null || helmet.getType() == Material.AIR) return Stats.emptyStats();
        return getStatsFromItem(helmet, "Armor");
    }

    public Stats getPlayerChestplateStats(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        if(chestplate == null || chestplate.getType() == Material.AIR) return Stats.emptyStats();
        return getStatsFromItem(chestplate, "Armor");
    }

    public Stats getPlayerLeggingsStats(Player player) {
        ItemStack leggings = player.getInventory().getLeggings();
        if(leggings == null || leggings.getType() == Material.AIR) return Stats.emptyStats();
        return getStatsFromItem(leggings, "Armor");
    }

    public Stats getPlayerBootsStats(Player player) {
        ItemStack boots = player.getInventory().getBoots();
        if(boots == null || boots.getType() == Material.AIR) return Stats.emptyStats();
        return getStatsFromItem(boots, "Armor");
    }

    public Stats getPlayerMainHandItemStats(Player player) {
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if(itemInMainHand.getType() == Material.AIR) return Stats.emptyStats();
        return getStatsFromItem(itemInMainHand, "Hand");
    }

    public Stats getPlayerStats(Player player) {
        PlayerStats playerStats = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getStats();
        return new Stats(playerStats.getSpeed(), playerStats.getHealth(), playerStats.getDamage(), playerStats.getDefence());
    }

    public double getNbtFromItem(ItemStack item, String tag) {
        NBTItem nbtItem = new NBTItem(item);
        if(!nbtItem.hasTag(tag)) return 0;
        return nbtItem.getDouble(tag);
    }

    public Stats getStatsFromItem(ItemStack item, String statsPrefix) {
        return new Stats(getNbtFromItem(item, statsPrefix + "StatsSpeedBoost"), getNbtFromItem(item, statsPrefix + "StatsHealthBoost"),
                getNbtFromItem(item, statsPrefix + "StatsDamageBoost"), getNbtFromItem(item, statsPrefix + "StatsDefenceBoost"));
    }
}
