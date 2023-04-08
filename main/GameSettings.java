package main;

import customComponents.ScoreTable;
import customComponents.GameTimer;

public class GameSettings {

    public int numberOfPlayers = 1;
    public int FBS = 4;
    public ScoreTable scoreTable = new ScoreTable();
    public boolean adventure = false;
    public boolean npc = false;
    public int mapIndex = 0;
    public long timeElapsed = 0;
    public GameTimer timer = new GameTimer();

    public void reset() {

        numberOfPlayers = 1;
        FBS = 4;
        scoreTable = new ScoreTable();
        adventure = false;
        npc = false;
        mapIndex = 0;
        timeElapsed = 0;
        timer.reset();

    }


}
