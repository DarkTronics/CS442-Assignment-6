package assignment06;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import assignment06MEM.ChessBoard;

// https://en.wikipedia.org/wiki/Algebraic_notation_(chess)
// https://en.wikipedia.org/wiki/ICCF_numeric_notation
public class Assignment6Tester {
	static JFrame frame = new JFrame("Chess Board");
	static Board board = new Board();
	static boolean showSteps = true;

	static void step(int n) throws InterruptedException {
		if (showSteps) Thread.sleep(1000);
		Command c = new Move(board, n);
		board.doNewCommand(c);
		if (showSteps) {
			frame.add(ChessBoard.drawGui(frame, board ));
			frame.validate();
		}
		printBoard();
	}

	static void take(int n) throws InterruptedException {
		if (showSteps) Thread.sleep(1000);
		Command c = new Capture(board, n);
		board.doNewCommand(c);
		if (showSteps) {
			frame.add(ChessBoard.drawGui(frame, board ));
			frame.validate();
		}
		printBoard();
	}

	static void undo() throws InterruptedException {
		if (showSteps) Thread.sleep(1000);
		board.undoCommand();
		if (showSteps) {
			frame.add(ChessBoard.drawGui(frame, board ));
			frame.validate();
		}
		printBoard();		
	}
	
	static void redo() throws InterruptedException {
		if (showSteps) Thread.sleep(1000);
		board.redoCommand();
		if (showSteps) {
			frame.add(ChessBoard.drawGui(frame, board ));
			frame.validate();
		}
		printBoard();		
	}

	public static void main(String[] args) {		
		Runnable r = new Runnable() {
			@Override
			public void run() {				
				JPanel controls = new JPanel(new GridLayout(0, 2, 4, 4));
				JButton undoButton = new JButton("Undo");
				undoButton .setFont(new Font("SansSerif", Font.BOLD, 24));
				controls.add(undoButton);
				JButton redoButton = new JButton("Redo");
				redoButton .setFont(new Font("SansSerif", Font.BOLD, 24));
				controls.add(redoButton);
				frame.add(controls, BorderLayout.PAGE_END);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(ChessBoard.drawGui(frame, board));
				frame.setSize(600, 750);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		};
		SwingUtilities.invokeLater(r);
		try {
			printBoard();
			showSteps = false;
			step(7163);
			step(7866);
			step(3234);
			step(7776);
			step(2133);
			step(6877);
			step(4244);
			step(8868);
			step(5878);
			step(3164);
			step(4745);
			step(4123);
			take(4534);
			take(2334);
			step(3736);
			System.out.println("START UNDOING");
			for(int i = 0; i < 20; i++) undo();
			System.out.println("START REDOING");
			for(int i = 0; i < 20; i++) redo();
			step(5254);
			step(2847);
			step(1141);
			step(4726);
			step(3435);
			System.out.println("START UNDOING");
			for(int i = 0; i < 6; i++) undo();
			//showSteps = true;
			step(8785);
			take(6437);
			step(4847);
			System.out.println("START UNDOING");
			for(int i = 0; i < 10; i++) undo();
			System.out.println("START REDOING");
			for(int i = 0; i < 10; i++) redo();
			frame.add(ChessBoard.drawGui(frame, board ));
			frame.validate();		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void printBoard() {
		for(int i = 1; i <= 8; i++) {
			for(int j = 1; j <= 8; j++) {
				Piece p = board.getPiece(10*j+i);
				System.out.print(p + ",");
			}
		}
		System.out.println();
	}
}
