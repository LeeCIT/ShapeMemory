


import javax.swing.*;



/**
 * The applet.  It's simply used a container for the GUI class.
 * @see Main
 */
public class ShapeApplet extends JApplet
{
	public void init() {
		Main.createInContainer( this );
	}
}
