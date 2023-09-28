package fr.eris.lunaz.manager.playerdata.data.control;

import com.google.gson.annotations.Expose;
import lombok.Getter;

public class PlayerControl {

    @Expose @Getter private Key weaponShootingKey = new Key(Key.ActionKey.RIGHT_CLICK);
    @Expose @Getter private Key weaponZoomKey = new Key(Key.ActionKey.LEFT_CLICK);
    @Expose @Getter private Key weaponReloadKey = new Key(Key.ActionKey.DROP);
}
