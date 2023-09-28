package fr.eris.lunaz.manager.scoreboard.scoreboard;

import fr.eris.lunaz.utils.ColorUtils;
import fr.eris.lunaz.utils.GetValue;
import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Scoreboard {
    @Getter protected final Player owner;
    @Getter protected final int visibleTime; // in second
    @Getter @Setter protected int currentDisplayTime; // in second
    protected final FastBoard scoreboard;

    protected GetValue<String> title;
    protected final HashMap<Integer, GetValue<String>> scoreboardContent;
    protected final HashMap<String, Integer> scoreboardLineIdentifier;

    public Scoreboard(Player owner, int visibleTime) {
        this.owner = owner;
        this.scoreboard = new FastBoard(owner);
        this.visibleTime = visibleTime;
        this.scoreboardContent = new HashMap<>();
        this.scoreboardLineIdentifier = new HashMap<>();
    }

    public final void update() {
        currentDisplayTime++;
        updateContent();
        updateScoreboard();
    }

    private void updateScoreboard() {
        List<String> scoreboardContentAsString = new ArrayList<>();
        for(int i = 1 ; i <= 15 ; i++) {
            if(scoreboardContent.get(i) == null) continue;
            scoreboardContentAsString.add(scoreboardContent.get(i).getValue());
        }
        scoreboard.updateLines(ColorUtils.translate(scoreboardContentAsString));
        scoreboard.updateTitle(ColorUtils.translate(title.getValue()));
    }

    protected abstract void updateContent();

    public final void editContent(int line, GetValue<String> value, String identifier) {
        if(line <= 0 || line > 15) return;
        int realLine = foundEmptyLine(line);
        scoreboardContent.put(realLine, value);
        scoreboardLineIdentifier.put(identifier, realLine);
    }

    private int foundEmptyLine(int startLine) {
        if(scoreboardContent.get(startLine) != null) return startLine;
        int currentLine = startLine;
        for(Integer line : scoreboardContent.keySet()) {
            if(line < currentLine) currentLine = line;
        }
        return currentLine;
    }

    public final void addContent(GetValue<String> value, String identifier) {
        for(int i = 1 ; i <= 15 ; i++) {
            if(scoreboardContent.containsKey(i)) continue;
            scoreboardContent.put(i, value);
            scoreboardLineIdentifier.put(identifier, i);
            break;
        }
    }

    public final void clearContent() {
        scoreboardContent.clear();
    }

    public final void setTitle(GetValue<String> title) {
        this.title = title;
    }

    public final void stop() {
        scoreboard.delete();
    }

    public int findLineByIdentifier(String identifier) {
        return scoreboardLineIdentifier.getOrDefault(identifier, -1);
    }
}
