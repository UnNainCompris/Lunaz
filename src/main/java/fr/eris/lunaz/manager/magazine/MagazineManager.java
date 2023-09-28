package fr.eris.lunaz.manager.magazine;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.manager.bullet.commands.BulletExecutor;
import fr.eris.lunaz.manager.bullet.data.Bullet;
import fr.eris.lunaz.manager.magazine.commands.MagazineExecutor;
import fr.eris.lunaz.manager.magazine.data.Magazine;
import fr.eris.lunaz.manager.magazine.listeners.DynamicListeners;
import fr.eris.lunaz.manager.weapon.data.Weapon;
import fr.eris.lunaz.utils.NBTUtils;
import fr.eris.lunaz.utils.file.FileUtils;
import fr.eris.lunaz.utils.file.JsonUtils;
import fr.eris.manager.Manager;
import fr.eris.manager.ManagerEnabler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MagazineManager extends Manager {
    private final HashMap<String, Magazine> cache = new HashMap<>();

    public void start() {
        ManagerEnabler.init(this);
        saveMagazine(Magazine.defaultMagazine());
        new MagazineExecutor();
        new DynamicListeners();
    }

    public void stop() {
        ManagerEnabler.stop(this);
    }

    public Magazine getMagazine(String systemName) {
        if(cache.containsKey(systemName)) return cache.get(systemName);
        Magazine magazine = JsonUtils.getData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "magazine"),
                systemName), Magazine.class);
        if(magazine != null) cache.put(systemName, magazine);
        return magazine;
    }

    public Magazine getMagazine(ItemStack item) {
        NBTItem nbtItem = NBTUtils.toNBT(item);
        if(nbtItem.hasNBTData())
            if(nbtItem.hasTag("MagazineSystemName"))
                return getMagazine(nbtItem.getString("MagazineSystemName"));
        return null;
    }

    public void saveMagazine(Magazine magazine) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "magazine"),
                magazine.getSystemName()), magazine);
    }

    public List<Magazine> getAllMagazine(boolean addItToCache) {
        List<Magazine> magazineList = new ArrayList<>();
        for(File file : FileUtils.getAllFile(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "magazine"), ".json")) {
            Magazine newMagazine = getMagazine(file.getName().replace(".json", ""));
            if(newMagazine != null) {
                magazineList.add(newMagazine);
                if (addItToCache && !cache.containsKey(newMagazine.getSystemName()))
                    cache.put(newMagazine.getSystemName(), newMagazine);
            }
        }
        return magazineList;
    }

    public boolean isAnMagazine(ItemStack item) {
        if(item == null || item.getAmount() == 0 || item.getType() == Material.AIR) return false;
        NBTItem nbtItem = NBTUtils.toNBT(item);
        return nbtItem.hasNBTData() && nbtItem.hasTag("MagazineSystemName");
    }
}
