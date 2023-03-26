package Logic.Pieces;

import Logic.Board;
import Logic.Movement.Move;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(boolean isWhite, int row, int col, int id, int value) {
        super(isWhite, row, col, id, value);
    }

    @Override
    public Piece copy() {
        return null;
    }

    @Override
    public String getName() {
        return "Queen";
    }

    @Override
    public ArrayList<Move> getLegalMoves(Board board, int row, int col) {
        ArrayList<Move> legalMoves = new ArrayList<>();

        // Check for legal moves in all directions: up, down, left, right, and diagonally
        legalMoves.addAll(getLegalMovesInDirection(board, row, col, -1, 0)); // up
        legalMoves.addAll(getLegalMovesInDirection(board, row, col, 1, 0)); // down
        legalMoves.addAll(getLegalMovesInDirection(board, row, col, 0, -1)); // left
        legalMoves.addAll(getLegalMovesInDirection(board, row, col, 0, 1)); // right
        legalMoves.addAll(getLegalMovesInDirection(board, row, col, -1, -1)); // up-left
        legalMoves.addAll(getLegalMovesInDirection(board, row, col, -1, 1)); // up-right
        legalMoves.addAll(getLegalMovesInDirection(board, row, col, 1, -1)); // down-left
        legalMoves.addAll(getLegalMovesInDirection(board, row, col, 1, 1)); // down-right



        return legalMoves;
    }

    // Helper method to get legal moves in a given direction
    private ArrayList<Move> getLegalMovesInDirection(Board board, int row, int col, int rowIncrement, int colIncrement){
        ArrayList<Move> legalMoves = new ArrayList<>();
        int newRow = row + rowIncrement;
        int newCol = col + colIncrement;
        while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
            if (board.getPieceAt(newRow, newCol) == null) {
                legalMoves.add(new Move(row, col, newRow, newCol, this));
            } else {
                Piece pieceAtNewPos = board.getPieceAt(newRow, newCol);
                if (pieceAtNewPos.isWhite() != isWhite()) {
                    legalMoves.add(new Move(row, col, newRow, newCol, this));
                }
                break;
            }
            newRow += rowIncrement;
            newCol += colIncrement;
        }
        return legalMoves;
    }
}
