package fr.eris.lunaz.event.player;

import fr.eris.event.events.Event;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import lombok.Getter;

public class PlayerDataEvent implements Event {
    @Getter private final PlayerData playerData;

    public PlayerDataEvent(PlayerData playerData) {
        this.playerData = playerData;
    }
}
