package fr.eris.lunaz.manager.playerdata.data;

import fr.eris.lunaz.manager.tag.data.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class PlayerTag {
    @Getter @Setter private String currentTag;
    @Getter private final List<String> ownedTag = new ArrayList<>();
}
