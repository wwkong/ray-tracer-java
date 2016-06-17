public class Object {
	
	// Attributes
	private Vec3 surfaceColor, emissionColor;
	private boolean reflective;
	private double transparency, refractionIndex;
	
	// Simple constructors
	public Object() {
		// Default is a pure green object
		surfaceColor = new Vec3(0,1,0); 
		emissionColor = null;
		reflective = false;
		transparency = 0;
		refractionIndex = 1;
	}
	public Object(Vec3 sc, Vec3 ec, boolean refl, double transp, double refIdx) {
		surfaceColor = sc; 
		emissionColor = ec;
		reflective = refl;
		transparency = transp;
		refractionIndex = refIdx;
	}
	
	// Get attributes
	public Vec3 getSurfaceColor() {
		return surfaceColor;
	}
	public Vec3 getEmissionColor() {
		return emissionColor;
	}
	public boolean getReflective() {
		return reflective;
	}
	public double getTransparency() {
		return transparency;
	}
	public double getRefractionIndex() {
		return refractionIndex;
	}

	
	// Return the intersection point if it exists and null otherwise
	public Vec3 intersection(Vec3 camera, Vec3 primRay) {
		// Default return value for now
		return null;
	}
	// Return the normal vector to a point on the object
	public Vec3 normal(Vec3 p) {
		// Default return value for now
		return null;
	}
	
}