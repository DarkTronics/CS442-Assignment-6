package assignment06Memento;
  
import assignment06MEM.Board;

public class Originator {
	public Memento getMemento(Board board, int iccf) {
		return new MoveMemento(board, iccf);
	}	
	
	public void reset (Board board, Memento mem) {
		MoveMemento mmem = (MoveMemento)mem;
		board.setPiece(GET THE FROM LOCATION* from mmem, GET THE FROM PIECE from mmem);
		board.setPiece(GET THE TO LOCATION* from mmem, GET THE TO PIECE from mmem);
		*NOTE the LOCATION will be 2 digits using the ICCF notation--the LOCATIONS
		are split from the iccf in mmem using from = iccf/100 and to = iccf%100
	}
}
