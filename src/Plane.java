public class Plane extends Object {
	
	// Simple constructors
	private Vec3 p0; // Point on the plane
	private Vec3 n; // Normal
	public Plane() {
		this(new Vec3(0,-1,0), new Vec3(0,1,0));
	}
	public Plane(Vec3 pp, Vec3 nn) {
		super();
		p0=pp; n=nn.normalize();
	}
	
	// Advanced constructors
	public Plane(Vec3 pp, Vec3 nn, Vec3 sc, Vec3 ec) {
		super(sc, ec, false, 0, 1);
		p0=pp; n=nn.normalize();
	}
	public Plane(Vec3 pp, Vec3 nn, Vec3 sc, Vec3 ec, boolean refl) {
		super(sc, ec, refl, 0, 1);
		p0=pp; n=nn.normalize();
	}
	public Plane(Vec3 pp, Vec3 nn, Vec3 sc, Vec3 ec, boolean refl, double transp) {
		super(sc, ec, refl, transp, 1);
		p0=pp; n=nn.normalize();
	}
	public Plane(Vec3 pp, Vec3 nn, Vec3 sc, Vec3 ec, boolean refl, double transp, double refIdx) {
		super(sc, ec, refl, transp, refIdx);
		p0=pp; n=nn.normalize();
	}
	
	// Access functions
	public Vec3 getPoint() {
		return p0;
	}
	public Vec3 getNormal() {
		return n;
	}
	
	// -----------------
	// Tracing functions
	
	// Return the intersection point if it exists and null otherwise
	@Override
	public Vec3 intersection(Vec3 camera, Vec3 primRay) {
		
		double denom = primRay.dotProduct(n);
		if (denom > 1e-6) {
			Vec3 p0l0 = p0.subtract(camera);
			double t = p0l0.dotProduct(n) / denom;
			return camera.add(primRay.scale(t));
		}
		else {
			return null;
		}
	}

	// For a point on the plane, return the unit normal vector
	public Vec3 normal(Vec3 p) {
		if (p.dotProduct(n) > 0) {
			return n.negate();
		} else {
			return n;
		}
		
	}
	
}