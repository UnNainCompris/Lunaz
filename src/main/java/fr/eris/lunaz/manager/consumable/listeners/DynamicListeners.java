package fr.eris.lunaz.manager.consumable.listeners;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.consumable.data.Consumable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DynamicListeners implements Listener {

    public DynamicListeners() {
        Bukkit.getPluginManager().registerEvents(this, LunaZ.getInstance());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if(item == null || item.getType() == Material.AIR || item.getAmount() == 0) return;
        if(!LunaZ.getConsumableManager().isAnConsumable(item)) return;
        Consumable consumable = LunaZ.getConsumableManager().getConsumable(item);
        if(consumable == null) return;
        event.setCancelled(true);
        consumable.consume(event.getPlayer(), item);
    }
}
