import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;

public class GlobalMouseListenerExample implements NativeMouseInputListener {
	PrintWriter out;

	public GlobalMouseListenerExample(String path) {
		String fileName = "" + System.currentTimeMillis() + ".txt";
		try {
			out = new PrintWriter(new File(path + fileName));
		} catch (FileNotFoundException e) {
			System.exit(1);
		}

	}

	public void nativeMouseClicked(NativeMouseEvent e) {
		out.println("Mosue Clicked: " + e.getClickCount());
		out.flush();

	}

	public void nativeMousePressed(NativeMouseEvent e) {
		out.println("Mosue Pressed: " + e.getButton());
		out.flush();

	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		out.println("Mosue Released: " + e.getButton());
		out.flush();

	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		out.println("Mosue Moved: " + e.getX() + ", " + e.getY());
		out.flush();

	}

	public void nativeMouseDragged(NativeMouseEvent e) {
		out.println("Mosue Dragged: " + e.getX() + ", " + e.getY() + " "
				+ System.currentTimeMillis());
		out.flush();

	}

	public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
		out.println("Mosue Wheel Moved: " + e.getWheelRotation());
		out.flush();
	}

}