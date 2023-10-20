package customComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class GameTimer {

    private int seconds = 0;
    private int minutes = 0;
    private int elapsedTime = 0;
    private String time  = minutes + ":" + seconds;
    
    public GameTimer() { };
    
    Timer timer = new Timer(1000, new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
        	
            elapsedTime = elapsedTime + 1000;
            minutes = (elapsedTime/60000) % 60;
            seconds = (elapsedTime/1000) % 60;
            time = minutes + ":" + seconds;
            
        }
        
    });

    public void reset() {

        seconds = 0;
        minutes = 0;
        elapsedTime = 0;
        time = minutes + ":" + seconds;

    }
    
    public void start() {

        timer.start();

    }

    public void stop() {

       timer.stop();

    }

    public String getRuntime() {

        return time;

    }

}
