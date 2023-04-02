package customComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class GameTimer {

    int seconds = 0;
    int minutes = 0;
    int elapsedTime = 0;
    boolean started = false;
    String seconds_string = String.format("%02d", seconds);
    String minutes_string = String.format("%02d", minutes);
    String time = minutes + ":" + seconds;

    Timer timer = new Timer(1000, new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
            elapsedTime = elapsedTime + 1000;
            minutes = (elapsedTime/60000) % 60;
            seconds = (elapsedTime/1000) % 60;
            minutes_string = String.format("%02d", minutes);
            seconds_string = String.format("%02d", seconds);
            time = minutes + ":" + seconds;
        }
        
    });

    public void reset() {

        started = false;
        seconds = 0;
        minutes = 0;
        elapsedTime = 0;
        seconds_string = String.format("%02d", seconds);
        minutes_string = String.format("%02d", minutes);
        time = minutes + ":" + seconds;

    }
    
    public void start() {

        started = true;
        timer.start();

    }

    public void stop() {

       timer.stop();
       started = false;

    }

    public boolean hasNotStarted() {

        return !started;

    }

    public String getRuntime() {

        return time;

    }



}
