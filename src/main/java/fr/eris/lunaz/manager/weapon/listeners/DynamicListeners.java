package fr.eris.lunaz.manager.weapon.listeners;

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
    public void onClickToShoot(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item.getType() == Material.AIR) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(!LunaZ.getWeaponManager().isAnWeapon(item)) return;
        event.setCancelled(true);

        Player player = event.getPlayer();
        Weapon weapon = LunaZ.getWeaponManager().getWeapon(item);
        int reamingBullet = NBTUtils.toNBT(item).getInteger("WeaponReamingBullets");
        if(reamingBullet > 0) weapon.shoot(player, item);
        else weapon.tryReload(player, item);
    }

    @EventHandler
    public void onClickToReload(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item.getType() == Material.AIR) return;
        if(event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        //if(!event.getPlayer().isSneaking()) return;
        if(!LunaZ.getWeaponManager().isAnWeapon(item)) return;
        event.setCancelled(true);
        LunaZ.getWeaponManager().getWeapon(item).tryReload(event.getPlayer(), item);
    }

    @EventHandler
    public void onItemSlotChanged(PlayerItemHeldEvent event) {
        final ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if(item == null) return;
        if (item.getType() == Material.AIR) return;
        if (LunaZ.getWeaponManager().isAnWeapon(item)) {
            final Player player = event.getPlayer();
            final Weapon weapon = LunaZ.getWeaponManager().getWeapon(item);
            weapon.displayBullet(player, item, true);
        }
    }
}
