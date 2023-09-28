package fr.eris.lunaz.manager.actionbar;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class PlayerMessage {
    @Getter private final Player player;
    private List<Message> messages = new ArrayList<>();

    public PlayerMessage(Player player) {
        this.player = player;
    }

    public void display() {
        if(messages.isEmpty()) return;
        if(messages.size() < 0) this.messages = new ArrayList<>();
        final Message message = messages.get(0);
        if(message.show(player)) {
            messages.remove(0);
        }
    }

    public Message getByReason(String reason) {
        if(messages.isEmpty()) return null;
        if(messages.size() < 0) this.messages = new ArrayList<>();
        for(Message message : messages)
            if(message.messageReason.equalsIgnoreCase(reason))
                return message;
        return null;
    }

    public void removeByReason(String reason) {
        Message toRemove = null;
        if(messages.isEmpty()) return;
        if(messages.size() < 0) this.messages = new ArrayList<>();
        for (Message message : messages)
            if (message.messageReason.equalsIgnoreCase(reason)) {
                toRemove = message;
                break;
            }
        if (toRemove == null) return;
        this.messages.remove(toRemove);
    }

    public void add(Message message) {
        if(messages.size() < 0) this.messages = new ArrayList<>();
        this.messages.add(message);
    }
    public void set(Message message) {
        if(messages.size() < 0) this.messages = new ArrayList<>();
        int index = this.messages.indexOf(getByReason(message.messageReason));
        this.messages.set(index, message);
    }
    public void addPriority(Message message) {
        if(messages.size() < 0) this.messages = new ArrayList<>();
        this.messages.add(0, message);
    }
    public void setPriority(Message message) {
        if(messages.size() < 0) this.messages = new ArrayList<>();
        this.messages.remove(getByReason(message.messageReason));
        this.messages.add(0, message);
    }
}
