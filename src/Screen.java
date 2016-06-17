import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

// Set the stage and runs the tracer
public class Screen extends JPanel {
	
	// Initialize
	private BufferedImage bi;
	private Graphics2D g;
	private int w=640;
	private int h=480;
	
	// For image processing
	public Screen(int ww, int hh) {
		// Set the stage
		Scene rs;
		w = ww; h = hh;
		bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		g = bi.createGraphics();
		rs = new Scene();
		// Add light sources
		rs.addObject(new Sphere(1.0, new Vec3(4,10,7),     new Vec3(1),     new Vec3(1)));
		// Add diffuse objects
		rs.addObject(new Sphere(1.0, new Vec3(-1.5,0,10), new Vec3(0,1,0), null));
		// Add reflective / refractive objects
		rs.addObject(new Sphere(0.5, new Vec3(0,-0.2,8),     new Vec3(0,0,1), null));
		rs.addObject(new Plane(new Vec3(0,-2,0), new Vec3(0,-1,0), new Vec3(0.8,1,0.9), null));
		
		// Trace the objects and paint the image
		rs.render(bi);
		// Output to a file
		try {
			ImageIO.write(bi, "png", new File("res/raytrace.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void paint(Graphics g) {
		g.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
	}
	
}