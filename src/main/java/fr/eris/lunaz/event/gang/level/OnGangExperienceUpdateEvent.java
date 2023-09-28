package fr.eris.lunaz.event.gang.level;

import fr.eris.event.events.Cancellable;
import fr.eris.lunaz.event.gang.GangEvent;
import fr.eris.lunaz.event.player.PlayerDataEvent;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import lombok.Getter;
import lombok.Setter;

public class OnGangExperienceUpdateEvent extends GangEvent implements Cancellable {
    @Getter @Setter private boolean cancelled;

    @Getter @Setter private long newExperience;
    @Getter private final long oldExperience;
    public OnGangExperienceUpdateEvent(Gang gang, long oldExperience, long newExperience) {
        super(gang);
        this.newExperience = newExperience;
        this.oldExperience = oldExperience;
    }
}
