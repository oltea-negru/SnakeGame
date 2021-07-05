import javax.sound.sampled.*;
import java.io.File;

/**
 * Class for audio support
 */
public class AudioPlayer {

    /**
     * Plays a sound effect
     *
     * @param soundLocation
     */
    void playSound(String soundLocation) {
        try {
            File f = new File("./" + soundLocation);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the background music in a loop
     *
     * @param musicLocation
     */
    void playMusic(String musicLocation) {
        try {
            File musicPath = new File("./" + musicLocation);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}