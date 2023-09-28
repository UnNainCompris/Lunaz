package fr.eris.lunaz.manager.bullet;

import fr.eris.lunaz.manager.bullet.commands.BulletExecutor;
import fr.eris.lunaz.manager.bullet.data.Bullet;
import fr.eris.lunaz.utils.file.FileUtils;
import fr.eris.lunaz.utils.file.JsonUtils;
import fr.eris.manager.Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BulletManager extends Manager {
    private final HashMap<String, Bullet> cache = new HashMap<>();

    public void start() {
        saveBullet(Bullet.defaultBullet());
        new BulletExecutor();
    }

    public Bullet getBullet(String systemName) {
        if(cache.containsKey(systemName)) return cache.get(systemName);
        Bullet bullet = JsonUtils.getData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "bullet"),
                systemName), Bullet.class);
        if(bullet != null) cache.put(systemName, bullet);
        return bullet;
    }

    public void saveBullet(Bullet bullet) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "bullet"),
                      bullet.getSystemName()), bullet);
    }

    public List<Bullet> getAllBullet(boolean addItToCache) {
        List<Bullet> bulletList = new ArrayList<>();
        for(File file : FileUtils.getAllFile(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "bullet"), ".json")) {
            Bullet newBullet = getBullet(file.getName().replace(".json", ""));
            if(newBullet != null) {
                bulletList.add(newBullet);
                if (addItToCache && !cache.containsKey(newBullet.getSystemName()))
                    cache.put(newBullet.getSystemName(), newBullet);
            }
        }
        return bulletList;
    }
}
