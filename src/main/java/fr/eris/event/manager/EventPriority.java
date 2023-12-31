package fr.eris.event.manager;

import lombok.Getter;

public enum EventPriority {

    LOWEST(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4);

    @Getter private final int value;
    EventPriority(int value) {
        this.value = value;
    }
}
