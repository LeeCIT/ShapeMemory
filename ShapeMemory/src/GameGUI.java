


import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;



/**
 * The game GUI panel.  Interfaces with GameLogic.
 * @see GameLogic
 */
public class GameGUI extends JPanel
{
	private JLabel      scoreNowLabel;
	private JLabel      scoreHighLabel;
	private ShapePanel  shapePanel;
	private JButton     buttStart;
	
	private ShapeButton   buttTriangle;
	private ShapeButton   buttSinusoid;
	private ShapeButton   buttBarbell; 
	private ShapeButton   buttSpiral;  
	private ShapeButton   buttFern;
	private ShapeButton[] shapeButtons;
	
	private JTextField fieldStatus;
	
	private GameLogic game;
	
	
	
	
	
	public GameGUI( GameLogic game ) {
		super();
		
		this.game = game;
		
		setupComponents();
		setupLayout();
		setupActions();
	}
	
	
	
	
	
	public void setFeedbackText( String str ) {
		fieldStatus.setText( str );
	}
	
	
	
	
	
	private void setupComponents() {
		scoreNowLabel  = new JLabel( "Score: 0" );
		scoreHighLabel = new JLabel( "Best: 0" );
		
		shapePanel = new ShapePanel();
		buttStart  = new JButton( "Start" );
		
		buttTriangle = new ShapeButton( ShapeType.triangle );
		buttSinusoid = new ShapeButton( ShapeType.sinusoid );
		buttBarbell  = new ShapeButton( ShapeType.barbell  );
		buttSpiral   = new ShapeButton( ShapeType.spiral   );
		buttFern     = new ShapeButton( ShapeType.fern     );
		
		shapeButtons = new ShapeButton[] {
			buttTriangle,
			buttSinusoid,
			buttBarbell,
			buttSpiral,
			buttFern
		};
		
		fieldStatus = new JTextField( "Press start button to begin." );
	}
	
	
	
	
	
	private void setupLayout() {
		setLayout( new MigLayout( "", "[grow][grow]", "[][grow][][grow][]" ) );
		
		add( scoreNowLabel,  "align left" );
		add( scoreHighLabel, "align right, wrap" );
		
		add( shapePanel, "span 2, grow, wrap, wmin 480px, hmin 360px" );
		
		add( buttStart, "span 2, align 50%, wmin 25%, wrap" );
		
		String buttonParams = "growx, growy, wmin 96px, hmin 96px";
		add( buttTriangle, buttonParams + ", span 2, split 5, align 50%" );
		add( buttSinusoid, buttonParams );
		add( buttBarbell , buttonParams );
		add( buttSpiral  , buttonParams );
		add( buttFern    , buttonParams + ", wrap" );
		
		add( fieldStatus, "span 2, growx" );
	}
	
	
	
	
	
	private void setupActions() {
		setShapeButtonsEnabled( false );
		fieldStatus.setEditable( false );
		
		
		
		// Textual feedback callback
		game.setTextualFeedbackCallback( new CallbackParam<String>() {
			public void execute( String str ) {
				setFeedbackText( str );
			}
		});
		
		
		
		// Start button end-of-sequence callback
		final Callback onDisplaySequenceComplete = new Callback() {
			public void execute() {
				setFeedbackText( "Press shape buttons to continue." );
				setShapeButtonsEnabled( true );
			}
		};
		
		
		
		// Start button
		buttStart.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				setFeedbackText( "Showing shape sequence..." );
				buttStart.setEnabled( false );
				setShapeButtonsEnabled( false );
				game.showRandomShapeSequence( shapePanel, onDisplaySequenceComplete );
			}
		});
		
		
		
		// Shape button sequence end callback
		final Callback onUserInputSequenceComplete = new Callback() {
			public void execute() {
				setShapeButtonsEnabled( false );
				buttStart.setEnabled( true );
			}
		};
		
		
		
		// Shape buttons
		for (final ShapeButton butt: shapeButtons) {
			butt.addActionListener( new ActionListener() {
				public void actionPerformed( ActionEvent ev ) {
					game.respondToShapeButton( butt.getShape(), onUserInputSequenceComplete );
					scoreNowLabel .setText( "Score: " + game.getScoreNow()  );
					scoreHighLabel.setText( "Best:  " + game.getScoreHigh() );
				}
			});
		}
		
		
		
		// Status field
		fieldStatus.addMouseListener( new MouseAdapter() {
			public void mouseClicked( MouseEvent e ) {
				fieldStatus.setText( "There's no point in clicking this..." );
			}
		});
	}
	
	
	
	
	
	private void setShapeButtonsEnabled( boolean state ) {
		for (ShapeButton b: shapeButtons)
			b.setEnabled( state );
	}
}





























