package main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
    // AN INITIAL IMPLEMENTATION USING ONLY ONE CLIP DID NOT WORK BECAUSE I WAS NOT ABLE TO STOP THE LOOP
    // PLAYING AFTER ANOTHER CLIP SOUND WAS PLAYED (THE MUSIC WOULD STOP AT GAME OVER, BUT NOT IF A
    // SNAKE HAD EATEN, THEN IT WOULD CONTINUE PLAYING AFTER GAME OVER).
    // A SECOND IMPLEMENTATION USING A HASH MAP FOR ALL THE CLIPS WAS TOO SLOW TO RESET EACH
    // SOUND AFTER IT WAS PLAYED, SO THE EAT SOUND WOULD OCASSIONALLY NOT PLAY EVEN THOUGH THE SNAKE
    // HAD EATEN
    // THIS IMPLEMENTATION SEEMS TO WORK SO FAR

    final static HashMap<Sound, URL> soundURL = loadURLMap("static/sound");
    static Clip loop;
    static Clip single;

    public static enum Sound {

        BACKGROUND,
        GAME_OVER,
        NEXT_LEVEL,
        EAT,
        BUTTON
        
    }

    private static HashMap<Sound, URL> loadURLMap(String folderPath) {

        // GET EACH SOUND PATH FROM THE FOLDER
        FolderHandler folderH = new FolderHandler(folderPath);        
        ArrayList<String> soundPaths = folderH.getFilePaths("wav");

        // LOAD SOUNDS URL TO HASH
        HashMap<Sound, URL> tempSounds = new HashMap<Sound, URL>();
        tempSounds.put(Sound.BACKGROUND, getURL(soundPaths.get(0)));
        tempSounds.put(Sound.EAT, getURL(soundPaths.get(1)));
        tempSounds.put(Sound.GAME_OVER, getURL(soundPaths.get(2)));
        tempSounds.put(Sound.NEXT_LEVEL, getURL(soundPaths.get(3)));
        tempSounds.put(Sound.BUTTON, getURL(soundPaths.get(4)));
       
        return tempSounds;

    }

    private static URL getURL(String path) {

        try {

            return new File(path).toURI().toURL();

        } catch (MalformedURLException e) {
        
            e.printStackTrace();
        
        }
        return null;

    }

    public void setFile(Sound sound, boolean isLoop) {

        try {

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL.get(sound));
            if (isLoop) {
                loop = AudioSystem.getClip();
                loop.open(ais);
            } else {
                single = AudioSystem.getClip();
                single.open(ais);
            }
        
        } catch (Exception e){

            e.printStackTrace();

        }
    
    }

    public void playLoop() {

        loop.start();
        loop.loop(Clip.LOOP_CONTINUOUSLY);

    }

    public void playSE() {

        single.start();

    }


    public void stopLoop() {

        loop.stop();
        loop.setFramePosition(0);

    }
    
}
