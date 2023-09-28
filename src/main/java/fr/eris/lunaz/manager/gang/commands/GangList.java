package fr.eris.lunaz.manager.gang.commands;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.commands.SubCommand;
import fr.eris.lunaz.manager.commands.SubCommandExecutor;
import fr.eris.lunaz.manager.gang.data.Gang;
import fr.eris.lunaz.utils.MessageBuilder;
import fr.eris.lunaz.utils.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GangList extends SubCommand {
    public GangList() {
        super("list", "lunaz.gang.list", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        List<Gang> allOnlineGang = LunaZ.getGangManager().getAllOnlineGang(true);
        int maxPage = (int) Math.floor(allOnlineGang.size() / 5f);
        int page = getPage(args, maxPage);
        printGangListHeader(player, page, maxPage);
        printGangListPage(player, page, allOnlineGang);
        printGangListFooter(player, page, maxPage);
    }

    private void printGangListHeader(Player sender, int page, int maxPage) {
        MessageBuilder message = MessageBuilder.builder();
        if(page > 0) {
            message.addClickEvent("&6<< &7&o" + (page - 1) + " &6<< ",
                    ClickEvent.Action.RUN_COMMAND, "gang list " + (page - 1));
        }
        
        message.addText("&8========== &7Page &6&l" + page + "/" + maxPage + "&8==========");
        
        if(page < maxPage) {
            message.addClickEvent(" &6>> &7&o" + (page + 1) + " &6>>",
                    ClickEvent.Action.RUN_COMMAND, "gang list " + (page + 1));
        }
        message.sendMessage(sender);
    }

    private void printGangListPage(Player sender, int page, List<Gang> gangOnline) { // Do not condense this to change easier !
        List<Gang> gangAtPages = getGangAtPage(10, page, gangOnline);
        MessageBuilder message = MessageBuilder.builder();
        for(Gang gang : gangAtPages) {
            message.addClickEvent("&6>> &7" + gang.getGangDisplayName() +
                    " &8&o(" + gang.getOnlineMemberCount() + "/" + gang.getMemberCount() + ")",
                    ClickEvent.Action.RUN_COMMAND, "gang show " + gang.getGangName());
            message.sendMessage(sender);
            message.reset();
        }
    }

    private void printGangListFooter(Player sender, int page, int maxPage) { // Do not condense this to change easier !
        MessageBuilder message = MessageBuilder.builder();
        if(page > 0) {
            message.addClickEvent("&6<< &7&o" + (page - 1) + " &6<< ",
                    ClickEvent.Action.RUN_COMMAND, "gang list " + (page - 1));
        }

        message.addText("&8========== &7Page &6&l" + page + "/" + maxPage + "&8==========");

        if(page < maxPage) {
            message.addClickEvent(" &6>> &7&o" + (page + 1) + " &6>>",
                    ClickEvent.Action.RUN_COMMAND, "gang list " + (page + 1));
        }
        message.sendMessage(sender);
    }

    public int getPage(String[] commandArgs, int maxPage) {
        if(commandArgs == null || commandArgs[0] == null || !StringUtils.isInteger(commandArgs[0])) return 1;
        int page = Integer.parseInt(commandArgs[0]);
        if(page > maxPage) page = maxPage;
        return page;
    }

    public List<Gang> getGangAtPage(int amountPerPage, int page, List<Gang> defaultList) {
        int startIndex = amountPerPage * page;
        List<Gang> gangAtPages = new ArrayList<>();
        for(int currentIndex = startIndex ; currentIndex < startIndex + amountPerPage ; currentIndex++) {
            if(currentIndex >= defaultList.size()) return gangAtPages;
            gangAtPages.add(defaultList.get(currentIndex));
        }
        return gangAtPages;
    }
}
