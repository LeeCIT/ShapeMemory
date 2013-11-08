


import java.text.DecimalFormat;
import java.util.ArrayList;



/**
 * Implements the memory game logic.
 * Communication with the GUI is handled via callbacks.
 * @see Callback, CallbackParam
 */
public class GameLogic
{
	private long inputTimeReference;
	private long shapeDisplayTime;
	private long shapeIntervalTime;
	private int  shapeCount;
	
	private ArrayList<ShapeType> displayedSequence;
	
	private long scoreNow;
	private long scoreHigh;
	private int  userInputIndex;
	private int  lives;
	
	private CallbackParam<String> textFeedback;
	
	
	
	
	
	/**
	 * Construct the GameLogic.
	 */
	public GameLogic() {
		reset();
	}
	
	
	
	
	
	public void showRandomShapeSequence( ShapePanel panel, Callback onCompletion ) {
		runShapeSequenceThread( panel, onCompletion );
	}
	
	
	
	
	
	public void respondToShapeButton( ShapeType shape, Callback onInputComplete ) {
		boolean correct    = (shape == displayedSequence.get( userInputIndex ));
		boolean lastGuess  = (userInputIndex == displayedSequence.size() - 1);
		boolean firstGuess = (userInputIndex == 0);
		String  feedback   = "";
		
		
		if (firstGuess)
			inputTimeReference = System.nanoTime();
		
		
		if (correct) {
			userInputIndex++;
			feedback = "Right!  It was the " + shape.name() + ".";
		} else {
			if (lives > 0) {
				lives--;
				feedback = "Wrong.  You lost a life!  You have " + lives + " lives left.";
			} else {
				reset();
				feedback = "Wrong!  You have no more lives, it's game over.";
			}
		}
		
		
		if (lastGuess || ! correct) {
			userInputIndex = 0;
			
			if (correct) {
				String secs = getFormattedTimeDeltaInSeconds(inputTimeReference);
				feedback += "  You got " + shapeCount + " points!";
				feedback += "  It took you " + secs + " seconds.";
				scorePoints( shapeCount );
			}
			
			if (onInputComplete != null)
				onInputComplete.execute();
		}
		
		
		if ( ! lastGuess && correct) {
			int remaining = displayedSequence.size() - userInputIndex; 
			feedback += "  " + remaining + " more to go...";
		}
		
		
		textFeedback.execute( feedback );
	}
	
	
	
	
	
	public void setTextualFeedbackCallback( CallbackParam<String> callback ) {
		textFeedback = callback;
	}
	
	
	
	
	
	public long getScoreNow() {
		return scoreNow;
	}
	
	
	
	
	
	public long getScoreHigh() {
		return scoreHigh;
	}
	
	
	
	
	
	private String getFormattedTimeDeltaInSeconds( long ref ) {
		DecimalFormat format = new DecimalFormat( "0.00" );
		double delta = (double) (System.nanoTime() - ref) / 1000000000.0;
		return format.format( delta ); 
	}
	
	
	
	
	
	private void runShapeSequenceThread( final ShapePanel panel, final Callback onCompletion ) {
		new Thread() {
			public void run() {
				executeShapeSequence( panel, onCompletion );
			}
		}.start();
	}
	
	
	
	
	
	private void executeShapeSequence( ShapePanel panel, Callback onCompletion ) {
		displayedSequence.clear();
		
		for (int i=0; i<shapeCount; i++) {
			ShapeType shape = pickRandomShapeType();
			displayedSequence.add( shape );
			
			panel.setActiveShape( shape );
			Util.sleep( shapeDisplayTime );
			
			panel.setActiveShape( ShapeType.none );
			Util.sleep( shapeIntervalTime );
		}
		
		if (onCompletion != null)
			onCompletion.execute();
	}
	
	
	
	
	
	private ShapeType pickRandomShapeType() {
		ShapeType[] shapes = ShapeType.getDisplayableShapes();
		return shapes[ getRandomInt(shapes.length) ];
	}
	
	
	
	
	
	private int getRandomInt( int xrange ) {
		return (int) Math.floor( Math.random() * xrange );
	}
	
	
	
	
	
	private void reset() {
		displayedSequence = new ArrayList<ShapeType>();
		shapeCount	      = 2;
		shapeDisplayTime  = 1750;
		shapeIntervalTime = 200;
		scoreNow          = 0;
		userInputIndex    = 0;
		lives			  = 3;
	}
	
	
	
	
	
	private void increaseDifficulty() {
		shapeCount = Math.min( shapeCount+1, 5 );
		shapeDisplayTime = (long) (shapeDisplayTime * 0.9);
	}
	
	
	
	
	
	private void scorePoints( long amount ) {
		scoreNow += amount;
		scoreHigh = Math.max( scoreNow, scoreHigh );
		increaseDifficulty();
	}
}











































