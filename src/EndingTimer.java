import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Plays a sound and shuts down the app
 * 
 * @author gidutz
 *
 */
public class EndingTimer extends TimerTask {

	public void run() {
		try {

			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(getClass().getResource("ding.wav"));
			clip.open(inputStream);
			clip.start();
			Thread.sleep((long) (1.4 * 1000));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Soutdown schedueled");
		Launch.tracker.terminate();
		System.exit(0);
	}

}
