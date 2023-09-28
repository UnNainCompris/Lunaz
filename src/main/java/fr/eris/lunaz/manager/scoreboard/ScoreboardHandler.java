package fr.eris.lunaz.manager.scoreboard;

import fr.eris.lunaz.manager.scoreboard.scoreboard.Scoreboard;
import fr.eris.lunaz.manager.scoreboard.scoreboard.UserScoreboard;
import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardHandler {
    @Getter private final Player owner;
    private List<Scoreboard> scoreboardList;
    @Getter private Scoreboard currentScoreboard;
    private long secondSinceLastChange;

    public ScoreboardHandler(Player owner) {
        this.owner = owner;
        this.scoreboardList = new ArrayList<>();
        initScoreboardList();
    }

    private void initScoreboardList() {
        scoreboardList.add(new UserScoreboard(owner));
    }

    public void update() {
        handleScoreboardSwitch();
        currentScoreboard.update();
    }

    private void handleScoreboardSwitch() {
        secondSinceLastChange++;
        if(currentScoreboard == null || currentScoreboard.getVisibleTime() >= secondSinceLastChange) {
            secondSinceLastChange = 0;
            currentScoreboard = getNextScoreboard();
            currentScoreboard.setCurrentDisplayTime(0);
        }
    }

    public Scoreboard getNextScoreboard() {
        if(currentScoreboard == null) return scoreboardList.get(0);
        int currentIndex = scoreboardList.lastIndexOf(currentScoreboard);
        if(currentIndex == scoreboardList.size()-1) return scoreboardList.get(0);
        else return scoreboardList.get(currentIndex+1);
    }

    public void stop() {
        scoreboardList.forEach(Scoreboard::stop);
    }
}
