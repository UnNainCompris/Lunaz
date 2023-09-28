package fr.eris.lunaz.manager.melee;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.manager.melee.commands.MeleeExecutor;
import fr.eris.lunaz.manager.melee.data.Melee;
import fr.eris.lunaz.manager.melee.listeners.DynamicListeners;
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

public class MeleeManager extends Manager {
    private final HashMap<String, Melee> cache = new HashMap<>();

    public void start() {
        saveMelee(Melee.defaultMelee());
        new MeleeExecutor();
        new DynamicListeners();
    }

    public Melee getMelee(String systemName) {
        if(cache.containsKey(systemName)) return cache.get(systemName);
        Melee melee = JsonUtils.getData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "melee"),
                systemName), Melee.class);
        if(melee != null) cache.put(systemName, melee);
        return melee;
    }

    public Melee getMelee(ItemStack item) {
        NBTItem nbtItem = NBTUtils.toNBT(item);
        if(nbtItem.hasNBTData())
            if(nbtItem.hasTag("MeleeSystemName"))
                return getMelee(nbtItem.getString("MeleeSystemName"));
        return null;
    }

    public void saveMelee(Melee melee) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "melee"),
                melee.getSystemName()), melee);
    }

    public boolean isAnMelee(ItemStack item) {
        if(item == null || item.getAmount() == 0 || item.getType() == Material.AIR) return false;
        NBTItem nbtItem = NBTUtils.toNBT(item);
        return nbtItem.hasNBTData() && nbtItem.hasTag("MeleeSystemName");
    }

    public List<Melee> getAllMelee(boolean addItToCache) {
        List<Melee> meleeList = new ArrayList<>();
        for(File file : FileUtils.getAllFile(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "melee"), ".json")) {
            Melee newMelee = getMelee(file.getName().replace(".json", ""));
            if(newMelee != null) {
                meleeList.add(newMelee);
                if (addItToCache && !cache.containsKey(newMelee.getSystemName()))
                    cache.put(newMelee.getSystemName(), newMelee);
            }
        }
        return meleeList;
    }
}
