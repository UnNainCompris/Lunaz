package fr.eris.event.events;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancel);
}
