package main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundHandler {

    Clip clip;
    ArrayList<URL> soundURL = new ArrayList<URL>();

    public SoundHandler() {

        loadSoundPaths();

    }

    private void loadSoundPaths() {

        FolderHandler folderH = new FolderHandler("static/sound");        
        ArrayList<String> soundPaths = folderH.getFilePaths("wav");

        for (String soundPath : soundPaths) {
            
            soundURL.add(getURL(soundPath));
            
        }
        
    }

    private URL getURL(String path) {

        try {

            return new File(path).toURI().toURL();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public void setFile(int soundIx) {

        try {

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL.get(soundIx));
            clip = AudioSystem.getClip();
            clip.open(ais);
        
        } catch (Exception e){

            e.printStackTrace();

        }
    
    }

    public void play() {

        clip.start();

    }

    public void loop() {

        clip.loop(Clip.LOOP_CONTINUOUSLY);

    }

    public void stop() {

        clip.stop();

    }
    
}
