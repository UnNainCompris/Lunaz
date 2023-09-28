package fr.eris.lunaz.manager.chat;

import fr.eris.lunaz.manager.chat.listeners.OnChatMessage;
import fr.eris.manager.Manager;

public class ChatManager extends Manager {
    public void start() {
        new OnChatMessage();
    }
}
