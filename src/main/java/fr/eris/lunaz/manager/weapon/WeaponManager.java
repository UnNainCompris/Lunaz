package fr.eris.lunaz.manager.weapon;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.manager.weapon.commands.WeaponExecutor;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.manager.weapon.listeners.DynamicListeners;
import fr.eris.lunaz.utils.NBTUtils;
import fr.eris.lunaz.utils.file.FileUtils;
import fr.eris.lunaz.utils.file.JsonUtils;
import fr.eris.manager.Manager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeaponManager extends Manager {

    private final HashMap<String, Weapon> cache = new HashMap<>();

    public void start() {
        saveWeapon(Weapon.defaultWeapon());
        new WeaponExecutor();
        new DynamicListeners();
    }

    public Weapon getWeapon(String systemName) {
        if(cache.containsKey(systemName)) return cache.get(systemName);
        Weapon weapon = JsonUtils.getData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "weapon"),
                systemName), Weapon.class);
        if(weapon != null) cache.put(systemName, weapon);
        return weapon;
    }

    public Weapon getWeapon(ItemStack item) {
        NBTItem nbtItem = NBTUtils.toNBT(item);
        if(nbtItem.hasNBTData())
            if(nbtItem.hasTag("WeaponSystemName"))
                return getWeapon(nbtItem.getString("WeaponSystemName"));
        return null;
    }

    public void saveWeapon(Weapon weapon) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "weapon"),
                          weapon.getSystemName()), weapon);
    }

    public boolean isAnWeapon(ItemStack item) {
        if(item == null || item.getType() == Material.AIR || item.getAmount() == 0) return false;
        NBTItem nbtItem = NBTUtils.toNBT(item);
        return nbtItem.hasNBTData() && nbtItem.hasTag("WeaponSystemName");
    }

    public List<Weapon> getAllWeapon(boolean addItToCache) {
        List<Weapon> weaponList = new ArrayList<>();
        for(File file : FileUtils.getAllFile(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "weapon"), ".json")) {
            Weapon newWeapon = getWeapon(file.getName().replace(".json", ""));
            if(newWeapon != null) {
                weaponList.add(newWeapon);
                if (addItToCache && !cache.containsKey(newWeapon.getSystemName()))
                    cache.put(newWeapon.getSystemName(), newWeapon);
            }
        }
        return weaponList;
    }
}
