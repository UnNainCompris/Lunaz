package fr.eris.event.manager;

import fr.eris.event.events.Event;
import fr.eris.event.listeners.Listener;

public interface EventExecutor {
    void execute(Listener listener, Event event);
}
