package fr.eris.lunaz.manager.playerdata.data;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

public class PlayerStatus {
    @Expose @Getter @Setter private boolean reloadingWeapon = false;
    @Expose @Getter @Setter private boolean weaponZooming = false;
    @Expose @Getter @Setter private boolean reloadingMagazine = false;
    @Expose @Getter @Setter private boolean consumingConsumable = false;
}
