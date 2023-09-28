package fr.eris.lunaz.manager.magazine.task;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.event.magazine.ReloadMagazineEvent;
import fr.eris.lunaz.event.weapon.WeaponReloadBulletEvent;
import fr.eris.lunaz.manager.actionbar.ActionbarManager;
import fr.eris.lunaz.manager.actionbar.Message;
import fr.eris.lunaz.manager.bullet.data.Bullet;
import fr.eris.lunaz.manager.magazine.data.Magazine;
import fr.eris.lunaz.manager.playerdata.data.PlayerStatus;
import fr.eris.lunaz.utils.BukkitTasks;
import fr.eris.lunaz.utils.NBTUtils;
import fr.eris.lunaz.utils.ProgressBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class ReloadTask {

    private final Player player;
    private ItemStack magazineItem;
    private Magazine magazineType;
    private BukkitTask reloadingTask;

    private final ProgressBar progressBar;

    private int tickCounter = -1;

    public ReloadTask(Player player, ItemStack magazineItem, Magazine magazineType) {
        this.player = player;
        progressBar = new ProgressBar("|||||||||||||||");
        this.magazineItem = magazineItem;
        this.magazineType = magazineType;
        reloadingTask = BukkitTasks.asyncTimer(this::reloadMagazine, 1, 1);
    }

    public void reloadMagazine() {
        if(reloadingTask.isCancelled()) return;

        PlayerStatus playerStatus = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getStatus();
        if(tickCounter == -1) {
            if (playerStatus.isReloadingMagazine() || !magazineType.haveBullet(player)) {
                playerStatus.setReloadingMagazine(false);
                reloadingTask.cancel();
                return;
            }
            else playerStatus.setReloadingMagazine(true);
            tickCounter = 0;
        }

        ActionbarManager actionBarManager = LunaZ.getActionbarManager();
        tickCounter++;
        Bullet bullet = LunaZ.getBulletManager().getBullet(magazineType.getBulletSystemName());
        ItemStack bulletItem = bullet.haveBullet(player);
        if (!player.getInventory().getItemInMainHand().equals(magazineItem) || bulletItem == null ||
                NBTUtils.toNBT(magazineItem).getInteger("MagazineRemainingBullet") >= magazineType.getMagazineSize()) {
            actionBarManager.removeByReason(player, "ReloadMagazine");
            playerStatus.setReloadingMagazine(false);
            reloadingTask.cancel();
            return;
        }
        if (tickCounter == magazineType.getTimePerBullet()) {
            ReloadMagazineEvent event = ReloadMagazineEvent.fromItem(player, magazineItem, bulletItem);
            LunaZ.getEventManager().postEvent(event);
            if(event == null || event.isCancelled()) {
                actionBarManager.removeByReason(player, "ReloadMagazine");
                playerStatus.setReloadingMagazine(false);
                reloadingTask.cancel();
                return;
            }
            int removedBullet = bullet.removeBullet(player, 1);
            player.getInventory().setItemInMainHand(magazineType.getAsItem(new NBTItem(player.getInventory().getItemInMainHand())
                    .getInteger("MagazineRemainingBullet") + removedBullet));
            magazineItem = player.getInventory().getItemInMainHand();
            tickCounter = 0;
            return;
        }
        Message.MessageUpdater message = () -> "&7Reloading! &6[&e" + progressBar.getDone(tickCounter, magazineType.getTimePerBullet()) +
                "&7" + progressBar.getNotDoneYet(tickCounter, magazineType.getTimePerBullet()) + "&6]";
        actionBarManager.addPriority(player, new Message(20, "ReloadMagazine", message));
    }
}
