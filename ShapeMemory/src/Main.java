


import java.awt.*;



/**
 * Creates a background thread, GUI and GameLogic. 
 * If the program is run from here the game is created in a JFrame instead of a JApplet.
 */
public class Main
{	
	public static void main( String[] args ) {
		createInContainer( new ShapeFrame() );
	}
	
	
	
	
	
	/**
	 * Create the whole game in the specified GUI container.
	 * @param container Any Container derived instance, such as a JFrame or JApplet.
	 */
	public static void createInContainer( final Container container ) {
		final long redrawInterval = 1000 / 72;
		
		final Dimension size    = new Dimension( 524, 600 );
		final GameLogic game    = new GameLogic();
		final GameGUI   gameGUI = new GameGUI( game );
		
		container.add( gameGUI );
		container.setSize( size );
		container.setMinimumSize( size );
		
		new CallbackThread( redrawInterval, new Callback() {
			public void execute() {
				container.repaint();
				Time.now++;
			}
		});
	}
}











































