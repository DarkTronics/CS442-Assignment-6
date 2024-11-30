package assignment06;

import java.awt.Color;

public enum ChessColor {
	BLACK(Color.BLACK), WHITE(Color.WHITE), CLICKED(Color.CYAN), NONE(Color.RED);
	Color color;
	ChessColor(Color c) {
		color = c;
	}
	public Color color() {
		return color;
	}
	public String toString() {
		return name().substring(0, 1);
	}
}
