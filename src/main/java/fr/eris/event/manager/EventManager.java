package fr.eris.event.manager;


import fr.eris.event.events.Event;
import fr.eris.event.listeners.Listener;
import fr.eris.event.listeners.RegisteredListener;
import fr.eris.lunaz.utils.BukkitTasks;
import fr.eris.manager.Manager;

import java.lang.reflect.Method;
import java.util.*;

public class EventManager extends Manager {

    private final List<RegisteredListener> registeredListeners = new ArrayList<>();

    public EventManager() {}

    public void registerListener(Listener listener) {
        final Set<Method> methods = new HashSet<>(Arrays.asList(listener.getClass().getMethods()));
        for(Method method : methods) {
            final EventHandler eventHandlerAnnotation;
            if((eventHandlerAnnotation = method.getAnnotation(EventHandler.class)) == null) continue;
            if(!Event.class.isAssignableFrom(method.getParameterTypes()[0])) continue;
            final Class<?> eventClass = method.getParameterTypes()[0];
            method.setAccessible(true);

            final EventExecutor eventExecutor = (methodClass, event) -> {
                if(!eventClass.isAssignableFrom(event.getClass())) return;
                try {
                    method.invoke(methodClass, event);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            };
            this.registeredListeners.add(new RegisteredListener(eventHandlerAnnotation.priority(),
                    eventHandlerAnnotation.ignoreCancelled(), eventExecutor, listener));
        }
        sortPriority();
    }

    public void postEvent(Event event) {
        registeredListeners.forEach(registeredListener -> registeredListener.execute(event));
    }

    public void postEventSync(Event event) {
        BukkitTasks.sync(() -> registeredListeners.forEach(registeredListener -> registeredListener.execute(event)));
    }

    private void sortPriority() {
        registeredListeners.sort(Comparator.comparingInt(listeners -> listeners.getEventPriority().getValue()));
        Collections.reverse(registeredListeners);
    }

    public void unregisterListener(Listener listener) {
        final Set<RegisteredListener> registeredListenersCopy = new HashSet<>(registeredListeners);
        for(RegisteredListener registeredListener : registeredListenersCopy) {
            if(registeredListener.getListener().getClass().isAssignableFrom(listener.getClass()))
                registeredListeners.remove(registeredListener);
        }
    }

}
