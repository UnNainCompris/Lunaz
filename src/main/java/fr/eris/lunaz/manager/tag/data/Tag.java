package fr.eris.lunaz.manager.tag.data;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

public class Tag {
    @Getter @Expose private final String systemName;
    @Getter @Expose private final String displayName;
    @Getter @Expose private final Material displayMaterial = Material.PAPER;
    @Getter @Expose private final boolean displaySystemName = true;

    public Tag(String systemName, String displayName) {
        this.systemName = systemName;
        this.displayName = displayName;
    }
}
