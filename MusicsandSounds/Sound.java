package MusicsandSounds;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    
    Clip clip;
    URL[] soundURL = new URL[30];

    public Sound()
    {

        soundURL[0] = getClass().getResource("mainMenuMusic.wav");
        soundURL[1] = getClass().getResource("ambiance.wav");
        soundURL[2] = getClass().getResource("bossMusic.wav");
        soundURL[3] = getClass().getResource("attackSound.wav");
        soundURL[4] = getClass().getResource("enemyDeath1.wav");
        soundURL[5] = getClass().getResource("enemyDeath2.wav");
        soundURL[6] = getClass().getResource("click.wav");
        soundURL[7] = getClass().getResource("fail.wav");
        soundURL[8] = getClass().getResource("win.wav");
        soundURL[9] = getClass().getResource("purchase.wav");
        soundURL[10] = getClass().getResource("parryfinal.wav");
        soundURL[11] = getClass().getResource("magic1.wav");
        soundURL[12] = getClass().getResource("roomMusic.wav");
    }

    public void setFile(int i)
    {
        try {

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            
        } catch (Exception e) {
            // TODO: handle exception
        }


    }
    public void play()
    {
        if(clip != null)
        {
            clip.start();
        }
    }
    public void stop()
    {
        if(clip != null)
        {
            clip.stop();
        }
    }
    public void loop()
    {
        if(clip != null)
        {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void close()
    {
        if(clip != null)
        {   
            clip.close();
        }
    }
    public URL[] getSoundURL() {
        return soundURL;
    }
    public Clip getClip() {
        return clip;
    }

}