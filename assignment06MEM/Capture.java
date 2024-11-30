package assignment06MEM;

import assignment06Memento.Memento;

public class Capture implements Command {
	int iccf;
	int undoICCF;
	Piece lost;
	Board board;
	public Capture (Board brd, int i) {
		iccf = i;
		board = brd;
		undoICCF = 100*(iccf % 100) + iccf/100;
	}

	@Override
	public Memento execute() {
		int from = iccf/100;
		int to = iccf%100;
		Memento mem = board.originator.getMemento(board, iccf);
		Piece p = board.getPiece(from);
		board.setPiece(from, board.piece.get("--"));
		lost = board.getPiece(to);
		board.setPiece(to, p);
		return mem;
	}
}
