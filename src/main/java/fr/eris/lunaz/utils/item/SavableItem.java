package fr.eris.lunaz.utils.item;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SavableItem {

    @Expose @Getter @Setter private String displayName;
    @Expose @Getter @Setter private List<String> lore;
    @Expose @Getter @Setter private Material type;
    @Expose @Getter @Setter private int amount;

    public SavableItem(String displayName, List<String> lore, Material type, int amount) {
        this.displayName = displayName;
        this.lore = lore;
        this.type = type;
        this.amount = amount;
    }

    public static SavableItem fromItem(ItemStack item) {
        if(!item.hasItemMeta()) return new SavableItem(null, null, item.getType(), item.getAmount());
        return new SavableItem(item.getItemMeta().getDisplayName(), item.getItemMeta().getLore(), item.getType(), item.getAmount());
    }

    public ItemStack toItem() {
        ItemBuilder itemBuilder = new ItemBuilder();
        if(type != null) itemBuilder.setMaterial(type);
        if(lore != null) itemBuilder.setLore(lore);
        if(displayName != null) itemBuilder.setDisplayName(displayName);
        if(amount != 0) itemBuilder.setAmount(amount);
        return itemBuilder.build();
    }

}
