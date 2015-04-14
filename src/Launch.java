/*
 * BorderLayoutDemo.java
 *
 */
import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Timer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 * 
 * @author gidutz
 *
 */
public class Launch {
	/**
	 * Sets the run time of the capture, starting when a button is clicked
	 * Default value set to 90
	 */
	public static long CAPTURE_TIME;

	/**
	 * determines the OS type for File system settings
	 */
	private static final String OS = System.getProperty("os.name");

	private static final String USAGE = "Usage: MouseTracker \"facebook\"/\"ynet\" path_to_save_data execution_time";

	/**
	 * path to save the data
	 */
	static String pathToSave;

	/**
	 * Tracker object
	 */
	static MouseMoveListener tracker;

	/**
	 * Opens the request uri, plays a sound to indicate recording started, and
	 * holds the software for 3 seconds. closes the app after EXCEUTION_TIME
	 * seconds
	 * 
	 * @param uri
	 */
	public static void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop()
				: null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {

			Clip openingSound = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(Launch.class.getResource("ding.wav"));
			openingSound.open(inputStream);
			openingSound.start();
			Thread.sleep((long) (3 * 1000));// wait 3 seconds for page to load

		} catch (Exception e) {
		} finally {// setting ending timer
			Timer timer = new Timer();
			EndingTimer exitApp = new EndingTimer();
			timer.schedule(exitApp, new Date(System.currentTimeMillis()
					+ CAPTURE_TIME * 1000));

			// starts tracking mouse moves
			try {
				GlobalScreen.registerNativeHook();
			} catch (NativeHookException ex) {
				System.err
						.println("There was a problem registering the native hook.");
				System.err.println(ex.getMessage());

				System.exit(1);
			}

		}
	}

	private static String ValidateFolder(String path) {

		File theDir = new File(path);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.err.println("Folder Does not exsist");
			System.exit(21424);

		}
		return theDir.getAbsolutePath();

	}

	public static void openWebpage(URL url) {
		try {
			openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void startTrackingMouse() {
		// Construct the example object.
		tracker = new MouseMoveListener(pathToSave);

		// Add the appropriate listeners for the example object.
		GlobalScreen.getInstance().addNativeMouseListener(tracker);
		GlobalScreen.getInstance().addNativeMouseMotionListener(tracker);
		GlobalScreen.getInstance().addNativeMouseWheelListener(tracker);

	}

	public static void main(String[] args) {

		validateArgs(args);

		String url = "http://google.com";
		if (args[0].equalsIgnoreCase("facebook")) {
			url = "http:/facebook.com";
		} else if (args[0].equalsIgnoreCase("ynet")) {
			url = "http://ynet.co.il";
		}

		try {
			openWebpage(new URI(url));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			System.exit(1);
		}

		pathToSave = ValidateFolder(args[1]);
		CAPTURE_TIME = Integer.parseInt(args[2]);
		startTrackingMouse();

	}

	private static void validateArgs(String[] args) {
		if (args.length != 3) {
			System.err.println(USAGE);
			System.exit(1);
		} else if (!args[0].equalsIgnoreCase("ynet")
				&& !args[0].equalsIgnoreCase("facebook")) {
			System.err
					.println("The first argument should be \"facebook\" or \"ynet\"");
			System.exit(2);
		} else if (ValidateFolder(args[1]) == null) {
			System.err.println("could not create directory");
			System.exit(3);

		} else if (!args[2].matches("\\d+")) {
			System.err.println("3rd argument must be an integer");
			System.exit(3);
		}

	}
}