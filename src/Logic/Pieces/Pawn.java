package Logic.Pieces;

import Logic.Board;
import Logic.Movement.Move;

import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(boolean isWhite, int row, int col, int id, int value) {
        super(isWhite, row, col, id, value);
    }

    @Override
    public Piece copy() {
        return null;
    }

    @Override
    public String getName() {
        return "Pawn";
    }
    @Override
    public ArrayList<Move> getLegalMoves(Board board, int row, int col) {
        ArrayList<Move> legalMoves = new ArrayList<>();

        int direction = (isWhite()) ? -1 : 1;
        // Check if on edge of board to prevent null direction.
        if ((isWhite() && 0 >= row) || (!isWhite() && 7 <= row))
            direction = 0;

        if (board.getPieceAt(row + direction, col) == null){
            legalMoves.add(new Move(row, col, row + direction, col, this));

            if ((isWhite() && row == 6) || (!isWhite() && row == 1)){
                // pawn can move two squares forward
                if (board.getPieceAt(row + (2 * direction), col) == null){
                    legalMoves.add(new Move(row, col, row + (2 * direction), col, this));
                }
            }
        }

        // Check if the pawn can capture an opponent's piece diagonally.
        if (col > 0 && board.getPieceAt(row + direction, col-1) != null && board.getPieceAt(row + direction, col-1).isWhite() != isWhite())
            legalMoves.add(new Move(row, col, row + direction, col-1, this));

        if (col < 7 && board.getPieceAt(row + direction, col+1) != null && board.getPieceAt(row + direction, col+1).isWhite() != isWhite())
            legalMoves.add(new Move(row, col, row + direction, col+1, this));

        return legalMoves;
    }
}
