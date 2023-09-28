package fr.eris.lunaz.event.player;

import fr.eris.event.events.Event;
import lombok.Getter;
import org.bukkit.entity.Player;

public class PlayerEvent implements Event {
    @Getter
    private final Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }
}
