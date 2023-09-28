package fr.eris.lunaz.manager.gang.data;

import com.google.gson.annotations.Expose;
import fr.eris.lunaz.LunaZ;
import fr.eris.lunaz.manager.gang.data.boost.GangBoost;
import fr.eris.lunaz.manager.gang.data.economy.GangBank;
import fr.eris.lunaz.manager.gang.data.level.GangLevel;
import fr.eris.lunaz.manager.gang.data.permission.GangPermissionManager;
import fr.eris.lunaz.manager.gang.data.upgrade.GangChestUpgrade;
import fr.eris.lunaz.manager.gang.data.upgrade.LuckUpgrade;
import fr.eris.lunaz.manager.gang.data.upgrade.MaxMemberUpgrade;
import fr.eris.lunaz.manager.gang.data.upgrade.MoneyUpgrade;
import fr.eris.lunaz.manager.gang.enums.GangChatType;
import fr.eris.lunaz.manager.gang.enums.GangRank;
import fr.eris.lunaz.manager.playerdata.data.PlayerData;
import fr.eris.lunaz.manager.playerdata.data.currency.CurrencyType;
import fr.eris.lunaz.manager.playerdata.data.gang.GangPlayer;
import fr.eris.lunaz.utils.ColorUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Gang {

    @Getter @Expose private UUID leader;
    @Getter @Expose private List<UUID> coleaders = new ArrayList<>();
    @Getter @Expose private List<UUID> mods = new ArrayList<>();
    @Getter @Expose private List<UUID> members = new ArrayList<>();
    @Getter @Expose private List<UUID> recruits = new ArrayList<>();
    @Getter @Expose private List<UUID> ally = new ArrayList<>();
    @Getter @Expose private List<UUID> enemy = new ArrayList<>();

    @Getter @Expose private final GangLevel level;
    @Getter @Expose private final GangBank gangLunaBank;
    @Getter @Expose private final GangBank gangMoneyBank;

    @Getter @Expose private final String gangName;
    @Getter @Expose private String gangDescription = "";
    @Expose private String gangDisplayName = "";
    @Getter @Expose private final UUID gangID;

    @Getter @Expose private List<GangBoost> moneyBoost = new ArrayList<>();
    @Getter @Expose private List<GangBoost> luckBoost = new ArrayList<>();

    @Getter @Expose private final MaxMemberUpgrade maxMemberUpgrade;
    @Getter @Expose private final GangChestUpgrade chestUpgrade;
    @Getter @Expose private final MoneyUpgrade moneyUpgrade;
    @Getter @Expose private final LuckUpgrade luckUpgrade;

    @Getter @Expose private final GangPermissionManager permissionManager;

    @Getter @Expose private List<UUID> receivedAllyRequest = new ArrayList<>();
    @Getter @Expose private List<UUID> sendedAllyRequest = new ArrayList<>();
    @Getter @Expose private List<UUID> receivedNeutralRequest = new ArrayList<>();
    @Getter @Expose private List<UUID> sendedNeutralRequest = new ArrayList<>();
    @Getter @Expose private List<UUID> sendedPlayerInviteRequest = new ArrayList<>();

    @Getter @Expose private boolean friendlyFire;

    public String getGangDisplayName() {
        return gangDisplayName.isEmpty() ? gangDisplayName : gangName;
    }

    public Gang(String gangName) {
        this.gangID = UUID.randomUUID();
        this.level = new GangLevel(this.gangID);
        this.gangMoneyBank = new GangBank(this.gangID);
        this.gangLunaBank = new GangBank(this.gangID);
        this.gangName = gangName;
        this.maxMemberUpgrade = new MaxMemberUpgrade();
        this.chestUpgrade = new GangChestUpgrade(this.gangID);
        this.moneyUpgrade = new MoneyUpgrade();
        this.luckUpgrade = new LuckUpgrade();
        this.permissionManager = new GangPermissionManager(this.gangID);
    }

    public int getMemberCount() {
        return 1 + coleaders.size() + mods.size() + members.size() + recruits.size();
    }

    public int getOnlineMemberCount() {
        int count = 0;
        for(UUID currentUuid : getAllMemberAsUuid()) {
            if (!Bukkit.getOfflinePlayer(currentUuid).isOnline()) continue;
            count++;
        }
        return count;
    }

    public List<GangPlayer> getAllMembers() {
        List<UUID> allMemberUuid = getAllMemberAsUuid();
        List<GangPlayer> gangPlayerList = new ArrayList<>();
        for(UUID currentUuid : allMemberUuid) {
            gangPlayerList.add(LunaZ.getPlayerDataManager().getPlayerdata(currentUuid).getGangPlayer());
        }
        return gangPlayerList;
    }

    public List<GangPlayer> getOnlineMembers() {
        List<UUID> allMemberUuid = getAllMemberAsUuid();
        List<GangPlayer> onlineMember = new ArrayList<>();
        for(UUID currentPlayerUUID : allMemberUuid) {
            if (!Bukkit.getOfflinePlayer(currentPlayerUUID).isOnline()) continue;
            onlineMember.add(LunaZ.getPlayerDataManager().getPlayerdata(currentPlayerUUID).getGangPlayer());
        }
        return onlineMember;
    }

    public List<Player> getOnlineMembersAsPlayer() {
        List<UUID> allMemberUuid = getAllMemberAsUuid();
        List<Player> onlineMember = new ArrayList<>();
        Player currentPlayer = null;
        for(UUID currentPlayerUUID : allMemberUuid) {
            if ((currentPlayer = Bukkit.getPlayer(currentPlayerUUID)) == null) continue;
            onlineMember.add(currentPlayer);
        }
        return onlineMember;
    }

    public List<GangPlayer> getOfflineMembers() {
        List<UUID> allMember = getAllMemberAsUuid();
        List<GangPlayer> offlineMember = new ArrayList<>();
        for(UUID currentPlayerUUID : allMember) {
            if (Bukkit.getOfflinePlayer(currentPlayerUUID).isOnline()) continue;
            offlineMember.add(LunaZ.getPlayerDataManager().getPlayerdata(currentPlayerUUID).getGangPlayer());
        }
        return offlineMember;
    }

    public int getOfflineMembersCount() {
        int count = 0;
        for(UUID currentUUID : getAllMemberAsUuid()) {
            if (Bukkit.getOfflinePlayer(currentUUID).isOnline()) continue;
            count++;
        }
        return count;
    }

    public List<UUID> getAllMemberAsUuid() {
        List<UUID> allUuid = new ArrayList<>();
        allUuid.add(leader);
        allUuid.addAll(coleaders);
        allUuid.addAll(mods);
        allUuid.addAll(members);
        allUuid.addAll(recruits);
        return allUuid;
    }

    public boolean equals(Object other) {
        if(!(other instanceof Gang)) return false;
        return ((Gang)other).getGangID().equals(this.getGangID());
    }

    public boolean haveAlly() {
        return !ally.isEmpty();
    }

    public boolean haveEnemy() {
        return !enemy.isEmpty();
    }

    public boolean haveDescription() {
        return gangDescription != null && !gangDescription.isEmpty();
    }

    public void kickPlayer(Player target) {
        kickPlayer(target.getUniqueId());
    }

    public void kickPlayer(UUID targetUUID) {
        coleaders.remove(targetUUID);
        mods.remove(targetUUID);
        members.remove(targetUUID);
        recruits.remove(targetUUID);

        GangPlayer gangTarget = LunaZ.getPlayerDataManager().getPlayerdata(targetUUID).getGangPlayer();
        gangTarget.setGangRank(GangRank.NO_GANG);
        gangTarget.setGangPlayerDisplayName("");
        gangTarget.setCurrentChat(GangChatType.GLOBAL);
        gangTarget.setGangID(null);
    }

    public void removePlayerFromList(UUID playerID, GangRank gangRank) {
        if(gangRank == GangRank.LEADER) leader = null;
        else if(gangRank == GangRank.CO_LEADER) coleaders.remove(playerID);
        else if(gangRank == GangRank.MOD) mods.remove(playerID);
        else if(gangRank == GangRank.MEMBER) members.remove(playerID);
        else if(gangRank == GangRank.RECRUIT) recruits.remove(playerID);
    }

    public void addPlayerToList(UUID playerID, GangRank rank) {
        if(rank == GangRank.LEADER) leader = playerID;
        else if(rank == GangRank.CO_LEADER) {
            if(!coleaders.contains(playerID)) coleaders.add(playerID);
        }
        else if(rank == GangRank.MOD) {
            if(!mods.contains(playerID)) mods.add(playerID);
        }
        else if(rank == GangRank.MEMBER) {
            if(!members.contains(playerID)) members.add(playerID);
        }
        else if(rank == GangRank.RECRUIT) {
            if(!recruits.contains(playerID)) recruits.add(playerID);
        }
    }

    public void sendAllyChatMessage(Player player, String message) {
        if(canSendColoredMessage(player)) message = ColorUtils.translate(message);
        GangPlayer sender = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        message = ColorUtils.translate("&5&l[&dAlly&5&l] &7&l- " + sender.getGangPlayerDisplayName() + " &7>> ") + message;

        for(UUID allyGang : ally) {
            Gang currentGang = LunaZ.getGangManager().getGang(allyGang);
            for(Player onlinePlayer : currentGang.getOnlineMembersAsPlayer()) {
                onlinePlayer.sendMessage(message);
            }
        }

        for(Player onlinePlayer : this.getOnlineMembersAsPlayer()) {
            onlinePlayer.sendMessage(message);
        }
    }

    public void sendGangChatMessage(Player player, String message) {
        if(canSendColoredMessage(player)) message = ColorUtils.translate(message);
        GangPlayer sender = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId()).getGangPlayer();
        String senderDisplayName = sender.getGangPlayerDisplayName();
        if(senderDisplayName.isEmpty()) senderDisplayName = player.getName();
        message = ColorUtils.translate("&6&l[&eGang&6&l] &7&l- " + senderDisplayName + " &7>> ") + message;

        for(Player onlinePlayer : this.getOnlineMembersAsPlayer()) {
            onlinePlayer.sendMessage(message);
        }
    }

    public void sendGangChatMessage(String message, boolean withSystemTag) {
        message = ColorUtils.translate("&6&l[&eGang&6&l] &7&l- " + (withSystemTag ? "&c[SYSTEM] &7>> " : "") + message);
        for(Player onlinePlayer : this.getOnlineMembersAsPlayer()) {
            onlinePlayer.sendMessage(message);
        }
    }

    public boolean canSendColoredMessage(Player player) {
        return player == null || player.hasPermission("lunaz.gang.chat.colored");
    }

    public void renameGang(String newName, boolean silent) {
        this.gangDisplayName = newName;
        if(!silent) {
            sendGangChatMessage("&7The gang was rename into " + ColorUtils.translate(newName) + " !", true);
        }
    }

    public void renameGangPlayer(Player target, String newName, boolean silent) {
        LunaZ.getPlayerDataManager().getPlayerdata(target.getUniqueId()).getGangPlayer().setGangPlayerDisplayName(newName);
        if(!silent) {
            sendGangChatMessage("&7The player " + target.getName() + " was rename into " +
                    ColorUtils.translate(newName) + " !", true);
        }
    }

    public void makeJoin(Player player, boolean silent) {
        this.recruits.add(player.getUniqueId());
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        playerData.getGangPlayer().setGangID(gangID);
        playerData.getGangPlayer().setGangRank(GangRank.RECRUIT);
        playerData.getGangPlayer().setGangPlayerDisplayName("");
        if(!silent) sendGangChatMessage("&7" + player.getName() + " &7join the gang!", false);
    }

    public void toggleFriendlyFire(boolean status, boolean silent) {
        if(status == this.friendlyFire) return;
        this.friendlyFire = status;
        if(!silent) {
            sendGangChatMessage("&7The friendly fire was " + (status ? "&aenable" : "&cdisable") + " &7!", true);
        }
    }

    public void disbandGang(boolean silent) {
        if(!silent) {
            sendGangChatMessage("&c&lYour gang was disband !", false);
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ColorUtils.translate("&6&l[&eGang&6&l] &c[✗] &7The gang " +
                        this.getGangDisplayName() + " was disbanded !"));
            }
        }
        for(UUID member : getAllMemberAsUuid()) {
            Player currentMember = Bukkit.getPlayer(member);
            kickPlayer(member);
        }

        LunaZ.getGangManager().deleteGang(gangID);
    }

    public boolean deposit(Player player, long amount, CurrencyType currencyType) {
        PlayerData playerData = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId());
        if(amount < 0) return false;
        if(currencyType == CurrencyType.LUNA) {
            if(playerData.getLuna().has(amount)) {
                playerData.getLuna().remove(amount, false);
                gangLunaBank.add(amount);
                return true;
            }
        } else if(currencyType == CurrencyType.MONEY) {
            if(playerData.getMoney().has(amount)) {
                playerData.getMoney().remove(amount, false);
                gangMoneyBank.add(amount);
                return true;
            }
        }
        return false;
    }

    public boolean withdraw(Player player, long amount, CurrencyType currencyType) {
        PlayerData playerData = LunaZ.getPlayerDataManager().getPlayerdata(player.getUniqueId());
        if(amount < 0) return false;
        if(currencyType == CurrencyType.LUNA) {
            if(gangLunaBank.has(amount)) {
                playerData.getLuna().add(amount);
                gangLunaBank.remove(amount, false);
                return true;
            }
        } else if(currencyType == CurrencyType.MONEY) {
            if(gangMoneyBank.has(amount)) {
                playerData.getMoney().add(amount);
                gangMoneyBank.remove(amount, false);
                return true;
            }
        }
        return false;
    }
}
