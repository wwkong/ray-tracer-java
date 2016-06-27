// Basic vector class to do linear algebra
public class Vec3 {
	
	// Simple constructors
	private double x, y, z;
	public Vec3() {
		x=0; y=0; z=0;
	}
	public Vec3(double xx) {
		x=xx; y=xx; z=xx;
	}
	public Vec3(double xx, double yy, double zz) {
		x=xx;y=yy;z=zz;
	}
	
	// Access functions and update functions
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public void setX(double xx) {
		x=xx;
	}
	public void setY(double yy) {
		y=yy;
	}
	public void setZ(double zz) {
		z=zz;
	}
	public void print() {
		System.out.println("x=" + x + " y=" + y + " z=" + z);
	}
	
	// Linear algebra functions
	public double dotProduct(Vec3 vec) {
		return (x*vec.getX() + y*vec.getY() + z*vec.getZ());
	}
	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	public Vec3 add(Vec3 vec) {
		return new Vec3(x+vec.getX(), y+vec.getY(), z+vec.getZ());
	}
	public Vec3 subtract(Vec3 vec) {
		return new Vec3(x-vec.getX(), y-vec.getY(), z-vec.getZ());
	}
	public Vec3 multiply(Vec3 vec) {
		return new Vec3(x*vec.getX(), y*vec.getY(), z*vec.getZ());
	}
	public Vec3 scale(double c) {
		return new Vec3(x*c, y*c, z*c);
	}
	public Vec3 negate() {
		return scale(-1);
	}
	public Vec3 normalize() {
		return scale(1/norm());
	}
	public double dist(Vec3 vec) {
		Vec3 dirVec = subtract(vec);
		return Math.sqrt(Math.pow(dirVec.getX(),2) + 
					  	 Math.pow(dirVec.getY(),2) + 
						 Math.pow(dirVec.getZ(),2));
	}
}