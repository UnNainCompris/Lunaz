package fr.eris.lunaz.manager.playerdata.data;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

public class PlayerSettings {
    @Expose @Getter @Setter private boolean allowPing = true; // TODO: 22/09/2023  
}
