/*
 * BorderLayoutDemo.java
 *
 */
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Timer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

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
	 * Default value set to 180
	 */
	public static final long CAPTURE_TIME = 180L;

	/**
	 * determines the OS type for File system settings
	 */
	private static final String OS = System.getProperty("os.name");

	/**
	 * path to save the data
	 */
	static String pathToSave;

	/**
	 * Tracker object
	 */
	static GlobalMouseListenerExample tracker;

	/**
	 * Adds components to the applet
	 * 
	 * @param pane
	 */
	public static void addComponentsToPane(Container pane) {

		/*
		 * Determine path to save file
		 */

		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		final JButton facebook_btn = new JButton("Facebook");
		final JButton ynet_btn = new JButton("ynet");

		facebook_btn.setPreferredSize(new Dimension(200, 100));
		ynet_btn.setPreferredSize(new Dimension(200, 100));
		pane.add(facebook_btn, BorderLayout.PAGE_START);
		pane.add(ynet_btn, BorderLayout.CENTER);
		facebook_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openWebpage(new URI("http://www.facebook.com"));
					pathToSave = pathToSave + "facebook/";
					ValidateFolder();
					startTrackingMouse();

				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} finally {
					ynet_btn.setEnabled(false);
					facebook_btn.setEnabled(false);
				}

			}
		});

		ynet_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openWebpage(new URI("http://www.ynet.co.il"));
					pathToSave = pathToSave + "ynet/";
					ValidateFolder();
					startTrackingMouse();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} finally {
					ynet_btn.setEnabled(false);
					facebook_btn.setEnabled(false);
				}
			}
		});

	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {

		// Create and set up the window.
		JFrame frame = new JFrame("BorderLayoutDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addComponentsToPane(frame.getContentPane());
		// Use the content pane's default BorderLayout. No need for
		// setLayout(new BorderLayout());
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Determines the OS
		if (OS.startsWith("Windows")) {
			pathToSave = "C:/Temp/MouseRecorder/";
		} else if (OS.startsWith("Mac")) {
			pathToSave = System.getProperty("user.home")
					+ "/Temp/MouseRecorder/";

		}

		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Turn off metal's use bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

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
			Thread.sleep((long) (3 * 1000));//wait 3 seconds for page to load
			
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

	private static void ValidateFolder() {

		File theDir = new File(pathToSave);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + pathToSave);
			boolean result = false;

			try {
				result = theDir.mkdirs();

			} catch (SecurityException se) {
				se.printStackTrace();
			}
			if (result) {
				System.out.println("DIR created");
				System.out.println(theDir.getAbsolutePath());
			}
		}

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
		tracker = new GlobalMouseListenerExample(pathToSave);

		// Add the appropriate listeners for the example object.
		GlobalScreen.getInstance().addNativeMouseListener(tracker);
		GlobalScreen.getInstance().addNativeMouseMotionListener(tracker);
	}

}