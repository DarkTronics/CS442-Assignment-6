package assignment06MEM;

import assignment06Memento.Memento;

public class Move implements Command {
	int iccf; // from*100+to
	int undoICCF; // to*100+from
	Board board;
	public Move (Board brd, int i) {
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
		board.setPiece(to, p);
		return mem;
	}
}
