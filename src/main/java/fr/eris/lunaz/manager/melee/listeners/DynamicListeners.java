package fr.eris.lunaz.manager.melee.listeners;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.damage.DamageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DynamicListeners implements Listener {

    public DynamicListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(this, LunaZ.getInstance());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) return;
        Player attacker = (Player) event.getDamager();
        LivingEntity target = (LivingEntity) event.getEntity();
        ItemStack heldItem = attacker.getInventory().getItemInMainHand();
        if(!LunaZ.getMeleeManager().isAnMelee(heldItem)) return;
        event.setDamage(0.01d); // Only for the "knockback"
        LunaZ.getDamageManager().applyMeleeDamage(target, attacker, heldItem);
    }
}
