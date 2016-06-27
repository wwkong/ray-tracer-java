import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Scene {
		
	// Constants
	private int maxDepth = 3;
	// Simple constructors
	private double fov;
	private double ambientLight;
	private ArrayList<Object> objects;
	// Camera will start at (0,0,0)
	public Scene() {
		fov	   = 30;
		ambientLight = 0.2;
		objects = new ArrayList<Object>();
	}
	public Scene(double f) {
		fov	   = f;
		ambientLight = 0.2;
		objects = new ArrayList<Object>();
	}
	public Scene(double f, double l) {
		fov	   = f;
		ambientLight = l;
		objects = new ArrayList<Object>();
	}
	public Scene(double f, double l, ArrayList<Object> o) {
		fov    = f;
		ambientLight = l;
		objects = o;
	}
	
	// Access functions
	public double getFOV() {
		return fov;
	}
	public double getAmbientLight() {
		return ambientLight;
	}
	public ArrayList<Object> getObjects() {
		return objects;
	}
	
	// Update functions
	public void addObject(Object o) {
		objects.add(o);
	}
	
	// ----------------------
	// Main tracing functions
	
	// Trace an object out for a given starting point (camera) and direction (primRay) and return a color
	public Vec3 trace(Vec3 camera, Vec3 primRay, BufferedImage bi, int w, int h, int depth) {
		
		// Reset some parameters
		Vec3 intersection = null;
		Vec3 closestP = null;
		Vec3 normalClosestP = null;
		Vec3 scVec = null;
		Object object = null;
		int objectIdx = -1;
		double distI;
		double bias = 1e-4; // Add bias away from the object that we are tracing
		boolean inShadow = false;
		double minDist = Double.POSITIVE_INFINITY;
		
		// ----------------------------------------
		// Iterate over the objects / light sources
		
		for (int i=0; i<objects.size(); i++) {
			// Get intersections and search for collisions
			intersection = objects.get(i).intersection(camera, primRay);
			// Get the closest, non-light emitting object
			if (intersection != null && objects.get(i).getEmissionColor() == null) {
				distI = camera.dist(intersection); 
				if  (distI < minDist) {
					object = objects.get(i);
					objectIdx = i;
					minDist = distI;
					closestP = intersection;
					normalClosestP = object.normal(closestP);
				}
			} 
		} // End main object loop
		
		// --------------------
		// Compute interactions
		
		// ==========================================
		// If we miss, render the background as black 
		if (object == null) {
			scVec = new Vec3(0);
		}
		
		// ================================
		// Reflective or refractive objects
		else if ((object.getReflective() || object.getTransparency() > 0) && depth < maxDepth) {
			// Initialize
			Vec3 reflection = new Vec3(0);
			Vec3 refraction = new Vec3(0);
			double kr = 1;
			// ----------
			// Reflection
			if (object.getReflective()) {
				Vec3 scaledNCP = normalClosestP.scale(camera.subtract(closestP).dotProduct(normalClosestP));								
				Vec3 reflPoint = camera.add(scaledNCP.subtract(camera).scale(2));
				Vec3 reflDirection = reflPoint.subtract(closestP).normalize();
				reflection = trace(closestP.add(normalClosestP.scale(bias)), reflDirection, bi, w, h, depth+1);
			}
			// ----------
			// Refraction
			if (object.getTransparency() > 0) {
				
				// Determine eta based on if we are inside or outside of the medium using cos(theta0)
				Vec3 sgnNormal, refrDirection;
				double eta = 1;
				double etai, etat;
				double cosi = camera.dotProduct(normalClosestP);
				// Outside of the object
				if (cosi < 0) {
					etai = 1;
					etat = object.getRefractionIndex();
					cosi = -cosi;
					sgnNormal = normalClosestP;
				} 
				// Inside of the object
				else { 
					etat = object.getRefractionIndex();
					etai = 1;
					eta = object.getRefractionIndex();
					sgnNormal = normalClosestP.negate();
				}
				// Helper variables
				eta = etai/etat;
				double k = 1-Math.pow(eta, 2)*(1-Math.pow(cosi, 2));
				// Total internal reflection
				if (k < 0) {
					refraction = new Vec3(0);
				}
				// Some transmission
				else {
					refrDirection = camera.scale(eta).add(sgnNormal.scale(eta*cosi-Math.sqrt(k)));
					refraction = trace(closestP.subtract(normalClosestP.scale(bias)), refrDirection, bi, w, h, depth+1);
					
					// Compute the proportion of reflectance
					
					// Find sint using Snell's law
					double sint = eta * Math.sqrt(Math.max(0, 1-cosi*cosi));
					if (sint >= 1) {
						kr = 1;
					} else {
						double cost = Math.sqrt(Math.max(0,1-sint*sint));
						double Rs = ((etat*cosi) - (etai*cost)) / ((etat*cosi) + (etai*cost));
						double Rp = ((etai*cosi) - (etat*cost)) / ((etai*cosi) + (etat*cost));
						kr = (Rs*Rs + Rp*Rp) / 2;
					}
					
				}
			}			
			
			// -------------------------------
			// Combine with the Fresnel effect
			scVec = reflection.scale(kr).add(refraction.scale((1-kr)*object.getTransparency()));
			scVec = scVec.multiply(object.getSurfaceColor());
			
		} 
		// ===============
		// Diffuse objects
		else {				
			
			// Find the light sources
			for (int i=0; i<objects.size(); i++) {
				if (objects.get(i).getEmissionColor() != null &&
					objects.get(i) instanceof Sphere &&
					i != objectIdx) {
					
					// Compute the interaction
					Sphere light = (Sphere) objects.get(i);
					Vec3 lightDirection = light.getCenter().subtract(closestP).normalize();
					double intensity    = lightDirection.dotProduct(normalClosestP);		
					
					// Check if we are in the shadow of another object
					for (int k=0; k<objects.size(); k++) {
						if (k != i 		   && 
							k != objectIdx &&
							(objects.get(k).intersection(closestP, lightDirection) != null)) {
							inShadow = true;
							break;
						}
					}		
					
					// Compute the color of the pixel based on all of the info
					if (intensity > 0 && !inShadow) {
						// Add light source impact
						double lsConst = (1-ambientLight)*intensity*light.getEmissionColor().getX();
						scVec = object.getSurfaceColor().scale(lsConst);
						// Add ambient light
						scVec = scVec.add(new Vec3(ambientLight));									
					} else {
						// Add ambient light
						scVec = new Vec3(ambientLight);
					}
					
				}// End light source calculation
			} // End light source search
			
		// =======================
		// End diffuse object case
		} 
		
		// Return the RGB color vector
		return scVec;
		
	}
	// Rendering function which writes to a BufferedImage
	public void render(BufferedImage bi) {
		
		// Helper variables
		Vec3 primRay = new Vec3();
		double imgWidth = bi.getWidth();
		double imgHeight = bi.getHeight();
		double aspectRatio = imgWidth / imgHeight;
		double qHeight = Math.tan(Math.PI * 0.5 * fov / 180);
		
		// Iterate over the pixels
		for (int w=0; w<imgWidth; w++) {
			for (int h=0; h<imgHeight; h++) {
				
				// The camera will shoot in the +z direction with the image plane situated at (x,y,1)
				primRay.setX(2 * qHeight * aspectRatio * ((((double) w+1)/imgWidth)-0.5));
				primRay.setY(2 * qHeight * (0.5-(((double) h+1)/imgHeight)));
				primRay.setZ(1);
				
				// Trace an object if it intersects with out primary ray
				Vec3 scVec = trace(new Vec3(0,0,0), primRay, bi, w, h, 1);
				// Update pixel
				bi.setRGB(w, h, new Color((float) scVec.getX(),
										  (float) scVec.getY(),
										  (float) scVec.getZ()).getRGB());
				
			} // End h loop
		} // End w loop
		
	}
	
}