import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

public class MouseMoveListener implements NativeMouseInputListener,
		NativeMouseWheelListener {
	PrintWriter out;

	public MouseMoveListener(String path) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		//String fileName = "/" + System.currentTimeMillis() + ".txt";
		try {
			out = new PrintWriter(new File(path ));
			out.println("Screen Resolution, " + width + "," + height);
		} catch (FileNotFoundException e) {
			System.exit(1);
		}

	}

	public void nativeMouseClicked(NativeMouseEvent e) {
		printAction(new String[] { "" + e.getWhen(), "Mouse Clicked",
				"" + e.getClickCount() });

	}

	public void nativeMousePressed(NativeMouseEvent e) {
		printAction(new String[] { "" + e.getWhen(), "Mouse Pressed",
				"" + e.getButton() });

	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		printAction(new String[] { "" + e.getWhen(), "Mouse Released",
				"" + e.getButton() });

	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		printAction(new String[] { "" + e.getWhen(), "Mouse Moved",
				"" + e.getX(), "" + e.getY() });

	}

	public void nativeMouseDragged(NativeMouseEvent e) {
		printAction(new String[] { "" + e.getWhen(), "Mouse Dragged",
				"" + e.getX(), "" + e.getY(), });

	}

	public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
		printAction(new String[] { "" + e.getWhen(), "Mouse Wheel Moved",
				"" + e.getWheelRotation(), "" + e.getScrollAmount() });
	}

	public void printAction(String[] params) {
		StringBuilder sb = new StringBuilder();
		for (String s : params) {
			sb.append(s);
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		out.println(sb.toString());
		out.flush();
	}

	public void terminate() {

		out.flush();
		out.close();
	}

}