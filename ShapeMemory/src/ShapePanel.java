


import java.awt.*;
import javax.swing.*;



/**
 * A panel shapes are drawn inside.
 * It displays the name of the currently displayed shape above it.
 * @see ShapeType
 */
public class ShapePanel extends JPanel
{
	private ShapeType activeShape = ShapeType.none;
	
	
	
	
	
	public void paint( Graphics g ) {
		super.paint( g );
		Region r = new Region( this );
		ShapeGraphics.draw( g, r, activeShape );
		
		if (activeShape != ShapeType.none) {
			Font   fontPrev = g.getFont();
			Font   font     = new Font( "Arial", Font.BOLD, 16 );
			String str      = activeShape.toString();
			int    width    = (int) g.getFontMetrics(font).getStringBounds(str,g).getWidth();
			int    xpos     = getX() + (getWidth()/2) - (width/2);
			int    ypos	    = getY() - 8;
			
			g.setFont( font );
			g.drawString( str, xpos, ypos );
			g.setFont( fontPrev );
		}
	}
	
	
	
	
	
	public void setActiveShape( ShapeType activeShape ) {
		this.activeShape = activeShape;
	}
}



































