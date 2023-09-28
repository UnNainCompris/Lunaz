package fr.eris.lunaz.manager.weapon.task;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.event.shoot.ShootEntityEvent;
import fr.eris.lunaz.event.weapon.WeaponReloadBulletEvent;
import fr.eris.lunaz.event.weapon.WeaponReloadMagazineEvent;
import fr.eris.lunaz.manager.actionbar.ActionbarManager;
import fr.eris.lunaz.manager.actionbar.Message;
import fr.eris.lunaz.manager.bullet.data.Bullet;
import fr.eris.lunaz.manager.magazine.data.Magazine;
import fr.eris.lunaz.manager.playerdata.data.PlayerStatus;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class ReloadTask {

    private final Player player;
    private final ItemStack weaponItem;
    private final Weapon weaponType;
    private final ReloadType reloadType;
    private BukkitTask reloadingTask;

    private final ProgressBar progressBar;
    private boolean done;

    private int tickCounter;

    public ReloadTask(Player player, ItemStack weaponItem, Weapon weaponType, ReloadType reloadType) {
        this.player = player;
        this.weaponItem = weaponItem;
        this.weaponType = weaponType;
        this.reloadType = reloadType;

        progressBar = new ProgressBar("|||||||||||||||");

        reloadingTask = BukkitTasks.asyncTimer((reloadType == ReloadType.BULLET) ? this::reloadBullet :
                this::reloadMagazine, 1, 1);
    }

    public void reloadBullet() {
        if(reloadingTask.isCancelled()) return;

        PlayerStatus playerStatus = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getStatus();
        if(tickCounter == 0) {
            if (playerStatus.isReloadingWeapon() || !weaponType.haveBullet(player)) {
                playerStatus.setReloadingWeapon(false);
                reloadingTask.cancel();
                return;
            }
            else playerStatus.setReloadingWeapon(true);
        }

        ActionbarManager actionBarManager = LunaZ.getActionbarManager();
        tickCounter++;
        Bullet bullet = LunaZ.getBulletManager().getBullet(weaponType.getBulletSystemName());
        ItemStack bulletItem = bullet.haveBullet(player);
        if (!player.getInventory().getItemInMainHand().equals(weaponItem) || bulletItem == null ||
                NBTUtils.toNBT(weaponItem).getInteger("WeaponReamingBullets") >= 1 || done) {
            actionBarManager.removeByReason(player, "ReloadWeapon");
            playerStatus.setReloadingWeapon(false);
            reloadingTask.cancel();
            return;
        }
        if (tickCounter == weaponType.getBulletReloadTime()) {
            WeaponReloadBulletEvent event = WeaponReloadBulletEvent.fromItem(player, weaponItem, bulletItem);
            LunaZ.getEventManager().postEvent(event);
            if(event == null || event.isCancelled()) {
                actionBarManager.removeByReason(player, "ReloadWeapon");
                playerStatus.setReloadingWeapon(false);
                reloadingTask.cancel();
                return;
            }
            playerStatus.setReloadingWeapon(false);
            int removedBullet = bullet.removeBullet(player, 1);
            actionBarManager.getByPlayer(player).removeByReason("DisplayBullets");
            actionBarManager.addPriority(player, new Message(20, "ReloadWeapon", () -> "&6You have reloaded"));
            player.getInventory().setItemInMainHand(new NBTUtils(weaponItem).set("WeaponReamingBullets", removedBullet).set("BulletMag", true).build());
            tickCounter = 0;
            done = true;
            return;
        }
        Message.MessageUpdater message = () -> "&7Reloading! &6[&e" + progressBar.getDone(tickCounter, weaponType.getBulletReloadTime()) +
                "&7" + progressBar.getNotDoneYet(tickCounter, weaponType.getBulletReloadTime()) + "&6]";
        actionBarManager.addPriority(player, new Message(20, "ReloadWeapon", message));
    }

    public void reloadMagazine() {
        if(reloadingTask.isCancelled()) return;

        PlayerStatus playerStatus = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getStatus();
        if(tickCounter == 0) {
            if (playerStatus.isReloadingWeapon() || !weaponType.haveMagazine(player)) {
                playerStatus.setReloadingWeapon(false);
                reloadingTask.cancel();
                return;
            }
            else playerStatus.setReloadingWeapon(true);
        }

        ActionbarManager actionBarManager = LunaZ.getActionbarManager();
        tickCounter++;
        Magazine magazine = LunaZ.getMagazineManager().getMagazine(weaponType.getMagazineSystemName());
        Tuple<ItemStack, Integer> magazineFound = magazine.findMagazine(player);
        if (!player.getInventory().getItemInMainHand().equals(weaponItem) || magazineFound == null ||
                NBTUtils.toNBT(weaponItem).getInteger("WeaponReamingBullets") >= magazine.getMagazineSize() || done) {
            actionBarManager.removeByReason(player, "ReloadWeapon");
            playerStatus.setReloadingWeapon(false);
            reloadingTask.cancel();
            return;
        }
        ItemStack magazineItem = magazineFound.getFirst();
        if (tickCounter == weaponType.getReloadTime()) {
            WeaponReloadMagazineEvent event = WeaponReloadMagazineEvent.fromItem(player, weaponItem, magazineItem);
            LunaZ.getEventManager().postEvent(event);
            if(event == null || event.isCancelled()) {
                actionBarManager.removeByReason(player, "ReloadWeapon");
                playerStatus.setReloadingWeapon(false);
                reloadingTask.cancel();
                return;
            }
            Tuple<ItemStack, Integer> removedMagazine = magazine.removeMagazine(player);
            playerStatus.setReloadingWeapon(false);
            if(removedMagazine == null) {
                reloadingTask.cancel();
                return;
            }
            actionBarManager.getByPlayer(player).removeByReason("DisplayBullets");
            actionBarManager.addPriority(player, new Message(20, "ReloadWeapon", () -> "&6You have reloaded"));
            if(new NBTItem(weaponItem).getBoolean("BulletMag")) {
                player.getInventory().addItem(LunaZ.getBulletManager().getBullet(weaponType.getBulletSystemName()).getAsItem());
            } else {
                player.getInventory().addItem(magazine.getAsItem(new NBTItem(weaponItem).getInteger("WeaponReamingBullets")));
            }
            player.getInventory().setItemInMainHand(new NBTUtils(weaponItem).set("WeaponReamingBullets", removedMagazine.getSecond()).set("BulletMag", false).build());
            tickCounter = 0;
            done = true;
            return;
        }
        Message.MessageUpdater message = () -> "&7Reloading! &6[&e" + progressBar.getDone(tickCounter, weaponType.getReloadTime()) +
                "&7" + progressBar.getNotDoneYet(tickCounter, weaponType.getReloadTime()) + "&6]";
        actionBarManager.addPriority(player, new Message(20, "ReloadWeapon", message));
    }

    public enum ReloadType {
        BULLET, MAGAZINE
    }
}
