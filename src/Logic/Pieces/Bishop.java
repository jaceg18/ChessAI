package Logic.Pieces;

import Logic.Board;
import Logic.Movement.Move;

import java.util.ArrayList;

public class Bishop extends Piece {

    private final int[][] DIR_OFFSETS = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    public Bishop(boolean isWhite, int row, int col, int id, int value) {
        super(isWhite, row, col, id, value);
    }

    @Override
    public Piece copy() {
        return null;
    }

    @Override
    public String getName() {
        return "Bishop";
    }

    @Override
    public ArrayList<Move> getLegalMoves(Board board, int row, int col) {
        ArrayList<Move> legalMoves = new ArrayList<>();

        // Check diagonals
        for (int dir=0; dir < 4; dir++){
            int newRow = row;
            int newCol = col;
            while (true){
                newRow += DIR_OFFSETS[dir][0];
                newCol += DIR_OFFSETS[dir][1];
                if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8)
                    break; // Destination square is off the board

                Piece destPiece = board.getPieceAt(newRow, newCol);
                if (destPiece == null){
                    legalMoves.add(new Move(row, col, newRow, newCol, this));
                } else {
                    if (destPiece.isWhite() != isWhite()){
                        legalMoves.add(new Move(row, col, newRow, newCol, this));
                    }
                    break; // Can't jump over pieces
                }
            }
        }
        return legalMoves;
    }
}
