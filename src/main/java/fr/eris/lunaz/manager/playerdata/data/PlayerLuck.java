package fr.eris.lunaz.manager.playerdata.data;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnegative;

public class PlayerLuck {
    @Expose @Getter @Setter private double armorLuck = 1; // boost the value
    @Expose @Getter @Setter private double meleeLuck = 1; // boost the value
    @Expose @Getter @Setter private double itemLuck = 1; // boost the value
}
