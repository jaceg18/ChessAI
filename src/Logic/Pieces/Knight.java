package Logic.Pieces;

import Logic.Board;
import Logic.Movement.Move;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(boolean isWhite, int row, int col, int id, int value) {
        super(isWhite, row, col, id, value);
    }

    @Override
    public Piece copy() {
        return null;
    }

    @Override
    public String getName() {
        return "Knight";
    }

    @Override
    public ArrayList<Move> getLegalMoves(Board board, int row, int col) {
        ArrayList<Move> legalMoves = new ArrayList<>();
        int[][] offsets = {{-1,-2}, {-2,-1}, {-2,1}, {-1,2}, {1,-2}, {2,-1}, {2,1}, {1,2}};

        for (int[] offset : offsets){
            int newRow = row + offset[0];
            int newCol = col + offset[1];

            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8){
                Piece destPiece = board.getPieceAt(newRow, newCol);

                // If the destination square is empty or contains an opposing pieces, add the move
                if (destPiece == null || destPiece.isWhite() != isWhite())
                    legalMoves.add(new Move(row, col, newRow, newCol, this));
            }
        }
        return legalMoves;
    }


}
