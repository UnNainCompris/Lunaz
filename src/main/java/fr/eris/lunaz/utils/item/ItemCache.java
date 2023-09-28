package fr.eris.lunaz.utils.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Use to load only 1 time certain item
 */
public class ItemCache {

    public static final ItemStack placeHolderGlowing = ItemBuilder.placeHolders(Material.GRAY_STAINED_GLASS_PANE, true).build();
    public static final ItemStack placeHolder = ItemBuilder.placeHolders(Material.GRAY_STAINED_GLASS_PANE, false).build();

    public static final ItemStack confirm = ItemBuilder.placeHolders(Material.GREEN_STAINED_GLASS_PANE, true).setDisplayName("&2Confirm").build();
    public static final ItemStack cancel = ItemBuilder.placeHolders(Material.RED_STAINED_GLASS_PANE, true).setDisplayName("&cCancel").build();
    public static final ItemStack nextPage = ItemBuilder.placeHolders(Material.GREEN_STAINED_GLASS_PANE, true).setDisplayName("&2Next Page").build();
    public static final ItemStack previousPage = ItemBuilder.placeHolders(Material.RED_STAINED_GLASS_PANE, true).setDisplayName("&cPrevious Page").build();
    public static final ItemStack back = ItemBuilder.placeHolders(Material.RED_STAINED_GLASS_PANE, false).setDisplayName("&cBack").build();
}
