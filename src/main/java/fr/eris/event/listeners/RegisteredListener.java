package fr.eris.event.listeners;

import fr.eris.event.events.Cancellable;
import fr.eris.event.events.Event;
import fr.eris.event.manager.EventExecutor;
import fr.eris.event.manager.EventPriority;
import lombok.Getter;

public class RegisteredListener {

    private final @Getter EventPriority eventPriority;
    private final @Getter boolean ignoreCancellable;
    private final @Getter EventExecutor eventExecutor;
    private final @Getter Listener listener;

    public RegisteredListener(EventPriority priority, boolean ignoreCancelled, EventExecutor eventExecutor, Listener listener) {
        this.eventPriority = priority;
        this.ignoreCancellable = ignoreCancelled;
        this.eventExecutor = eventExecutor;
        this.listener = listener;
    }

    public void execute(Event event) {
        if (event instanceof Cancellable)
            if (((Cancellable) event).isCancelled() && !isIgnoreCancellable())
                return;
        eventExecutor.execute(listener, event);
    }
}
