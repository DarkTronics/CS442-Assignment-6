package assignment06Memento;

import assignment06MEM.Board;
import assignment06MEM.Piece;

class MoveMemento implements Memento {
	int iccf;
	String fromPieceName;
	String toPieceName;
	Board board;
	MoveMemento(Board boardIn, int iccfIn) {
		board = boardIn;
		iccf = iccfIn;
		fromPieceName = board.getPiece(iccf/100).getName();
		toPieceName = board.getPiece(iccf%100).getName();
	}
	int getIccf() {
		return iccf;
	}
	Piece getFromPiece() {
		return board.piece.get(fromPieceName);
	}
	Piece getToPiece() {
		return board.piece.get(toPieceName);
	}
}
