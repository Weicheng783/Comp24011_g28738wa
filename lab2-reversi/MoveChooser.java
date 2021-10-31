import java.util.ArrayList;  

public class MoveChooser {
  
    public static Move chooseMove(BoardState boardState){

	int searchDepth= Othello.searchDepth;

        ArrayList<Move> moves= boardState.getLegalMoves();
        if(moves.isEmpty()){
            return null;
	}
        return moves.get(0);
    }
}
