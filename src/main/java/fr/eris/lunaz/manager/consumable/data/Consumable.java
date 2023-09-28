package fr.eris.lunaz.manager.consumable.data;

import com.google.gson.annotations.Expose;
import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.consumable.task.ConsumeTask;
import fr.eris.lunaz.manager.stats.task.TemporaryStatsUpdater;
import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.ItemUtils;
import fr.eris.lunaz.utils.NBTUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Consumable {
    @Expose @Getter private final ConsumableStatsBonus speedBoost;
    @Expose @Getter private final ConsumableStatsBonus healthBoost;
    @Expose @Getter private final ConsumableStatsBonus damageBoost;
    @Expose @Getter private final ConsumableStatsBonus defenceBoost;

    @Expose @Getter private final String displayName;
    @Expose @Getter private final String systemName;
    @Expose @Getter private final Material material;

    @Expose @Getter private final long consumeDelayTickTime;
    @Expose @Getter private final long consumeCooldownMillis;

    public Consumable(ConsumableStatsBonus speedBoost, ConsumableStatsBonus healthBoost, ConsumableStatsBonus damageBoost,
                      ConsumableStatsBonus defenceBoost, String displayName, String systemName, Material material, long consumeDelayTickTime, long consumeCooldownMillis) {
        this.speedBoost = speedBoost;
        this.healthBoost = healthBoost;
        this.damageBoost = damageBoost;
        this.defenceBoost = defenceBoost;
        this.displayName = displayName;
        this.systemName = systemName;
        this.material = material;
        this.consumeDelayTickTime = consumeDelayTickTime;
        this.consumeCooldownMillis = consumeCooldownMillis;
    }


    public static Consumable defaultConsumable() {
        return new Consumable(new ConsumableStatsBonus(50, 600, TemporaryStatsUpdater.Action.ADD_TO_TIMER),
                new ConsumableStatsBonus(50, 600, TemporaryStatsUpdater.Action.ADD_TO_TIMER),
                new ConsumableStatsBonus(50, 600, TemporaryStatsUpdater.Action.ADD_TO_TIMER),
                new ConsumableStatsBonus(50, 600, TemporaryStatsUpdater.Action.ADD_TO_TIMER),
                "&6DefaultConsumable", "defaultConsumable", Material.GOLDEN_APPLE, 30, 1500);
    }

    public ItemStack getAsItem() {
        return new NBTUtils(ItemUtils.createItem(material, displayName, 1,
                Arrays.asList(  (speedBoost != null) ? "&bSpeed : &7&o" + speedBoost : null,
                                (healthBoost != null) ? "&cHealth: &7&o" + healthBoost : null,
                                (damageBoost != null) ? "&4Damage: &7&o" + damageBoost : null,
                                (defenceBoost != null) ? "&fDefence: &7&o" + defenceBoost : null,
                                "&8Cooldown : &o" + Math.round(consumeCooldownMillis / 100f) / 10f + "s"), null))
                .set("ConsumableSystemName", this.systemName).build();
    }

    public boolean haveConsumable(Player player) {
        for(ItemStack item : player.getInventory().getContents()) {
            if(item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if(!nbtItem.hasNBTData()) continue;
            if(!nbtItem.hasTag("ConsumableSystemName")) continue;
            if(nbtItem.getString("ConsumableSystemName").equalsIgnoreCase(this.systemName))
                return true;
        }
        return false;
    }

    public void consume(Player consumer, ItemStack consumableItem) {
        if(!LunaZ.getConsumableManager().canConsume(consumer, this)) {
            consumer.sendMessage(ColorUtils.translate("&7You need to wait before consuming this again"));
            return;
        }
        new ConsumeTask(consumer, this, consumableItem);
    }
}
