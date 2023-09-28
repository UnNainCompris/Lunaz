package fr.eris.lunaz.utils;

import fr.eris.lunaz.manager.actionbar.Message;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class MessageBuilder {

    private ComponentBuilder builder;
    //message.append(new ComponentBuilder("Text 1").event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "UwU")))
    //message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "ch join" + channel.getName()));

    private MessageBuilder() {
        builder = new ComponentBuilder("");
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public MessageBuilder addClickEvent(String messageToAdd, ClickEvent.Action clickAction, String clickActionValue) {
        TextComponent newText = new TextComponent(ColorUtils.translate(messageToAdd));
        newText.setClickEvent(new ClickEvent(clickAction, clickActionValue));
        builder.append(newText);
        return this;
    }

    public MessageBuilder addHoverEvent(String messageToAdd, HoverEvent.Action hoverAction, List<Content> hoverActionValue) {
        TextComponent newText = new TextComponent(ColorUtils.translate(messageToAdd));
        newText.setHoverEvent(new HoverEvent(hoverAction, hoverActionValue));
        builder.append(newText);
        return this;
    }

    public MessageBuilder addClickAndHoverEvent(String messageToAdd, ClickEvent.Action clickAction, String clickActionValue,
                                                HoverEvent.Action hoverAction, List<Content> hoverActionValue) {
        TextComponent newText = new TextComponent(ColorUtils.translate(messageToAdd));
        newText.setHoverEvent(new HoverEvent(hoverAction, hoverActionValue));
        newText.setClickEvent(new ClickEvent(clickAction, clickActionValue));
        builder.append(newText);
        return this;
    }

    public MessageBuilder addText(String messageToAdd) {
        TextComponent newText = new TextComponent(ColorUtils.translate(messageToAdd));
        builder.append(newText);
        return this;
    }

    public MessageBuilder sendMessage(Player player) {
        player.spigot().sendMessage(builder.create());
        return this;
    }

    public MessageBuilder sendMessage(Collection<Player> players) {
        for(Player player : players) player.spigot().sendMessage(builder.create());
        return this;
    }

    public MessageBuilder reset() {
        this.builder = new ComponentBuilder("");
        return this;
    }

    public MessageBuilder reset(String defaultText) {
        this.builder = new ComponentBuilder(ColorUtils.translate(defaultText));
        return this;
    }

}
