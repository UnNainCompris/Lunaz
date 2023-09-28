package fr.eris.lunaz.manager.playerdata.data;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

public class PlayerStats {
    @Expose @Getter @Setter private double speed = 0;
    @Expose @Getter @Setter private double defence = 1;
    @Expose @Getter @Setter private double health = 20;
    @Expose @Getter @Setter private double damage = 1;

    @Expose @Getter @Setter private double radioactivity = 0; // max = 100.00
}
