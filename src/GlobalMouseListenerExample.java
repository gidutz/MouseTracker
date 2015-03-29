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
		printAction(new String[] { "Mosue Clicked", "" + e.getClickCount() });

	}

	public void nativeMousePressed(NativeMouseEvent e) {
		printAction(new String[] { "Mosue Pressed", "" + e.getButton() });

	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		printAction(new String[] { "Mosue Released", "" + e.getButton() });

	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		printAction(new String[] { "Mosue Moved", "" + e.getX(), "" + e.getY() });

	}

	public void nativeMouseDragged(NativeMouseEvent e) {
		printAction(new String[] { "Mosue Dragged", "" + e.getX(),
				"" + e.getY() });

	}

	public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
		printAction(new String[] { "Mosue Wheel Moved",
				"" + e.getWheelRotation() });
	}

	public void printAction(String[] params) {

		String actionTime = "" + System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		sb.append(actionTime);
		sb.append(",");
		for (String s : params) {
			sb.append(s);
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		out.println(sb.toString());
	}

	public void terminate() {

		out.flush();
		out.close();
	}

}