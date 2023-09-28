package fr.eris.lunaz.manager.damage;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.melee.data.Melee;
import fr.eris.lunaz.manager.stats.data.Stats;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.manager.Manager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DamageManager extends Manager {
    public boolean applyWeaponDamage(LivingEntity target, Player attacker, ItemStack weaponItem) {
        Weapon weapon = LunaZ.getWeaponManager().getWeapon(weaponItem);
        if(weapon == null) return false;
        Stats attackerStats = LunaZ.getStatsManager().getGlobalStats(attacker);
        Stats targetStats = Stats.emptyStats();
        if(target instanceof Player)
            targetStats = LunaZ.getStatsManager().getGlobalStats((Player)target);
        double realDamage = processDamage(weapon.getWeaponDamage(), targetStats, attackerStats);
        target.setNoDamageTicks(0);
        target.damage(realDamage);
        return true;
    }

    public boolean applyMeleeDamage(LivingEntity target, Player attacker, ItemStack meleeItem) {
        Melee weapon = LunaZ.getMeleeManager().getMelee(meleeItem);
        if(weapon == null) return false;
        Stats attackerStats = LunaZ.getStatsManager().getGlobalStats(attacker);
        Stats targetStats = Stats.emptyStats();
        if(target instanceof Player)
            targetStats = LunaZ.getStatsManager().getGlobalStats((Player)target);
        double realDamage = processDamage(weapon.getMeleeDamage(), targetStats, attackerStats);
        target.damage(realDamage);
        return true;
    }

    public double processDamage(double damage, Stats targetStats, Stats attackerStats) {
        return damage + (attackerStats.getDamage() / 2) - (targetStats.getDefence() / 5);
    }
}