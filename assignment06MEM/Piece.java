package assignment06MEM;

import java.awt.Color;

public class Piece {
	static final Color DARK = new Color(192,142,60);
	static final Color LIGHT = new Color(203,203,197);
	
	private Color color;
	private String name; 
	private char code; 
	public Piece(Color colorIn, String nameIn, char codeIn) {
		color = colorIn;
		name = nameIn;
		code = codeIn;
	}
	public String getName() {
		return name;
	}
	public char getCode() {
		return code;
	}
	public Color getColor() {
		return color;
	}
	@Override
	public String toString() {
		return name;
	}	
}
