package fr.eris.lunaz.manager.scoreboard.scoreboard;

import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.district.data.District;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserScoreboard extends Scoreboard { // TODO: 21/08/2023 Make a something to let player have a custom scoreboard
    public UserScoreboard(Player owner) {
        super(owner, 30);
    }

    protected void updateContent() {
        setupScoreboard();
    }

    private void setupScoreboard() {
        scoreboardContent.clear();
        PlayerData playerData = LunaZ.getPlayerDataManager().getPlayerdata(getOwner().getUniqueId());
        setTitle((args) -> "&cLuna&4&lZ &7(&o" + LunaZ.getConfiguration().version() + "&7)");

        addContent((args) -> "", "BlankLine1");
        addContent((args) -> owner.getDisplayName(), "OwnerDisplayName");
        addContent((args) -> "&8| &7Luna : &6" + playerData.getLuna().getValue(), "PlayerLuna");
        addContent((args) -> "&8| &7Money : &e" + playerData.getMoney().getValue(), "PlayerMoney");
        //Clan clan = SimpleClans.getInstance().getClanManager().getClanByPlayerUniqueId(player.getUniqueId());
        //if(clan != null)
        //    scoreboardContent.add("&8| &7Gang: " + clan.getTag() + " &7[&6"+ clan.getOnlineMembers().size() + "&7/&6" + clan.getSize() + "&7]");
        addContent((args) -> " ", "BlankLine2");
        addContent((args) -> "&7Information:", "Information");
        addContent((args) -> "&8| &7Kills : &c" + playerData.getStatistics().getPlayerKills(), "InformationKills");
        addContent((args) -> "&8| &7Zombie Kills : &c" + playerData.getStatistics().getZombieKills(), "InformationZombieKills");
        addContent((args) -> "&8| &7Death : &c" + playerData.getStatistics().getPlayerDeath(), "InformationDeath");
        District district = LunaZ.getDisctrictManager().getDistrictFromLocation(owner.getLocation());
        if(district != null) {
            addContent((args) -> "  ", "BlankLine3");
            addContent((args) -> district.getScoreboardDisplayName(), "CurrentDistrict");
            addContent((args) -> "&8| &7" + district.getPlayerInDistrictDisplay() + "Player(s)", "PlayerInDistrict");
        }
        if(playerData.getStats().getRadioactivity() != 0)
            addContent((args) -> playerData.getStats().getRadioactivity() + "%", "PlayerRadioactivity");
    }
}
