


/**
 * Denotes a specific type of shape for drawing.
 * @see ShapeGraphics, ShapePanel, ShapeButton
 */
public enum ShapeType {
	 triangle,
	 sinusoid,
	 barbell,
	 spiral,
	 fern,
	 none;
	 
	 
	 
	 
	 
	 public static ShapeType[] getDisplayableShapes() {
		 ShapeType[] shapes = {
			triangle,
			sinusoid,
			barbell,
			spiral,
			fern,
		};
		 
		 return shapes;
	 }
}