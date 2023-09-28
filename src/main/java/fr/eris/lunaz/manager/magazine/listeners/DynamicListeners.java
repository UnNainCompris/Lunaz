package fr.eris.lunaz.manager.magazine.listeners;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class DynamicListeners implements Listener {
    public DynamicListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(this, LunaZ.getInstance());
    }

    @EventHandler
    public void onClickToReload(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item.getType() == Material.AIR) return;
        if(event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        //if(!event.getPlayer().isSneaking()) return;
        if(!LunaZ.getMagazineManager().isAnMagazine(item)) return;
        event.setCancelled(true);
        LunaZ.getMagazineManager().getMagazine(item).tryReload(event.getPlayer(), item);
    }
}
