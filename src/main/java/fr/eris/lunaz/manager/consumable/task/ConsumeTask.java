package fr.eris.lunaz.manager.consumable.task;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.actionbar.Message;
import fr.eris.lunaz.manager.consumable.data.Consumable;
import fr.eris.lunaz.manager.consumable.data.ConsumableStatsBonus;
import fr.eris.lunaz.manager.playerdata.data.PlayerStatus;
import fr.eris.lunaz.manager.stats.data.Stats;
import fr.eris.lunaz.manager.stats.data.TemporaryStats;
import fr.eris.lunaz.utils.BukkitTasks;
import fr.eris.lunaz.utils.ProgressBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;

public class ConsumeTask {
    private BukkitTask consumeTask;
    private final Player consumer;
    private final Consumable consumable;
    private final ItemStack consumableItem;
    private long consumeTimer;
    private final ProgressBar progressBar;
    public ConsumeTask(Player consumer, Consumable consumable, ItemStack consumableItem) {
        this.consumer = consumer;
        this.consumable = consumable;
        this.consumableItem = consumableItem;
        this.progressBar = new ProgressBar("|||||||||||||||");

        PlayerStatus playerStatus = LunaZ.getPlayerDataManager().getPlayerdata(consumer.getUniqueId()).getStatus();
        if(playerStatus.isConsumingConsumable()) {
            return;
        } else {
            playerStatus.setConsumingConsumable(true);
        }
        consumeTask = BukkitTasks.asyncTimer(this::update, 1L, 1L);
    }

    public void update() {
        consumeTimer++;
        if(consumeTask.isCancelled()) {
            removeProgressBar();
            return;
        } else {
            updateConsumeProgressBar();
        }

        if(!check()) {
            cancelTask();
            return;
        }

        if(consumeTimer >= consumable.getConsumeDelayTickTime()) {
            consume();
            cancelTask();
        }
    }

    public boolean check() {
        return consumer.getInventory().getItemInMainHand().equals(consumableItem);
    }

    public void removeProgressBar() {
        LunaZ.getActionbarManager().removeByReason(consumer, "ConsumingTimer");
    }

    public void updateConsumeProgressBar() {
        Message.MessageUpdater message = () -> "&7Consuming! &6[&e" + progressBar.getDone(consumeTimer, consumable.getConsumeDelayTickTime()) +
                "&7" + progressBar.getNotDoneYet(consumeTimer, consumable.getConsumeDelayTickTime()) + "&6]";
        LunaZ.getActionbarManager().addPriority(consumer, new Message(20, "ConsumingTimer", message));
    }

    public void consume() {
        removeConsumable();
        applyStatsBoost();
        LunaZ.getConsumableManager().putPlayerInCooldown(consumer, consumable);
    }

    public void cancelTask() {
        PlayerStatus playerStatus = LunaZ.getPlayerDataManager().getPlayerdata(consumer.getUniqueId()).getStatus();
        LunaZ.getActionbarManager().removeByReason(consumer, "ConsumingTimer");
        playerStatus.setConsumingConsumable(false);
        consumeTask.cancel();
    }

    private void removeConsumable() {
        if(consumableItem.getAmount() == 1) {
            consumer.getInventory().clear(Arrays.asList(consumer.getInventory().getContents()).indexOf(consumableItem));
        } else if(consumableItem.getAmount() > 1) {
            consumableItem.setAmount(consumableItem.getAmount() - 1);
        }
    }

    private void applyStatsBoost() {
        applySpeedStatsBoost();
        applyHealthStatsBoost();
        applyDamageStatsBoost();
        applyDefenceStatsBoost();
    }

    private void applySpeedStatsBoost() {
        if(consumable.getSpeedBoost() != null) {
            String consumeReason = "";
            if(consumable.getSpeedBoost().getCustomReasonName().equals("none"))
                consumeReason = consumable.getSystemName() + "ConsumableBoost";
            else consumeReason = consumable.getSpeedBoost().getCustomReasonName() + "ConsumableBoost";

            TemporaryStats temporaryStats =
                    new TemporaryStats(Stats.toSpeedStats(consumable.getSpeedBoost().getValue()),
                            consumable.getSpeedBoost().getTickTime(), consumeReason);
            LunaZ.getStatsManager().addTemporaryStats(consumer, temporaryStats, consumable.getSpeedBoost().getTodoIfAlreadyHaveBoost());
        }
    }
    private void applyHealthStatsBoost() {
        if(consumable.getHealthBoost() != null) {
            String consumeReason = "";
            if(consumable.getHealthBoost().getCustomReasonName().equals("none"))
                consumeReason = consumable.getSystemName() + "ConsumableBoost";
            else consumeReason = consumable.getHealthBoost().getCustomReasonName() + "ConsumableBoost";

            TemporaryStats temporaryStats =
                    new TemporaryStats(Stats.toHealthStats(consumable.getHealthBoost().getValue()),
                            consumable.getHealthBoost().getTickTime(), consumeReason);
            LunaZ.getStatsManager().addTemporaryStats(consumer, temporaryStats, consumable.getHealthBoost().getTodoIfAlreadyHaveBoost());
        }
    }
    private void applyDamageStatsBoost() {
        if(consumable.getDamageBoost() != null) {
            String consumeReason = "";
            if(consumable.getDamageBoost().getCustomReasonName().equals("none"))
                consumeReason = consumable.getSystemName() + "ConsumableBoost";
            else consumeReason = consumable.getDamageBoost().getCustomReasonName() + "ConsumableBoost";

            TemporaryStats temporaryStats =
                    new TemporaryStats(Stats.toDamageStats(consumable.getDamageBoost().getValue()),
                            consumable.getDamageBoost().getTickTime(), consumeReason);
            LunaZ.getStatsManager().addTemporaryStats(consumer, temporaryStats, consumable.getDamageBoost().getTodoIfAlreadyHaveBoost());
        }
    }
    private void applyDefenceStatsBoost() {
        if(consumable.getDefenceBoost() != null) {
            String consumeReason = "";
            if(consumable.getDefenceBoost().getCustomReasonName().equals("none"))
                consumeReason = consumable.getSystemName() + "ConsumableBoost";
            else consumeReason = consumable.getDefenceBoost().getCustomReasonName() + "ConsumableBoost";

            TemporaryStats temporaryStats =
                    new TemporaryStats(Stats.toDefenceStats(consumable.getDefenceBoost().getValue()),
                            consumable.getDefenceBoost().getTickTime(), consumeReason);
            LunaZ.getStatsManager().addTemporaryStats(consumer, temporaryStats, consumable.getDefenceBoost().getTodoIfAlreadyHaveBoost());
        }
    }
}
