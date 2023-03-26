package Logic.Pieces;

import Logic.Board;
import Logic.Movement.Move;

import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(boolean isWhite, int row, int col, int id, int value) {
        super(isWhite, row, col, id, value);
    }

    @Override
    public Piece copy() {
        return null;
    }

    @Override
    public String getName() {
        return "Rook";
    }

    @Override
    public ArrayList<Move> getLegalMoves(Board board, int row, int col) {
        ArrayList<Move> legalMoves = new ArrayList<>();

        // Move up
        for (int r = row - 1; r >= 0; r--) {
            if (board.getPieceAt(r, col) == null) {
                legalMoves.add(new Move(row, col, r, col, this));
            } else {
                if (board.getPieceAt(r, col).isWhite() != isWhite()) {
                    legalMoves.add(new Move(row, col, r, col, this));
                }
                break;
            }
        }

        // Move down
        for (int r = row + 1; r < 8; r++) {
            if (board.getPieceAt(r, col) == null) {
                legalMoves.add(new Move(row, col, r, col, this));
            } else {
                if (board.getPieceAt(r, col).isWhite() != isWhite()) {
                    legalMoves.add(new Move(row, col, r, col, this));
                }
                break;
            }
        }

        // Move left
        for (int c = col - 1; c >= 0; c--) {
            if (board.getPieceAt(row, c) == null) {
                legalMoves.add(new Move(row, col, row, c, this));
            } else {
                if (board.getPieceAt(row, c).isWhite() != isWhite()) {
                    legalMoves.add(new Move(row, col, row, c, this));
                }
                break;
            }
        }

        // Move right
        for (int c = col + 1; c < 8; c++) {
            if (board.getPieceAt(row, c) == null) {
                legalMoves.add(new Move(row, col, row, c, this));
            } else {
                if (board.getPieceAt(row, c).isWhite() != isWhite()) {
                    legalMoves.add(new Move(row, col, row, c, this));
                }
                break;
            }
        }

        return legalMoves;
    }


}
