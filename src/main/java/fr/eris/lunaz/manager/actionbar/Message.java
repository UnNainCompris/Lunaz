package fr.eris.lunaz.manager.actionbar;

import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.GetValue;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message {

    public final int showTime;
    public List<MessageUpdater> message = new ArrayList<>();
    public int showCounter = 0;
    public final String messageReason;
    private final HashMap<String, GetValue<Boolean>> conditionalView;

    public Message(int showTime, String messageReason, MessageUpdater message) {
        this.message.add(message); this.showTime = showTime; this.messageReason = messageReason;
        conditionalView = new HashMap<>();
    }

    public Message addConditionalView(String conditionReason, GetValue<Boolean> condition) {
        conditionalView.put(conditionReason, condition);
        return this;
    }

    public Message removeConditionalView(String conditionReason) {
        conditionalView.remove(conditionReason);
        return this;
    }

    public void add(MessageUpdater newString) {
        showCounter = 0;
        message.add(newString);
    }

    public boolean show(Player player) {
        StringBuilder toDisplay = new StringBuilder();
        for(MessageUpdater string : this.message)
            toDisplay.append(string.getMessage());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorUtils.translate(toDisplay.toString())));
        showCounter++;
        return showCounter >= showTime && isConditionMeetToSkip();
    }

    public boolean isConditionMeetToSkip() {
        if(conditionalView.isEmpty()) return true;
        for(String reason : conditionalView.keySet()) {
            if(!conditionalView.get(reason).getValue()) return false;
        }
        return true;
    }

    public interface MessageUpdater {
        String getMessage();
    }
}
