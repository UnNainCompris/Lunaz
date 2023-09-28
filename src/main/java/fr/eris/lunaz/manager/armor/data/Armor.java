package fr.eris.lunaz.manager.armor.data;

import com.google.gson.annotations.Expose;
import fr.eris.lunaz.utils.ItemUtils;
import fr.eris.lunaz.utils.NBTUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Armor {

    @Expose @Getter private final ArmorStatsBonus speedBoost;
    @Expose @Getter private final ArmorStatsBonus damageBoost;
    @Expose @Getter private final ArmorStatsBonus defenceBoost;
    @Expose @Getter private final ArmorStatsBonus healthBoost;

    @Expose @Getter private final String displayName;
    @Expose @Getter private final String systemName;
    @Expose @Getter private final Material material;

    public Armor(ArmorStatsBonus speedBoost, ArmorStatsBonus damageBoost, ArmorStatsBonus defenceBoost, ArmorStatsBonus healthBoost,
                 String displayName, String systemName, Material material) {
        this.speedBoost = speedBoost;
        this.damageBoost = damageBoost;
        this.defenceBoost = defenceBoost;
        this.healthBoost = healthBoost;
        this.displayName = displayName;
        this.systemName = systemName;
        this.material = material;
    }

    public static Armor defaultArmor() {
        return new Armor(new ArmorStatsBonus(1, 50), new ArmorStatsBonus(1, 50), new ArmorStatsBonus(1, 50), new ArmorStatsBonus(1, 50),
                "&6DefaultArmor", "defaultArmor", Material.LEATHER_CHESTPLATE);
    }

    public ItemStack getAsItem(Player requester) {
        double speedBoostValue = 0,  healthBoostValue = 0, damageBoostValue = 0, defenceBoostValue = 0;
        if(speedBoost != null) speedBoostValue = Math.round(speedBoost.getRandomValue(requester) * 10d) / 10d;
        if(healthBoost != null) healthBoostValue = Math.round(healthBoost.getRandomValue(requester) * 10d) / 10d;
        if(damageBoost != null) damageBoostValue = Math.round(damageBoost.getRandomValue(requester) * 10d) / 10d;
        if(defenceBoost != null) defenceBoostValue = Math.round(defenceBoost.getRandomValue(requester) * 10d) / 10d;

        return new NBTUtils(ItemUtils.createItem(material, displayName, 1,
                Arrays.asList(  (speedBoostValue != 0) ? "&bSpeed : &7&o" + speedBoostValue : null,
                                (healthBoostValue != 0) ? "&cHealth: &7&o" + healthBoostValue : null,
                                (damageBoostValue != 0) ? "&4Damage: &7&o" + damageBoostValue : null,
                                (defenceBoostValue != 0) ? "&fDefence: &7&o" + defenceBoostValue : null), null))
                .set("ArmorSystemName", this.systemName).set("ArmorStatsSpeedBoost", speedBoostValue).set("ArmorStatsHealthBoost", healthBoostValue)
                .set("ArmorStatsDamageBoost", damageBoostValue).set("ArmorStatsDefenceBoost", defenceBoostValue).build();
    }

    public ItemStack getAsDisplayItem() {
        return new NBTUtils(ItemUtils.createItem(material, displayName, 1,
                Arrays.asList(  (speedBoost != null) ? "&bSpeed : &7&o" + speedBoost.getMin() + " &f-> &7&o" + speedBoost.getMax() : null,
                                (healthBoost != null) ? "&cHealth: &7&o" + healthBoost.getMin() + " &f-> &7&o" + healthBoost.getMax() : null,
                                (damageBoost != null) ? "&4Damage: &7&o" + damageBoost.getMin() + " &f-> &7&o" + damageBoost.getMax() : null,
                                (defenceBoost != null) ? "&fDefence: &7&o" + defenceBoost.getMin() + " &f-> &7&o" + defenceBoost.getMax() : null), null))
                .set("ArmorSystemName", this.systemName).build();
    }
}
