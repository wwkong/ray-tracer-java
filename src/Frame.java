import java.awt.GridLayout;
import javax.swing.JFrame;

public class Frame extends JFrame {
	
	// Set up variables
	Screen s;
	int w = 1920;
	int h = 1080;
	
	// Set up the frame
	public Frame () {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(w,h);
		setResizable(false);
		setTitle("Graphics");
		init();
	}
	
	// Initialize the Grid
	public void init () {
		setLocationRelativeTo(null);
		setLayout(new GridLayout(1,1,0,0));
		s = new Screen(w,h);
		add(s);
		setVisible(true);
	}
	
	// For the run-configuration
	public static void main(String[] args) {
		new Frame();
	}
}