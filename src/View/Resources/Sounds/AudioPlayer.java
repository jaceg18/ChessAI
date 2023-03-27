package View.Resources.Sounds;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {

    public static void playSound(boolean capture){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( (!capture ) ?
                    new File("src/View/Resources/Sounds/move-self.wav") :
                    new File("src/View/Resources/Sounds/capture.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

}
