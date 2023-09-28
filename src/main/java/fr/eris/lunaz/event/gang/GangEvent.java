package fr.eris.lunaz.event.gang;

import fr.eris.event.events.Event;
import fr.eris.lunaz.manager.gang.data.Gang;
import lombok.Getter;
import org.bukkit.entity.Player;

public class GangEvent implements Event {
    @Getter private final Gang gang;

    public GangEvent(Gang gang) {
        this.gang = gang;
    }
}
