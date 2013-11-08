


import java.awt.*;
import java.awt.geom.AffineTransform;



/**
 * Draws various animated geometric shapes.
 */
public class ShapeGraphics
{
	/**
	 * Draw shape inside a region.  The shape is positioned and scaled automatically.
	 * @param g Graphics to draw with
	 * @param r Region to draw inside
	 * @param shape Shape to draw
	 */
	public static void draw( Graphics g, Region r, ShapeType shape ) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform lastMatrix = g2d.getTransform();
		AffineTransform identity   = new AffineTransform();
		g2d.setTransform( identity );
		
		// Looks nice, but it's far too slow.
		//g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		
		switch (shape) {
		    case triangle:  ShapeGraphics.drawTriSpline( g, r );  break;
		    case sinusoid:  ShapeGraphics.drawSinusoid ( g, r );  break;
		    case barbell:   ShapeGraphics.drawBarbell  ( g, r );  break;
		    case spiral:    ShapeGraphics.drawSpiral   ( g, r );  break;
		    case fern:      ShapeGraphics.drawFern     ( g, r );  break;
		    default: break;
		}
		
		g2d.setTransform( lastMatrix );
	}
	
	
	
	
	
	private static void drawFern( Graphics g, Region region ) {
		double wind    = Geo.sineSync(Time.now,600.0)*3.0 + Geo.sineSync(Time.now,720.0)*-0.5;
		double baseLen = (region.getSmallest() * 1.33) / 8.0;
		double xOff    = -baseLen * 0.5;
		double yOff    = (region.getSize().y * 0.5) - (baseLen * 0.5);
		Vec2   pos     = region.getCentre().add( new Vec2(xOff,yOff) );
		double aspect  = region.getAspect();
		
		if (aspect <= 1.0) // Keep centred
			pos.y -= region.getSize().y * 0.5 * (1.0 - aspect);
		
		drawFernRecursive( g, pos, baseLen, 45, 15+wind, 8 );
		drawCircle( g, pos, baseLen/16.0, true );
	}
	
	
	
	
	
	private static void drawFernRecursive( Graphics g, Vec2 pos, double baseLength, double direction, double curvature, int depth ) {
		if (depth <= 0)
			return;
		
		Color   colA  = new Color( 16, 32,   8 );
		Color   colB  = new Color( 32, 128, 24 );
		Polygon poly  = new Polygon();
		int     verts = 16;
		
		for (int v=0; v<verts; v++) {
			poly.addPoint( (int) pos.x, (int) pos.y );
			
			direction  += curvature;
			curvature  *= 0.75;
			baseLength *= 0.85;
			
			Vec2 offset = Geo.lenDir( baseLength, direction );
			pos = pos.add( offset );
			
			if (baseLength >= 0.75) { // Speed optimisation: don't draw tiny leaves
				double vertFrac = 1.0 - (((double) v) / (verts-1));
				Color colPrev = g.getColor();
				Color colLerp = Geo.lerp( colA, colB, vertFrac );
				g.setColor( colLerp );
				drawFernRecursive( g, pos, baseLength*0.4, direction+75.0, curvature, depth/2 );
				drawFernRecursive( g, pos, baseLength*0.4, direction-75.0, curvature, depth/2 );
				g.setColor( colPrev );
			}
		}
		
		g.drawPolyline( poly.xpoints, poly.ypoints, poly.npoints );
	}
	
	
	
	
	
	private static void drawCircle( Graphics g, Vec2 pos, double radius, boolean filled ) {
		Polygon poly     = new Polygon();
		double  vertices = 64.0;
		
		for (double degs=0; degs<360.0; degs+=360.0/vertices) {
			Vec2 offset = Geo.lenDir( radius, degs );
			Vec2 vertex = pos.add( offset );			
			poly.addPoint( (int) vertex.x, (int) vertex.y );
		}
		
		if (filled)
			 g.fillPolygon( poly );
		else g.drawPolygon( poly );
	}
	
	
	
	
	
	private static void drawSpiral( Graphics g, Region region ) {
		Polygon poly	 = new Polygon();
		double  circDegs = 360.0;
		double  circOffs = circDegs / 512.0;
		double  circles  = 6.0;
		double  degsMax  = circDegs * circles;
		double  rotation = Time.now * 3.0;
		
		for (double degs=0; degs<degsMax; degs+=circOffs) {
			double ratio  = 1.0 - (degs / (degsMax - circOffs));
			double radius = region.getDrawRadius() * ratio;
			Vec2   v      = region.getCentre().add( Geo.lenDir(radius, degs+rotation) );
			
			poly.addPoint( (int) v.x, (int) v.y );
		}
		
		g.drawPolyline( poly.xpoints, poly.ypoints, poly.npoints );
	}
	
	
	
	
	
	private static void drawSinusoid( Graphics g, Region region ) {
		Polygon poly	  = new Polygon();
		double  circDegs  = 360.0;
		double  circOffs  = circDegs / 256.0;
		double  radius	  = region.getDrawRadius();
		double  waveAmp   = radius * 0.5;
		double  rInner    = radius - waveAmp;
	    double  rOuter    = radius;
		double  waveCount = 7.0;
		double  rotation  = (Time.now/6.0) % circDegs;
		
		for (double degs=0; degs<circDegs; degs+=circOffs) {
			double ratio    = (degs - circOffs) / circDegs;
		    double sineA    = Geo.sineSync(ratio-Time.now/1500.0, 1.0/ waveCount,    0, 0.75 );
		    double sineB    = Geo.sineSync(ratio+Time.now/3000.0, 1.0/(waveCount*2), 0, 0.25 );
		    double sinewave = sineA + sineB;
		    double radiusF  = Geo.lerp( rInner, rOuter, sinewave );
			Vec2   v 		= region.getCentre().add( Geo.lenDir(radiusF, degs+rotation) );
			
			poly.addPoint( (int) v.x, (int) v.y );
		}
		
		int offset = 3;
		poly.translate( -offset, -offset );
		Color colPrev = g.getColor();
		g.setColor( new Color(255,32,192) ); // Neon fuschia
		g.fillPolygon( poly );
		
		poly.translate( +offset, +offset );
		g.setColor( colPrev );
		g.fillPolygon( poly );
	}
	
	
	
	
	
	private static void drawBarbell( Graphics g, Region region ) {
		double radius      = region.getDrawRadius();
		double radiusBell  = radius * 0.33;
		double radiusBar   = radius - radiusBell;
		double angle       = -Time.now / 6.0;
		Vec2   origin      = region.getCentre();
		Vec2   posBellA    = origin.add( Geo.lenDir(+radiusBar, angle) );
		Vec2   posBellB    = origin.add( Geo.lenDir(-radiusBar, angle) );
		double sinewave    = Geo.sineSync( Time.now, 140, 0, 1 );
		double radiusBellA = Geo.lerp( radiusBell, radiusBell*0.25, 	sinewave );
		double radiusBellB = Geo.lerp( radiusBell, radiusBell*0.25, 1.0-sinewave );
		double barMinY     = Math.min( radiusBar*0.5, Math.min(radiusBellA,radiusBellB) );
		Vec2   barSize     = new Vec2( radiusBar*2.0, barMinY );
		
		drawOrientedRect( g, origin, barSize, angle, true );
		
		drawCircle( g, posBellA, radiusBellA, true );
		drawCircle( g, posBellB, radiusBellB, true );
		
		Color colPrev = g.getColor();
		
		g.setColor( new Color(255,240,224) ); // Off-white
		drawCircle( g, posBellA, radiusBellA*0.1, true );
		drawCircle( g, posBellB, radiusBellB*0.1, true );
		
		g.setColor( new Color(255,240,64) ); // Neon yellow
		drawCircle( g, posBellA, radiusBellA*0.33, false );
		drawCircle( g, posBellB, radiusBellB*0.33, false );
		
		g.setColor( colPrev );
	}
	
	
	
	
	
	private static void drawOrientedRect( Graphics g, Vec2 centre, Vec2 size, double angle, boolean filled ) {
		Polygon poly = new Polygon();
		Vec2    offH = Geo.lenDir( size.x*0.5, angle    );
		Vec2    offV = Geo.lenDir( size.y*0.5, angle+90 );
		Vec2    a    = centre.add( offH         .add(offV         ) );
		Vec2    b    = centre.add( offH         .add(offV.negate()) );
		Vec2    c    = centre.add( offH.negate().add(offV.negate()) );
		Vec2    d    = centre.add( offH.negate().add(offV         ) );
		
		poly.addPoint( (int) a.x, (int) a.y );
		poly.addPoint( (int) b.x, (int) b.y );
		poly.addPoint( (int) c.x, (int) c.y );
		poly.addPoint( (int) d.x, (int) d.y );
		
		if (filled)
			 g.fillPolygon( poly );
		else g.drawPolygon( poly );
	}
	
	
	
	
	
	private static void drawTriSpline( Graphics g, Region region ) {
		Vec2   centre     = region.getCentre();
		double radius     = region.getDrawRadius();
		double rotation   = -Time.now * 0.5;
		double lerpFactor = Geo.hermite( (Time.now/600.0) % 1.0 );
		
		Triangle tri = new Triangle(
			centre.add( Geo.lenDir(radius, rotation+120.0) ),
			centre.add( Geo.lenDir(radius, rotation+240.0) ),
			centre.add( Geo.lenDir(radius, rotation+360.0) )
		);
		
		drawTriangleRecursive( g, tri, lerpFactor, 16 );
	}
	
	
	
	
	
	private static void drawTriangleRecursive( Graphics g, Triangle tri, double lerpFactor, int depth ) {
		if (depth < 0)
			return;
		
		drawTriangle( g, tri, false );
		
		Triangle newTri = new Triangle(
			Geo.lerp( tri.a, tri.b, lerpFactor ),
			Geo.lerp( tri.b, tri.c, lerpFactor ),
			Geo.lerp( tri.c, tri.a, lerpFactor )
		);
		
		drawTriangleRecursive( g, newTri, lerpFactor, --depth );
	}
	
	
	
	
	
	private static void drawTriangle( Graphics g, Triangle tri, boolean filled ) {
		Polygon poly = new Polygon();
		
		for (int i=0; i<3; i++)
			poly.addPoint( (int) tri.get(i).x, (int) tri.get(i).y );
		
		if (filled)
			 g.fillPolygon( poly );
		else g.drawPolygon( poly );
	}
}



































