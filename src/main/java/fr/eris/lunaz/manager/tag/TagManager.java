package fr.eris.lunaz.manager.tag;

import fr.eris.lunaz.manager.tag.commands.TagExecutor;
import fr.eris.lunaz.manager.tag.data.Tag;
import fr.eris.lunaz.utils.file.FileUtils;
import fr.eris.lunaz.utils.file.JsonUtils;
import fr.eris.manager.Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TagManager extends Manager {
    private final HashMap<String, Tag> cache = new HashMap<>();
    public void start() {
        new TagExecutor();
    }

    public Tag getTag(String systemName) {
        if(cache.containsKey(systemName)) return cache.get(systemName);
        Tag tag = JsonUtils.getData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "tag"),
                systemName), Tag.class);
        if(tag != null) cache.put(systemName, tag);
        return tag;
    }

    public void saveTag(Tag tag) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "tag"),
                tag.getSystemName()), tag);
    }

    public List<Tag> getAllTag(boolean addItToCache) {
        List<Tag> tagList = new ArrayList<>();
        for(File file : FileUtils.getAllFile(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "tag"), ".json")) {
            Tag newTag = getTag(file.getName().replace(".json", ""));
            if(newTag != null) {
                tagList.add(newTag);
                if (addItToCache && !cache.containsKey(newTag.getSystemName()))
                    cache.put(newTag.getSystemName(), newTag);
            }
        }
        return tagList;
    }

    public void deleteTag(String systemName) {
        if(JsonUtils.isExist(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "tag"), systemName)) {
            JsonUtils.deleteJson(FileUtils.getOrCreateFile(FileUtils.ROOT_FOLDER, "tag"), systemName);
        }
    }
}
