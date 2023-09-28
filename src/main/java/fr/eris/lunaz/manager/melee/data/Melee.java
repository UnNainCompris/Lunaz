package fr.eris.lunaz.manager.melee.data;

import com.google.gson.annotations.Expose;
import fr.eris.lunaz.utils.ItemUtils;
import fr.eris.lunaz.utils.NBTUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Melee {

    @Expose @Getter private final MeleeStatsBonus speedBoost;
    @Expose @Getter private final MeleeStatsBonus damageBoost;
    @Expose @Getter private final MeleeStatsBonus defenceBoost;
    @Expose @Getter private final MeleeStatsBonus healthBoost;

    @Expose @Getter private final String displayName;
    @Expose @Getter private final String systemName;
    @Expose @Getter private final long meleeDamage;

    public Melee(MeleeStatsBonus speedBoost, MeleeStatsBonus damageBoost, MeleeStatsBonus defenceBoost, MeleeStatsBonus healthBoost,
                 String displayName, String systemName, long meleeDamage) {
        this.speedBoost = speedBoost;
        this.damageBoost = damageBoost;
        this.defenceBoost = defenceBoost;
        this.healthBoost = healthBoost;
        this.displayName = displayName;
        this.systemName = systemName;
        this.meleeDamage = meleeDamage;
    }

    public static Melee defaultMelee() {
        return new Melee(new MeleeStatsBonus(1, 5), new MeleeStatsBonus(1, 5), new MeleeStatsBonus(1, 5), new MeleeStatsBonus(1, 5),
                "&6DefaultMelee", "defaultMelee", 10);
    }

    public ItemStack getAsItem(Player requester) {
        double speedBoostValue = 0,  healthBoostValue = 0, damageBoostValue = 0, defenceBoostValue = 0;
        if(speedBoost != null) speedBoostValue = Math.round(speedBoost.getRandomValue(requester) * 10d) / 10d;
        if(healthBoost != null) healthBoostValue = Math.round(healthBoost.getRandomValue(requester) * 10d) / 10d;
        if(damageBoost != null) damageBoostValue = Math.round(damageBoost.getRandomValue(requester) * 10d) / 10d;
        if(defenceBoost != null) defenceBoostValue = Math.round(defenceBoost.getRandomValue(requester) * 10d) / 10d;

        return new NBTUtils(ItemUtils.createItem(Material.ARROW, displayName, 1,
                Arrays.asList(  (speedBoostValue != 0) ? "&bSpeed : &7&o" + speedBoostValue : null,
                                (healthBoostValue != 0) ? "&cHealth: &7&o" + healthBoostValue : null,
                                (damageBoostValue != 0) ? "&4Damage: &7&o" + damageBoostValue : null,
                                (defenceBoostValue != 0) ? "&fDefence: &7&o" + defenceBoostValue : null), null))
                .set("MeleeSystemName", this.systemName).set("HandStatsSpeedBoost", speedBoostValue).set("HandStatsHealthBoost", healthBoostValue)
                .set("HandStatsDamageBoost", damageBoostValue).set("HandStatsDefenceBoost", defenceBoostValue).build();
    }

    public ItemStack getAsDisplayItem() {
        return new NBTUtils(ItemUtils.createItem(Material.ARROW, displayName, 1,
                Arrays.asList(  (speedBoost != null) ? "&bSpeed : &7&o" + speedBoost.getMin() + " &f-> &7&o" + speedBoost.getMax() : null,
                                (healthBoost != null) ? "&cHealth: &7&o" + healthBoost.getMin() + " &f-> &7&o" + healthBoost.getMax() : null,
                                (damageBoost != null) ? "&4Damage: &7&o" + damageBoost.getMin() + " &f-> &7&o" + damageBoost.getMax() : null,
                                (defenceBoost != null) ? "&fDefence: &7&o" + defenceBoost.getMin() + " &f-> &7&o" + defenceBoost.getMax() : null), null))
                .set("MeleeSystemName", this.systemName).build();
    }
}
