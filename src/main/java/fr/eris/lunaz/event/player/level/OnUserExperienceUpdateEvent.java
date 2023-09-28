package fr.eris.lunaz.event.player.level;

import fr.eris.event.events.Cancellable;
import fr.eris.lunaz.event.player.PlayerDataEvent;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import lombok.Getter;
import lombok.Setter;

public class OnUserExperienceUpdateEvent extends PlayerDataEvent implements Cancellable {
    @Getter @Setter private boolean cancelled;

    @Getter @Setter private long newExperience;
    @Getter private final long oldExperience;
    public OnUserExperienceUpdateEvent(PlayerData player, long oldExperience, long newExperience) {
        super(player);
        this.newExperience = newExperience;
        this.oldExperience = oldExperience;
    }
}
