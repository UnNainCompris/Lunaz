package fr.eris.lunaz.manager.magazine.data;

import com.google.gson.annotations.Expose;
import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.magazine.task.ReloadTask;
import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.ItemUtils;
import fr.eris.lunaz.utils.NBTUtils;
import fr.eris.lunaz.utils.Tuple;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

public class Magazine {
    @Expose @Getter private final int magazineSize;
    @Expose @Getter private final String magazineDisplayName;
    @Expose @Getter private final String systemName;
    @Expose @Getter private final String bulletSystemName;
    @Expose @Getter private final int timePerBullet; // reloadSpeed in ticks
    @Expose @Getter private final Material magazineMaterial;

    public Magazine(int magazineSize, String magazineDisplayName, String systemName, String bulletSystemName, int timePerBullet, Material magazineMaterial) {
        this.magazineSize = magazineSize;
        this.magazineDisplayName = magazineDisplayName;
        this.systemName = systemName;
        this.bulletSystemName = bulletSystemName;
        this.timePerBullet = timePerBullet;
        this.magazineMaterial = magazineMaterial;
    }

    public ItemStack getAsItem() {
        return new NBTUtils(ItemUtils.createItem(magazineMaterial, magazineDisplayName + " &7Magazine [" + magazineSize + "/" + magazineSize + "]", 1,
                ColorUtils.translate(Arrays.asList( "&7Bullet type: " + LunaZ.getBulletManager().getBullet(bulletSystemName).getSystemName(),
                        "&7Magazine reload speed: " + timePerBullet)),
                null)).set("MagazineSystemName", systemName).set("MagazineRemainingBullet", magazineSize).build();
    }

    public ItemStack getAsItem(int remainingBullet) {
        return new NBTUtils(ItemUtils.createItem(magazineMaterial, magazineDisplayName + " &7Magazine [" + remainingBullet + "/" + magazineSize + "]", 1,
                ColorUtils.translate(Arrays.asList( "&7Bullet type: " + LunaZ.getBulletManager().getBullet(bulletSystemName).getSystemName(),
                                                    "&7Magazine reload speed: " + timePerBullet)),
                null)).set("MagazineSystemName", systemName).set("MagazineRemainingBullet", remainingBullet).build();
    }

    public static Magazine defaultMagazine() {
        return new Magazine(30, "&6DefaultMagazine", "defaultMagazine", "defaultBullet", 2, Material.OAK_PLANKS);
    }

    public ItemStack haveMagazine(Player player) {
        for(ItemStack item : player.getInventory().getContents()) {
            if(item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if(!nbtItem.hasNBTData()) continue;
            if(!nbtItem.hasTag("MagazineSystemName")) continue;
            if(nbtItem.getString("MagazineSystemName").equalsIgnoreCase(this.systemName))
                return item;
        }
        return null;
    }

    public boolean haveBullet(Player player) { // Check if the player have bullet with the bullet type in there inventory
        return LunaZ.getBulletManager().getBullet(bulletSystemName).haveBullet(player) != null;
    }

    public Tuple<ItemStack, Integer> removeMagazine(Player player) {
        HashMap<ItemStack, Integer> weaponAmmoMap = new HashMap<>();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if (!nbtItem.hasNBTData()) continue;
            if (!nbtItem.hasTag("MagazineSystemName")) continue;
            if (nbtItem.getString("MagazineSystemName").equals(this.systemName)) {
                weaponAmmoMap.put(item, nbtItem.getInteger("MagazineRemainingBullet"));
            }
        }

        Tuple<ItemStack, Integer> betterMagazine = null;
        for(ItemStack magazine : weaponAmmoMap.keySet()) {
            if(betterMagazine == null) betterMagazine = new Tuple<>(magazine, weaponAmmoMap.get(magazine));
            if(weaponAmmoMap.get(magazine) > betterMagazine.getSecond()) {
                betterMagazine.setFirst(magazine);
                betterMagazine.setSecond(weaponAmmoMap.get(magazine));
            }
        }

        if(betterMagazine != null) {
            ItemStack item = betterMagazine.getFirst();
            if(item.getAmount() == 1)
                player.getInventory().clear(Arrays.asList(player.getInventory().getContents()).indexOf(item));
            else if(item.getAmount() > 1) item.setAmount(item.getAmount() - 1);
        }

        return betterMagazine;
    }

    public Tuple<ItemStack, Integer> findMagazine(Player player) {
        HashMap<ItemStack, Integer> weaponAmmoMap = new HashMap<>();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if (!nbtItem.hasNBTData()) continue;
            if (!nbtItem.hasTag("MagazineSystemName")) continue;
            if (nbtItem.getString("MagazineSystemName").equals(this.systemName)) {
                weaponAmmoMap.put(item, nbtItem.getInteger("MagazineRemainingBullet"));
            }
        }

        Tuple<ItemStack, Integer> betterMagazine = null;
        for(ItemStack magazine : weaponAmmoMap.keySet()) {
            if(betterMagazine == null) betterMagazine = new Tuple<>(magazine, weaponAmmoMap.get(magazine));
            if(weaponAmmoMap.get(magazine) > betterMagazine.getSecond()) {
                betterMagazine.setFirst(magazine);
                betterMagazine.setSecond(weaponAmmoMap.get(magazine));
            }
        }
        return betterMagazine;
    }

    public void tryReload(Player player, ItemStack magazine) {
        if(magazine.getAmount() > 1) {
            player.sendMessage("&c[LunaZ] &8 You can't reload stacked magazine !");
            return;
        }

        if(haveBullet(player)) {
            new ReloadTask(player, magazine, this);
        }
    }
}
