package main;

import customComponents.ScoreTable;
import customComponents.GameTimer;

public class GameSettings {

    public int numberOfPlayers = 1;
    public int FBS = 5;
    public ScoreTable scoreTable = new ScoreTable();
    public boolean adventure = false;
    public boolean npc = false;
    public int mapIndex = 0;
    public long timeElapsed = 0;
    public GameTimer timer = new GameTimer();
    
}
