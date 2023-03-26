package Logic.Pieces;

import Logic.Board;
import Logic.Movement.Move;

import java.util.ArrayList;

public class King extends Piece {

    private boolean hasCastled = false;

    private int[] startingPosition;
    public King(boolean isWhite, int row, int col, int id, int value) {
        super(isWhite, row, col, id, value);
        startingPosition = new int[]{row, col};
    }

    @Override
    public Piece copy() {
        return null;
    }

    @Override
    public String getName() {
        return "King";
    }

    @Override
    public ArrayList<Move> getLegalMoves(Board board, int row, int col) {
        ArrayList<Move> legalMoves = new ArrayList<>();

        // Check for legal moves in all directions: up, down, left, right, and diagonals
        for (int newRow = row - 1; newRow <= row + 1; newRow++) {
            for (int newCol = col - 1; newCol <= col + 1; newCol++) {
                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    Move move = new Move(row, col, newRow, newCol, this);
                    if (board.getPieceAt(newRow, newCol) == null || board.getPieceAt(newRow, newCol).isWhite() != isWhite()) {
                        legalMoves.add(move);
                    }
                }
            }
        }

        legalMoves.addAll(checkCastlingMoves(board, row, col, legalMoves));


        return legalMoves;
    }

    private ArrayList<Move> checkCastlingMoves(Board board, int row, int col, ArrayList<Move> legalMoves){

        ArrayList<Move> castlingMoves = new ArrayList<>();

        if (hasMoved()) { // King has already moved, can't castle
            return new ArrayList<>();
        }

        if (startingPosition[0] == row && startingPosition[1] == col) {

            Piece[][] pieces = board.pieces;

            // Check castling with the kingside rook
            Piece kingsideRook = pieces[row][7];
            if (kingsideRook instanceof Rook && !kingsideRook.hasMoved()) {
                boolean isPathClear = true;
                for (int i = col + 1; i < 7; i++) {
                    if (pieces[row][i] != null) {
                        isPathClear = false;
                        break;
                    }
                }
                if (isPathClear && !board.inCheck(isWhite()) && !board.isAttacked(row, col + 1, isWhite()) && !board.isAttacked(row, col + 2, isWhite())) {
                    Move kingsideCastling = new Move(row, col, row, col + 2, this);
                    castlingMoves.add(kingsideCastling);
                }
            }

            // Check castling with the queenside rook
            Piece queensideRook = pieces[row][0];
            if (queensideRook instanceof Rook && !queensideRook.hasMoved()) {
                boolean isPathClear = true;
                for (int i = col - 1; i > 0; i--) {
                    if (pieces[row][i] != null) {
                        isPathClear = false;
                        break;
                    }
                }
                if (isPathClear && !board.inCheck(isWhite()) && !board.isAttacked(row, col - 1, isWhite()) && !board.isAttacked(row, col - 2, isWhite())) {
                    Move queensideCastling = new Move(row, col, row, col - 2, this);
                    castlingMoves.add(queensideCastling);
                }
            }
        }

        return castlingMoves;

    }
    public void setHasCastled(boolean hasCastled){
        this.hasCastled = hasCastled;
    }
    public boolean hasCastled(){
        return hasCastled;
    }
}
