package fr.eris.lunaz.manager.consumable;

import de.tr7zw.nbtapi.NBTItem;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.consumable.commands.ConsumableExecutor;
import fr.eris.lunaz.manager.consumable.data.Consumable;
import fr.eris.lunaz.manager.consumable.listeners.DynamicListeners;
import fr.eris.lunaz.utils.NBTUtils;
import fr.eris.lunaz.utils.Tuple;
import fr.eris.lunaz.utils.file.FileUtils;
import fr.eris.lunaz.utils.file.JsonUtils;
import fr.eris.manager.Manager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConsumableManager extends Manager {
    private final HashMap<String, Consumable> cache = new HashMap<>();
    private final HashMap<Player, List<Tuple<Consumable, Long>>> consumeCooldown = new HashMap<>();

    public void start() {
        saveConsumable(Consumable.defaultConsumable());
        new ConsumableExecutor();
        new DynamicListeners();
    }

    public Consumable getConsumable(String systemName) {
        if(cache.containsKey(systemName)) return cache.get(systemName);
        Consumable consumable = JsonUtils.getData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "consumable"),
                systemName), Consumable.class);
        if(consumable != null) cache.put(systemName, consumable);
        return consumable;
    }

    public Consumable getConsumable(ItemStack item) {
        NBTItem nbtItem = NBTUtils.toNBT(item);
        if(nbtItem.hasNBTData())
            if(nbtItem.hasTag("ConsumableSystemName"))
                return getConsumable(nbtItem.getString("ConsumableSystemName"));
        return null;
    }

    public void saveConsumable(Consumable consumable) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "consumable"),
                consumable.getSystemName()), consumable);
    }

    public boolean isAnConsumable(ItemStack item) {
        if(item == null || item.getAmount() == 0 || item.getType() == Material.AIR) return false;
        NBTItem nbtItem = NBTUtils.toNBT(item);
        return nbtItem.hasNBTData() && nbtItem.hasTag("ConsumableSystemName");
    }

    public List<Consumable> getAllConsumable(boolean addItToCache) {
        List<Consumable> consumableList = new ArrayList<>();
        for(File file : FileUtils.getAllFile(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "consumable"), ".json")) {
            Consumable newConsumable = getConsumable(file.getName().replace(".json", ""));
            if(newConsumable != null) {
                consumableList.add(newConsumable);
                if (addItToCache && !cache.containsKey(newConsumable.getSystemName()))
                    cache.put(newConsumable.getSystemName(), newConsumable);
            }
        }
        return consumableList;
    }

    public boolean canConsume(Player consumer, Consumable consumable) {
        if(!consumeCooldown.containsKey(consumer)) return true;
        if(LunaZ.getPlayerDataManager().getPlayerdata(consumer.getUniqueId()).getStatus().isConsumingConsumable()) return false;
        for(Tuple<Consumable, Long> lastConsumableConsume : consumeCooldown.get(consumer)) {
            Consumable currentConsumable = lastConsumableConsume.getFirst();
            if(!currentConsumable.getSystemName().equals(consumable.getSystemName())) continue;
            long lastConsumeMillis = lastConsumableConsume.getSecond();
            if(currentConsumable.getConsumeCooldownMillis() > System.currentTimeMillis() - lastConsumeMillis) return false;
        }
        return true;
    }

    public void putPlayerInCooldown(Player consumer, Consumable consumable) {
        if(!consumeCooldown.containsKey(consumer)) consumeCooldown.put(consumer, new ArrayList<>());
        consumeCooldown.get(consumer).add(new Tuple<>(consumable, System.currentTimeMillis()));
    }
}
