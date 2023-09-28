package fr.eris.lunaz.manager.playerdata.data.control;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class Key {

    @Expose @NonNull @Getter @Setter private ActionKey actionKey;
    @Expose @Getter @Setter private ConditionalKey conditionalKey;

    public Key(@NonNull ActionKey actionKey) {
        this.actionKey = actionKey;
    }

    public enum ActionKey {
        MIDDLE_CLICK,
        RIGHT_CLICK,
        LEFT_CLICK,
        OFF_HAND,
        DROP;
    }

    public enum ConditionalKey {
        SNEAKING
    }
}
