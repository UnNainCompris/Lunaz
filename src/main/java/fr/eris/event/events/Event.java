package fr.eris.event.events;

public interface Event {
    default String getName() {
        return this.getClass().getSimpleName();
    }
}