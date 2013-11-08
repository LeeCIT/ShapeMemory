


import java.awt.Color;



/**
 * Provides geometric and mathematical utility functions.
 */
public class Geo
{
	/**
	 * Linear interpolate from A to B by fraction F.
	 */
	public static double lerp( double a, double b, double f ) {
		return a + (b-a) * f;
	}
	
	
	
	
	
	/**
	 * Linear interpolate from A to B by fraction F.
	 */
	public static Vec2 lerp( Vec2 a, Vec2 b, double f ) {
		return new Vec2( 
			lerp( a.x, b.x, f ),
			lerp( a.y, b.y, f )
		);
	}
	
	
	
	
	
	/**
	 * Linear interpolate colour from A to B by fraction F.
	 */
	public static Color lerp( Color a, Color b, double f ) {
		return new Color(
			(int) lerp( a.getRed(),   b.getRed(),   f ),
			(int) lerp( a.getGreen(), b.getGreen(), f ),
			(int) lerp( a.getBlue(),  b.getBlue(),  f )
		);
	}
	
	
	
	
	
	/**
	 * Transform a linear [0:1] interpolant into a hermite curve.
	 */
	public static double hermite( double f ) {
		return f * f * (3.0 - (2.0 * f));
	}
	
	
	
	
	
	/**
	 * Hermite interpolate from A to B by fraction F.
	 */
	public static double herp( double a, double b, double f ) {
		return lerp( a, b, hermite(f) );
	}
	
	
	
	
	
	/**
	 * Normalise given current value and min/max.
	 */
	public static double unlerp( double v, double minv, double maxv ) {
		double base  = v    - minv;
		double delta = maxv - minv;
		return base / delta;
	}
	
	
	
	
	
	/**
	 * Clamp to inclusive range. 
	 */
	public static double clamp( double v, double minv, double maxv  ) {
		return Math.min( Math.max(v,minv), maxv );
	}
	
	
	
	
	
	/**
	 * Transform a monotonically increasing linear input into a sinewave.
	 * Waveform: One dip and rise per period, zero at edges and centre.
	 * Cycle:    Dip -> zero -> rise -> zero  [v^]
	 * Range:    [-1:+1] inclusive.
	 */
	public static double sineSync( double input, double wavelength ) {
	    double half = wavelength * 0.5;
	    double mod  = (input % wavelength) - half;
	    double pm1  = mod / half;
	    return Math.sin( pm1 * Math.PI );
	}
	
	
	
	
	
	/**
	 * Same as sineSync but with user-defined output range.
	 */
	public static double sineSync( double input, double wavelength, double low, double high ) {
	    double sine = sineSync( input, wavelength );
	    double f    = (sine * 0.5) + 0.5;
	    return lerp( low, high, f );
	}
	
	
	
	
	
	/**
	 * Get relative coordinate offset for given length and direction. 
	 */
	public static Vec2 lenDir( double len, double dir ) {
		double radians = Math.toRadians( dir );
		return new Vec2( Math.cos(radians) * len,
					    -Math.sin(radians) * len );
	}
}
