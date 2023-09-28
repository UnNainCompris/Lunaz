package fr.eris.lunaz.utils.inventory;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.utils.BukkitTasks;
import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.InventoryUtils;
import fr.eris.lunaz.utils.item.ClickableItem;
import fr.eris.lunaz.utils.item.ItemCache;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class CustomInventory implements Listener {

    private final HashMap<Byte, ClickableItem> items;
    //                    slot      item
    private final HashMap<Byte, ClickableItem> toolbarItems;
    private final List<UUID> currentViewers;
    @Getter private Inventory inventory;

    private Consumer<CustomInventory> closeAction;
    private BukkitTask deleteTask;

    private byte inventorySize; // 9, 18, 27, 36, 45, 54
    private String inventoryName;

    public CustomInventory() {
        items = new HashMap<>();
        currentViewers = new ArrayList<>();
        toolbarItems = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, LunaZ.getInstance());
    }

    public CustomInventory setItem(int slot, ClickableItem item) {
        items.put((byte) slot, item);
        return this;
    }

    public CustomInventory setCloseAction(Consumer<CustomInventory> closeAction) {
        this.closeAction = closeAction;
        return this;
    }

    public CustomInventory setInventorySize(int inventoryRow) {
        this.inventorySize = InventoryUtils.getSizeFromInt(inventoryRow * 9);
        return this;
    }

    public CustomInventory setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
        return this;
    }

    private void loadInventory() {
        byte inventorySize = this.inventorySize;
        inventory = Bukkit.createInventory(null, inventorySize, ColorUtils.translate(inventoryName));
        if(items != null && !this.items.isEmpty())
            for(byte key : items.keySet()) {
                inventory.setItem(key, items.get(key).getItem());
            }
        setToolbar(inventory);
    }

    private void setToolbar(Inventory inventory) {
        InventoryUtils.setSideInventory(inventory, new ItemStack[]{ItemCache.placeHolder}, new InventoryUtils.Side[]{InventoryUtils.Side.DOWN});

        for(byte slot : toolbarItems.keySet()) {
            inventory.setItem(slot, toolbarItems.get(slot).getItem());
        }
    }

    public void destroy() {
        currentViewers.forEach(uuid -> Bukkit.getPlayer(uuid).closeInventory());
        HandlerList.unregisterAll(this);
    }

    public void update(HumanEntity... newViewers) {
        final List<Player> playerWithOpenedInventory = new ArrayList<>();
        for(UUID playerUUID : currentViewers) {
            final Player player = Bukkit.getPlayer(playerUUID);
            playerWithOpenedInventory.add(player);
        }
        loadInventory();
        for(Player player : playerWithOpenedInventory) {
            player.openInventory(inventory);
        }
        if(newViewers != null)
            for(HumanEntity newViewer : newViewers)
                newViewer.openInventory(inventory);
    }

    /**
     * @param slot the slot between 1 and 9 where we want to place the new item
     * @param item the item to place in the toolbar
     * @param force know if we don't care about everything else
     * @return the current instance
     */
    public CustomInventory addToolbarItem(int slot, ClickableItem item, boolean force) {
        if(slot > 9 || slot < 1) return this;
        if((this.toolbarItems.containsKey((byte)slot) || this.toolbarItems.containsValue(item)) && !force) return this;
        this.toolbarItems.put((byte) slot, item);
        this.update();
        return this;
    }

    /**
     * Use to know if the {@code inventory} is in the list
     * @param inventory The inventory we want to check
     * @return false if the inventory are not in the inventory list other is the index of the find inventory
     */
    public boolean isValidInventory(Inventory inventory) {
        if(inventory == null) return false;
        return this.inventory.equals(inventory);
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        if(!isValidInventory(event.getInventory())) return;
        if(event.getCurrentItem() == null) return;
        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();

        if(isToolbarSlot((byte) event.getSlot(), event.getInventory())) {
            for (final ClickableItem item : this.toolbarItems.values()) {
                if(item.getItem().equals(event.getCurrentItem())) {
                    item.executeAction(event);
                    break;
                }
            }
        } else {
            if(this.items != null && !this.items.isEmpty()) {
                for (final ClickableItem item : this.items.values()) {
                    if (item.getItem().equals(clickedItem)) {
                        item.executeAction(event);
                        break;
                    }
                }
            }
        }
    }

    public boolean isToolbarSlot(byte slot, Inventory currentInventory) {
        return slot + 1 >= currentInventory.getSize() - 8;
    }

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent event) {
        if(!isValidInventory(event.getInventory())) return;
        currentViewers.remove(event.getPlayer().getUniqueId());
        if(closeAction != null) closeAction.accept(this);
        if(currentViewers.isEmpty()) {
            this.deleteTask = BukkitTasks.asyncLater(this::destroy, 20 * 300);
        }
    }

    @EventHandler
    public void inventoryOpenEvent(InventoryOpenEvent event) {
        if(!isValidInventory(event.getInventory())) return;
        if(currentViewers.contains(event.getPlayer().getUniqueId())) return;

        currentViewers.add(event.getPlayer().getUniqueId());
        if(deleteTask != null) {
            deleteTask.cancel();
            deleteTask = null;
        }
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        if(!isValidInventory(event.getPlayer().getOpenInventory().getTopInventory())) return;
        currentViewers.remove(event.getPlayer().getUniqueId());
        if(currentViewers.isEmpty()) {
            this.deleteTask = BukkitTasks.asyncLater(this::destroy, 20 * 300);
        }
    }

}
