package assignment06MEM;

import java.awt.Color;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import assignment06.TODO;
import assignment06.by;
import assignment06.command;
import assignment06.get;
import assignment06.is;
import assignment06.no;
import assignment06.size;
import assignment06.the;
import assignment06.then;
import assignment06.there;
import assignment06Memento.Memento;
import assignment06Memento.Originator;

public class Board {
	private Piece[][] board = new Piece[8][8];
	private Color[][] boardColors = new Color[8][8];
	private Stack<Command> executedStack = new Stack<>();
	private Stack<Command> undoneStack = new Stack<>();
	private Stack<Memento> mementoStack = new Stack<>(); // Note the Memento stack
	public Originator originator; // Note the Originator object
	
	public Map<String, Piece> piece = new TreeMap<>();
	
	public Board() {
		originator = new Originator();
		makePieces();
		for(byte i = 0; i < 8; i++) { // columns=files
			for(byte j = 0; j < 8; j++) { // rows=rank
				board[i][j] = piece.get("--");
				if((i+j)%2 == 0) 
					boardColors[i][j] = Color.BLACK;
				else
					boardColors[i][j] = Color.WHITE;
			}
		}
		initializeBoard();
	}
	public Piece getPiece(int code) { //57
		int file = (code/10);
		int rank = (code % 10);
		return board[file-1][rank-1];
	}
	public Color getColor(int code) { //57
		int file = (code/10);
		int rank = (code % 10);
		return boardColors[file-1][rank-1];
	}
	public void setPiece(int rank, int file, Piece p) { 
		board[file-1][rank-1] = p;
	}
	public void setPiece(int code, Piece p) {
		setPiece(code % 10, code/10, p);
	}
	public void setColor(int code, Color col) { //57
		int file = (code/10);
		int rank = (code % 10);
		boardColors[file-1][rank-1] = col;
	}

	public void makePieces () {
		piece.put("--", new Piece(null, "--", ' ')); // a non-piece used for blank squares
		Color b = Color.BLACK;
		Color w = Color.WHITE;
		piece.put("WR", new Piece(w, "WR", '\u2656'));
		piece.put("WN", new Piece(w, "WN", '\u2658'));
		piece.put("WB", new Piece(w, "WB", '\u2657'));
		piece.put("WQ", new Piece(w, "WQ", '\u2655'));
		piece.put("WK", new Piece(w, "WK", '\u2654'));
		piece.put("WP", new Piece(w, "WP", '\u2659'));
		piece.put("BR", new Piece(b, "BR", '\u2656'));
		piece.put("BN", new Piece(b, "BN", '\u2658'));
		piece.put("BB", new Piece(b, "BB", '\u2657'));
		piece.put("BQ", new Piece(b, "BQ", '\u2655'));
		piece.put("BK", new Piece(b, "BK", '\u2654'));
		piece.put("BP", new Piece(b, "BP", '\u2659'));
	}	

	public void initializeBoard() {
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				board[i][j] = piece.get("--");
		board[0][0] = piece.get("WR");
		board[1][0] = piece.get("WN");
		board[2][0] = piece.get("WB");
		board[3][0] = piece.get("WQ");
		board[4][0] = piece.get("WK");
		board[5][0] = piece.get("WB");
		board[6][0] = piece.get("WN");
		board[7][0] = piece.get("WR");
		for(int j = 0; j < 8; j++)
			board[j][1] = piece.get("WP");
		for(int j = 0; j < 8; j++)
			board[j][6] = piece.get("BP");
		board[0][7] = piece.get("BR");
		board[1][7] = piece.get("BN");
		board[2][7] = piece.get("BB");
		board[3][7] = piece.get("BQ");
		board[4][7] = piece.get("BK");
		board[5][7] = piece.get("BB");
		board[6][7] = piece.get("BN");
		board[7][7] = piece.get("BR");
	}

	public void doNewCommand(Command cm) {
		TODO
		clear the undone stack
		push the command on to the executed stack
		get the Memento object returned by executing the command
		push that Memento object onto the memento stack
		(Note the last 2 lines can be combined into one instruction)
	}
	
	public void undoCommand() {
		TODO
		if the executed stack size is positive
		get the command by popping the executed stack, then
		get the memento "mem" by popping the memento stack, then
		push this command on to the undone stack, then
		call the reset method of originator with arguments (this, mem) 
		(there is no code if the executed stack is empty)
	}
	public void redoCommand() {
		TODO
		if the undone stack size is positive
		get the command by popping the stack, then
		push this command on to the executed stack, then
		get the Memento object returned by executing the command
		push that Memento object onto the memento stack
		(Note the last 2 lines can be combined into one instruction)
		(there is no code if the executed stack is empty)
	}
}
