package fr.eris.lunaz.manager.bullet.data;

import com.google.gson.annotations.Expose;
import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.utils.ItemUtils;
import fr.eris.lunaz.utils.NBTUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Bullet {
    @Getter @Expose private final Material material;
    @Getter @Expose private final String bulletType; // Like 9mmXNato
    @Getter @Expose private final String systemName;
    @Getter @Expose private final String bulletDisplayName;

    public ItemStack getAsItem() {
        return new NBTUtils(ItemUtils.createItem(material, bulletDisplayName + " &Bullet [" + bulletType + "]", 1, null, null))
                .set("BulletSystemName", systemName).set("BulletType", bulletType).build();
    }

    public ItemStack haveBullet(Player player) {
        for(ItemStack item : player.getInventory().getContents()) {
            if(item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if(!nbtItem.hasNBTData()) continue;
            if(!nbtItem.hasTag("BulletSystemName")) continue;
            if(nbtItem.getString("BulletSystemName").equalsIgnoreCase(this.systemName))
                return item;
        }
        return null;
    }

    public int removeBullet(Player player, int amount) {
        int currentlyRemoved = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if (!nbtItem.hasNBTData()) continue;
            if (!nbtItem.hasTag("BulletSystemName")) continue;

            if (nbtItem.getString("BulletSystemName").equals(this.systemName)) {
                if(item.getAmount() == amount) {
                    player.getInventory().clear(Arrays.asList(player.getInventory().getContents()).indexOf(item));
                    return amount;
                } else if(item.getAmount() < amount) {
                    currentlyRemoved += amount - item.getAmount();
                    amount -= item.getAmount();
                    player.getInventory().clear(Arrays.asList(player.getInventory().getContents()).indexOf(item));
                } else if(item.getAmount() > amount) {
                    item.setAmount(item.getAmount() - amount);
                    return amount;
                }
            }
        }
        return currentlyRemoved;
    }

    public Bullet(String bulletType, String bulletDisplayName, String systemName, Material material) {
        this.bulletDisplayName = bulletDisplayName;
        this.systemName = systemName;
        this.bulletType = bulletType;
        this.material = material;
    }

    public static Bullet defaultBullet() {
        return new Bullet("9mm", "&6DefaultBullet", "defaultBullet", Material.OAK_BUTTON);
    }
}
