package fr.eris.lunaz.manager.gang.data.upgrade;

import com.google.gson.annotations.Expose;
import fr.eris.event.manager.EventHandler;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.gang.data.permission.enums.GangPermission;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import fr.eris.lunaz.manager.playerdata.data.currency.CurrencyType;
import fr.eris.lunaz.utils.ColorUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GangChestUpgrade implements Listener {
    @Getter private final List<UpgradePrice> upgradePrices;
    @Getter private final int maxLevel;

    @Expose @Getter private int currentLevel;
    @Expose @Getter private final HashMap<Integer, ItemStack> inventoryContent = new HashMap<>();
    @Expose @Getter private final UUID ownerID;

    private Inventory gangChestInventory;
    private String inventoryName = "";
    @Getter private final List<HumanEntity> currentViewers = new ArrayList<>();

    public GangChestUpgrade(UUID ownerID) {
        this.ownerID = ownerID;
        this.currentLevel = 0;
        upgradePrices = Arrays.asList(
                new UpgradePrice(CurrencyType.MONEY, 10000), new UpgradePrice(CurrencyType.MONEY, 20000),
                new UpgradePrice(CurrencyType.MONEY, 30000), new UpgradePrice(CurrencyType.MONEY, 40000),
                new UpgradePrice(CurrencyType.LUNA, 100), new UpgradePrice(CurrencyType.LUNA, 1000));
        this.maxLevel = upgradePrices.size();

        Bukkit.getServer().getPluginManager().registerEvents(this, LunaZ.getInstance());
        updateInventory();
    }

    public void updateInventory() {
        if(currentLevel == 0) {
            gangChestInventory = null;
            return;
        }
        List<HumanEntity> oldViewers = new ArrayList<>(currentViewers);
        for(HumanEntity viewer : oldViewers) viewer.closeInventory();

        saveItem();
        inventoryName = ColorUtils.translate("&6&l[&eBank&6&l] &7- Bank of " +
                LunaZ.getGangManager().getGang(ownerID).getGangDisplayName());
        gangChestInventory = Bukkit.createInventory(null, currentLevel * 9, inventoryName);
        loadItem();

        for(HumanEntity viewer : oldViewers) viewer.openInventory(gangChestInventory);
    }

    public void saveItem() {
        if(gangChestInventory != null) {
            for(int slot = 0 ; slot <= gangChestInventory.getSize() ; slot++) {
                ItemStack currentItem = gangChestInventory.getItem(slot);
                if(currentItem == null || currentItem.getType() == Material.AIR) {
                    continue;
                }
                inventoryContent.put(slot, currentItem);
            }
        }
    }

    public void loadItem() {
        for(Integer slot : new ArrayList<>(inventoryContent.keySet())) {
            if(gangChestInventory.getSize() < slot) {
                inventoryContent.remove(slot);
                continue;
            }
            gangChestInventory.setItem(slot, inventoryContent.get(slot));
        }
    }

    public void openInventory(Player target) {
        if(gangChestInventory == null) {
            updateInventory();
        }
        PlayerData playerData = PlayerData.getPlayerData(target.getUniqueId());
        currentViewers.add(target);
        target.openInventory(gangChestInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().contains(this.inventoryName)) return;
        event.setCancelled(true);
        PlayerData playerData = PlayerData.getPlayerData(event.getWhoClicked().getUniqueId());
        event.setCancelled(!playerData.getGangPlayer().havePermission(GangPermission.EDIT_BANK_ITEM, true));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!event.getView().getTitle().contains(this.inventoryName)) return;
        currentViewers.remove(event.getPlayer());
    }
}
