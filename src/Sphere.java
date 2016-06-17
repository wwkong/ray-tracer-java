public class Sphere extends Object {
	
	// Simple constructors
	private double r; // Radius
	private Vec3 c; // Center
	public Sphere() {
		super();
		r=1; c= new Vec3(0,0,0);
	}
	public Sphere(double rr) {
		super();
		r=rr; c= new Vec3(0,0,0);
	}
	public Sphere(double rr, Vec3 cc) {
		super();
		r=rr; c=cc;
	}
	
	// Advanced constructors
	public Sphere(double rr, Vec3 cc, Vec3 sc, Vec3 ec) {
		super(sc, ec, false, 0, 1);
		r=rr; c=cc;
	}
	public Sphere(double rr, Vec3 cc, Vec3 sc, Vec3 ec, boolean refl) {
		super(sc, ec, refl, 0, 1);
		r=rr; c=cc;
	}
	public Sphere(double rr, Vec3 cc, Vec3 sc, Vec3 ec, boolean refl, double transp) {
		super(sc, ec, refl, transp, 1);
		r=rr; c=cc;
	}
	public Sphere(double rr, Vec3 cc, Vec3 sc, Vec3 ec, boolean refl, double transp, double refIdx) {
		super(sc, ec, refl, transp, refIdx);
		r=rr; c=cc;
	}
	
	// Access functions
	public double getRadius() {
		return r;
	}
	public Vec3 getCenter() {
		return c;
	}
	
	// -----------------
	// Tracing functions
	
	// Return the intersection point if it exists and null otherwise
	@Override
	public Vec3 intersection(Vec3 camera, Vec3 primRay) {
		
		// V is the normalized direction of the primary ray
		// X is the position of the camera
		// C is the position of the center of the sphere
		// P is the potential point of intersection
		// D is the point that connects P and C orthogonally
		
		// Helper qualities
		Vec3 v,xc;
		double dpVXC,dXC2,dXD2,dPD2;
		v  = primRay.normalize(); 	
		xc = c.subtract(camera); 	
		dpVXC = v.dotProduct(xc); // Check if we are shooting at the sphere or away from it
		dXC2 = Math.pow(xc.norm(),2); 			
		dXD2 = Math.pow(dpVXC,2);  
		dPD2 =  Math.pow(r, 2) - dXC2 + dXD2; 
		Vec3 P = camera.add(v.scale(Math.sqrt(dXD2)-Math.sqrt(dPD2)));
		
		// Distance check with some bias
		if (dPD2 > 0 && dpVXC > 0) {
			return P;
		} else {
			return null;
		}
	}

	// For a point on the sphere, return the unit normal vector
	public Vec3 normal(Vec3 p) {
		return p.subtract(c).normalize();
	}
	
}