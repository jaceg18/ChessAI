package Engine.Openings;

import Logic.Board;
import Logic.Movement.Move;

public class Opening {

    private final Move move;
    private Board board;
    public Opening(Move move, Board board){
        this.move = move;
        this.board = board;
    }
    public Move getMove(){
        return move;
    }


}
