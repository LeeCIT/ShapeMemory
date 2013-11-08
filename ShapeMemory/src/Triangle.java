


/**
 * Defines a triangle.
 */
public class Triangle
{
	public Vec2 a,b,c;
	
	
	
	
	
	public Triangle( Vec2 a, Vec2 b, Vec2 c ) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	
	
	
	
	public Vec2 get( int index ) {
		Vec2[] array = { a, b, c };
		return array[ index % 3 ];
	}
}