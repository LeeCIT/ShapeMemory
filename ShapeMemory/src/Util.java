


/**
 * Generic utility methods.
 */
public class Util
{
	
	/**
	 * Sleep the calling thread.
	 * @param millis Time to sleep in milliseconds.
	 */
	public static void sleep( long millis ) {
		try {
			Thread.sleep( millis );
		}
		catch (InterruptedException ex) {
			Thread.interrupted();
		}
	}
}
