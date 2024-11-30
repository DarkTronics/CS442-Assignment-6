package assignment06;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Some Java & Wikipedia chess resources:
// https://stackoverflow.com/questions/18686199/fill-unicode-characters-in-labels
// https://www.youtube.com/watch?v=vO7wHV0HB8w
// https://www.youtube.com/watch?v=LivX1XKpSQA
// https://www.youtube.com/watch?v=U4ogK0MIzqk
// https://github.com/javachessgui/javachessgui
// https://codereview.stackexchange.com/questions/71790/design-a-chess-game-using-object-oriented-principles
// https://github.com/jaeheonshim/ChessBoard
// https://en.wikipedia.org/wiki/Algebraic_notation_(chess)
// https://en.wikipedia.org/wiki/Correspondence_chess

class ChessBoard {

	static Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 50);
	static Random rnd = new Random();
	static boolean fromClick = true;
	static int fromICCF;
	static int toICCF;
	static Color oldColor;

	// https://stackoverflow.com/questions/18686199/fill-unicode-characters-in-labels
	public static ArrayList<Shape> separateShapeIntoRegions(Shape shape) {
		ArrayList<Shape> regions = new ArrayList<Shape>();
		PathIterator pi = shape.getPathIterator(null);
		GeneralPath gp = new GeneralPath();
		while (!pi.isDone()) {
			double[] coords = new double[6];
			int pathSegmentType = pi.currentSegment(coords);
			int windingRule = pi.getWindingRule();
			gp.setWindingRule(windingRule);
			if (pathSegmentType == PathIterator.SEG_MOVETO) {
				gp = new GeneralPath();
				gp.setWindingRule(windingRule);
				gp.moveTo(coords[0], coords[1]);
			} else if (pathSegmentType == PathIterator.SEG_LINETO) {
				gp.lineTo(coords[0], coords[1]);
			} else if (pathSegmentType == PathIterator.SEG_QUADTO) {
				gp.quadTo(coords[0], coords[1], coords[2], coords[3]);
			} else if (pathSegmentType == PathIterator.SEG_CUBICTO) {
				gp.curveTo(
						coords[0], coords[1],
						coords[2], coords[3],
						coords[4], coords[5]);
			} else if (pathSegmentType == PathIterator.SEG_CLOSE) {
				gp.closePath();
				regions.add(new Area(gp));
			} else {
				System.err.println("Unexpected value! " + pathSegmentType);
			}
			pi.next();
		}
		return regions;
	}

	// https://stackoverflow.com/questions/18686199/fill-unicode-characters-in-labels
	public static void addColoredUnicodeChar(
			char ch, Container c,
			Color bgColor, Color outlineColor,
			JFrame frame, Board board, int file, int rank) {
		String s = "" + ch;
		int sz = font.getSize();
		BufferedImage bi = new BufferedImage(
				sz, sz, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(
				RenderingHints.KEY_DITHERING,
				RenderingHints.VALUE_DITHER_ENABLE);
		g.setRenderingHint(
				RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector gv = font.createGlyphVector(frc, s);

		Shape shape1 = gv.getOutline();
		Rectangle r = shape1.getBounds();
		int spaceX = sz - r.width;
		int spaceY = sz - r.height;
		AffineTransform trans = AffineTransform.getTranslateInstance(
				-r.x + (spaceX / 2), -r.y + (spaceY / 2));

		Shape shapeCentered = trans.createTransformedShape(shape1);

		Shape imageShape = new Rectangle2D.Double(0, 0, sz, sz);
		Area imageShapeArea = new Area(imageShape);
		Area shapeArea = new Area(shapeCentered);
		imageShapeArea.subtract(shapeArea);
		ArrayList<Shape> regions = separateShapeIntoRegions(imageShapeArea);
		g.setStroke(new BasicStroke(1));
		for (Shape region : regions) {
			Rectangle r1 = region.getBounds();
			if (r1.getX() < 0.001 && r1.getY() < 0.001) {
			} else {
				g.setColor(bgColor);
				g.fill(region);
			}
		}
		g.setColor(outlineColor);
		g.fill(shapeArea);
		g.dispose();

		JLabel l = new JLabel(new ImageIcon(bi), JLabel.CENTER);
		Color bg = board.getColor(file*10+rank);
		l.setBackground(bg);
		l.setOpaque(true);
		c.add(l);
		l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(fromClick) {
					fromICCF = 10*file+rank;
					oldColor = board.getColor(fromICCF);
					board.setColor(fromICCF, Color.CYAN);
					frame.add(drawGui(frame,board ));
					frame.validate();
				} else {
					board.setColor(fromICCF, oldColor);
					toICCF = 10*file+rank;
					Command c = new Move(board, 100*fromICCF + toICCF);
					if(board.getPiece(toICCF).getColor() != null)
						c = new Capture(board, 100*fromICCF + toICCF);
					board.doNewCommand(c);
					frame.add(drawGui(frame,board ));
					frame.validate();
				}
				fromClick = !fromClick;
			}
		});
	}

	public static void addLetterRowToContainer(
			char s, Container c) {
		JLabel l = new JLabel(""+s, JLabel.CENTER);
		l.setOpaque(true);
		l.setBackground(Color.WHITE);
		l.setForeground(Color.BLACK);
		l.setFont(new Font("SansSerif", Font.PLAIN, 24));
		c.add(l);
	}
	
	public static JPanel drawGui(JFrame frame, Board board) {
		JPanel gui = new JPanel(new GridLayout(0, 10, 4, 4));
		for (var letter : List.of(' ','a','b','c','d','e','f','g','h',' ')) {
			addLetterRowToContainer(
					letter, gui);
		}				
		for (var letter : List.of(' ','1','2','3','4','5','6','7','8',' ')) {
			addLetterRowToContainer(
					letter, gui);
		}				
		for (int r = 8; r >= 1; r--) {
			addLetterRowToContainer((char)('0'+r), gui);
			for (int f = 1; f <= 8; f++) {
				Piece piece = board.getPiece(10*f+r);
				if (piece.getColor() == null) {
					addColoredUnicodeChar(
							piece.getCode(), gui, Piece.LIGHT,
							Color.DARK_GRAY, frame, board, f, r);
				} else {
					if(piece.getColor() == Color.WHITE)
						addColoredUnicodeChar(piece.getCode(), gui, Piece.LIGHT, 
								Color.DARK_GRAY, frame, board, f, r);
					else 
						addColoredUnicodeChar(piece.getCode(), gui, Piece.DARK, 
								Color.DARK_GRAY, frame, board, f, r);
				}
			}
			addLetterRowToContainer((char)('0'+r), gui);

		}
		for (var letter : List.of(' ','1','2','3','4','5','6','7','8',' ')) {
			addLetterRowToContainer(
					letter, gui);
		}				
		for (var letter : List.of(' ','a','b','c','d','e','f','g','h',' ')) {
			addLetterRowToContainer(
					letter, gui);
		}				
		return gui;
	}
	public static void main(String[] args) {
		Runnable r = new Runnable() {
			@Override
			public void run() {				
				JFrame frame = new JFrame("Chess Board");
				Board board = new Board();
				JPanel controls = new JPanel(new GridLayout(0, 2, 4, 4));
				JButton undoButton = new JButton("Undo");
				undoButton .setFont(new Font("SansSerif", Font.BOLD, 24));
				controls.add(undoButton);
				undoButton.addActionListener(e -> {					
					board.undoCommand();
					frame.add(drawGui(frame,board ));
					frame.validate();
				});
				JButton redoButton = new JButton("Redo");
				redoButton .setFont(new Font("SansSerif", Font.BOLD, 24));
				controls.add(redoButton);
				redoButton.addActionListener(e -> {
					board.redoCommand();
					frame.add(drawGui(frame,board ));
					frame.validate();
				});
				frame.add(controls, BorderLayout.PAGE_END);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(drawGui(frame, board));
				frame.setSize(600, 750);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				KeyEventDispatcher myKeyEventDispatcher = new MyDefaultFocusManager(board, frame);
				KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(myKeyEventDispatcher);
			}
		};
		SwingUtilities.invokeLater(r);
	}
	static class MyDefaultFocusManager extends DefaultKeyboardFocusManager {
		Board board;	
		JFrame frame;
		public MyDefaultFocusManager(Board boardIn, JFrame frameIn) {
			board = boardIn;
			frame = frameIn;
		}
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			int ctrlMask = KeyEvent.CTRL_DOWN_MASK;
			if((e.getModifiersEx() & ctrlMask) == ctrlMask) {
				if(e.getID() == KeyEvent.KEY_RELEASED) {
					if(e.getKeyCode() == KeyEvent.VK_Z) {
						board.undoCommand();
						frame.add(drawGui(frame,board ));
						frame.validate();					
					}
					if(e.getKeyCode() == KeyEvent.VK_Y) {
						board.redoCommand();
						frame.add(drawGui(frame,board ));
						frame.validate();					
					}
				} 
			}
			return true;
		}
	}
}