


import java.awt.*;
import javax.swing.*;



/**
 * A button which draws a shape.
 * @see ShapeType, ShapeGraphics 
 */
public class ShapeButton extends JButton
{
	private ShapeType shape;
	
	
	
	
	
	public ShapeButton( ShapeType shape ) {
		super();
		this.shape = shape;
	}
	
	
	
	
	
	public void paint( Graphics g ) {
		super.paint( g );
		Region r = new Region( this );
		ShapeGraphics.draw( g, r, shape );
	}
	
	
	
	
	
	public ShapeType getShape() {
		return shape;
	}
}
