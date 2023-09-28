package fr.eris.lunaz.manager.armor;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.manager.armor.commands.ArmorExecutor;
import fr.eris.lunaz.manager.armor.data.Armor;
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

public class ArmorManager extends Manager {
    private final HashMap<String, Armor> cache = new HashMap<>();

    public void start() {
        saveArmor(Armor.defaultArmor());
        new ArmorExecutor();
        //new DynamicListeners();
    }

    public Armor getArmor(String systemName) {
        if(cache.containsKey(systemName)) return cache.get(systemName);
        Armor armor = JsonUtils.getData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "armor"),
                systemName), Armor.class);
        if(armor != null) cache.put(systemName, armor);
        return armor;
    }

    public Armor getArmor(ItemStack item) {
        NBTItem nbtItem = NBTUtils.toNBT(item);
        if(nbtItem.hasNBTData())
            if(nbtItem.hasTag("ArmorSystemName"))
                return getArmor(nbtItem.getString("ArmorSystemName"));
        return null;
    }

    public void saveArmor(Armor armor) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "armor"),
                armor.getSystemName()), armor);
    }

    public boolean isAnArmor(ItemStack item) {
        if(item == null || item.getAmount() == 0 || item.getType() == Material.AIR) return false;
        NBTItem nbtItem = NBTUtils.toNBT(item);
        return nbtItem.hasNBTData() && nbtItem.hasTag("ArmorSystemName");
    }

    public List<Armor> getAllArmor(boolean addItToCache) {
        List<Armor> armorList = new ArrayList<>();
        for(File file : FileUtils.getAllFile(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "armor"), ".json")) {
            Armor newArmor = getArmor(file.getName().replace(".json", ""));
            if(newArmor != null) {
                armorList.add(newArmor);
                if (addItToCache && !cache.containsKey(newArmor.getSystemName()))
                    cache.put(newArmor.getSystemName(), newArmor);
            }
        }
        return armorList;
    }
}
