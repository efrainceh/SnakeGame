package game.game;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

import customComponents.GameTimer;

public class GameScores {

	@Getter static public GameTimer timer = new GameTimer();
    @Getter @Setter private ArrayList<Integer> scores;
    
    public GameScores(GameTimer timer, ArrayList<Integer> scores) {
    	
    	this.scores = scores;
    	
    }
    
    public void update(ArrayList<Integer> scores) {
    	
    	this.scores = scores;
    	
    }
    
    public void startTimer() {
    	
    	timer.start();
    	
    }
    
    public void stopTimer() {
    	
    	timer.stop();
    	
    }
    
    public void resetTimer() {

        timer.reset();

    }
    
    public String getRuntime() {
    	
    	return timer.getRuntime();
    	
    }

}
