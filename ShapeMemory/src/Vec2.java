


/**
 * Two-dimensional vector.
 */
public class Vec2
{
	public double x,y;
	
	
	
	
	
	public Vec2( double x, double y ) {
		this.x = x;
		this.y = y;
	}
	
	
	
	
	
	public Vec2 add( Vec2 v ) {
		return new Vec2( x + v.x,
						 y + v.y );
	}
	
	
	
	
	
	public Vec2 negate() {
		return new Vec2( -x, -y );
	}
	
	
	
	
	
	public double getSmallest() {
		return Math.min( x, y );
	}
}
