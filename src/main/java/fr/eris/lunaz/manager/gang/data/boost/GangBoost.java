package fr.eris.lunaz.manager.gang.data.boost;

import com.google.gson.annotations.Expose;
import lombok.Getter;

import java.util.UUID;

public class GangBoost {
    @Expose @Getter private double boostValue;
    @Expose @Getter private long boostDuration; // in second
    @Expose @Getter private long boostStart; // in second
    @Expose @Getter private UUID boosterOwner;
}
