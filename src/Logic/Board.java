package Logic;
import Engine.AI;
import Logic.Movement.Move;
import Logic.Pieces.*;

import java.util.ArrayList;

public class Board {

    public AI ai;

    public Piece[][] pieces;

    public Board() {
        this.pieces = new Piece[8][8];

        pieces[0][0] = new Rook(false, 0, 0, 1, 500);
        pieces[0][1] = new Knight(false, 0, 1, 2, 300);
        pieces[0][2] = new Bishop(false, 0, 2, 3, 325);
        pieces[0][3] = new Queen(false, 0, 3, 4, 900);
        pieces[0][4] = new King(false, 0, 4, 5, 0);
        pieces[0][5] = new Bishop(false, 0, 5, 3, 300);
        pieces[0][6] = new Knight(false, 0, 6, 2, 300);
        pieces[0][7] = new Rook(false, 0, 7, 1, 500);
        for (int i = 0; i < 8; i++) {
            pieces[1][i] = new Pawn(false, 1, i, 6, 100);
        }

        // initialize white pieces
        pieces[7][0] = new Rook(true, 7, 0, 1, 500);
        pieces[7][1] = new Knight(true, 7, 1, 2, 300);
        pieces[7][2] = new Bishop(true, 7, 2, 3, 325);
        pieces[7][3] = new Queen(true, 7, 3, 4, 900);
        pieces[7][4] = new King(true, 7, 4, 5, 0);
        pieces[7][5] = new Bishop(true, 7, 5, 3, 300);
        pieces[7][6] = new Knight(true, 7, 6, 2, 300);
        pieces[7][7] = new Rook(true, 7, 7, 1, 500);
        for (int i = 0; i < 8; i++) {
            pieces[6][i] = new Pawn(true, 6, i, 6, 100);
        }

        ai = new AI(this);

    }

    public ArrayList<Move> getPieceLegalMove(Piece piece) {
        // Removes moves that lead to or don't prevent a check.
        ArrayList<Move> legalMoves = piece.getLegalMoves(this, piece.getRow(), piece.getCol());
        ArrayList<Move> illegalMoves = new ArrayList<>();
        for (Move move : legalMoves) {
            if (isCastleMove(move)){
                if (!((King) move.getPiece()).hasCastled()){
                    makeMove(move);
                    if (inCheck(piece.isWhite()))
                        illegalMoves.add(move);
                    unmakeMove(move);
                }
            } else {
                makeMove(move);
                if (inCheck(piece.isWhite()))
                    illegalMoves.add(move);
                unmakeMove(move);
            }
        }
        legalMoves.removeAll(illegalMoves);
        return legalMoves;
    }

    public ArrayList<Move> getTeamLegalMove(boolean white) {
        // Removes moves that lead to or don't prevent a check.
        ArrayList<Piece> teamPieces = getTeamPieces(white);
        ArrayList<Move> teamLegalMoves = new ArrayList<>();
        for (Piece piece : teamPieces)
            teamLegalMoves.addAll(getPieceLegalMove(piece));

        return teamLegalMoves;
    }
    public ArrayList<Move> getTeamCaptureMoves(boolean white){
        ArrayList<Move> legalMoves = getTeamLegalMove(white);
        ArrayList<Move> captureMoves = new ArrayList<>();
        for (Move move : legalMoves) {
            Piece capture = getPieceAt(move.getToRow(), move.getToCol());
            if (capture != null && capture.isWhite() != white){
                captureMoves.add(move);
            }
        }
        return captureMoves;
    }

    public ArrayList<Piece> getTeamPieces(boolean white) {
        ArrayList<Piece> teamPieces = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            for (Piece piece : pieces[i])
                if (piece != null && piece.isWhite() == white)
                    teamPieces.add(piece);

        return teamPieces;
    }

    public ArrayList<Move> getTeamCheckMoves(boolean white){
        ArrayList<Move> allTeamLegalMoves = getTeamLegalMove(white);
        ArrayList<Move> checkMoves = new ArrayList<>();
        King opponentsKing = getKing(!white);
        for (Move move : allTeamLegalMoves){
            if (move.getToRow() == opponentsKing.getRow() && move.getToCol() == opponentsKing.getCol()) {
                checkMoves.add(move);
            }
        }
        return checkMoves;
    }

    public ArrayList<Move> getPieceCaptures(Piece piece){
        ArrayList<Move> allTeamCaptureMoves = getTeamCaptureMoves(piece.isWhite());
        ArrayList<Move> pieceCaptureMoves = new ArrayList<>();
        for (Move move : allTeamCaptureMoves){
            if (arePiecesEqual(move.getPiece(), piece))
                pieceCaptureMoves.add(move);
        }
        return pieceCaptureMoves;
    }
    public boolean arePiecesEqual(Piece piece1, Piece piece2){
        return piece1.getRow() == piece2.getRow() && piece1.getCol() == piece2.getCol() && piece1.getName().equals(piece2.getName());
    }

    public ArrayList<Piece> getAllPieces() {
        ArrayList<Piece> allPieces = new ArrayList<>();
        allPieces.addAll(getTeamPieces(true));
        allPieces.addAll(getTeamPieces(false));
        return allPieces;
    }

    public boolean inCheck(boolean white) {
        ArrayList<Piece> oppositeTeamPieces = getTeamPieces(!white);
        for (Piece piece : oppositeTeamPieces)
            if (!(piece instanceof King))
                if (canCaptureKing(piece))
                    return true;

        return false;
    }

    public boolean canCaptureKing(Piece piece) {
        King king = getKing(!piece.isWhite());
        Move requiredCheckMove = new Move(piece.getRow(), piece.getCol(), king.getRow(), king.getCol(), piece);
        for (Move move : piece.getLegalMoves(this, piece.getRow(), piece.getCol())) {
            if (areMovesEqual(move, requiredCheckMove))
                return true;
        }
        return false;
    }

    public boolean areMovesEqual(Move move1, Move move2) {
        return move1.getPiece().getName().equals(move2.getPiece().getName()) && move1.getToRow() == move2.getToRow() && move1.getToCol() == move2.getToCol()
                && move1.getFromCol() == move2.getFromCol() && move1.getFromRow() == move1.getFromRow() && move1.getPiece().isWhite() == move2.getPiece().isWhite();
    }

    public King getKing(boolean white) {
        ArrayList<Piece> teamPieces = getTeamPieces(white);
        for (Piece piece : teamPieces)
            if (piece.getName().equals("King"))
                return (King) piece;

        return null;
    }

    public boolean isAttacked(int row, int col, boolean isWhite) {
        ArrayList<Piece> teamPieces = getTeamPieces(!isWhite);
        for (Piece piece : teamPieces)
            if (!(piece instanceof King))
                for (Move move : piece.getLegalMoves(this, piece.getRow(), piece.getCol()))
                    if (areMovesEqual(move, new Move(piece.getRow(), piece.getCol(), row, col, piece)))
                        return true;
        return false;
    }

    public boolean isValidMove(ArrayList<Move> legalMoves, Move move) {
        for (Move m : legalMoves)
            if (areMovesEqual(m, move))
                return true;

        return false;
    }

    public void makeMove(Move move) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        if (isCastleMove(move)) {
            makeCastleMove(move);
        } else {
            Piece piece = pieces[fromRow][fromCol];
            Piece capturedPiece = pieces[toRow][toCol];


            pieces[toRow][toCol] = piece;
            pieces[fromRow][fromCol] = null;

            if (piece != null) {
                piece.setHasMoved(true);
                piece.setPosition(toRow, toCol);
            }
            if (capturedPiece != null) {
                move.setCapturedPiece(capturedPiece);
                capturedPiece.setCaptured(true);
            }
        }
    }

    public void unmakeMove(Move move) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        if (isCastleMove(move)) {
            unmakeCastleMove(move);
        } else {
            Piece piece = move.getPiece();
            Piece capturedPiece = move.getCapturedPiece();

            pieces[fromRow][fromCol] = piece;
            pieces[toRow][toCol] = capturedPiece;


            piece.setHasMoved(false);
            piece.setPosition(fromRow, fromCol);


            if (capturedPiece != null) {
                capturedPiece.setCaptured(false);
                capturedPiece.setPosition(toRow, toCol);
            }
        }
    }

    private void makeCastleMove(Move move) {
        if (move.getPiece().getName().equals("King")) {
            King king = (King) move.getPiece();
            king.setHasCastled(true);
            king.setHasMoved(true);

            // Move the rook
            int fromRow = move.getFromRow();
            int fromCol = move.getFromCol();
            int toCol = move.getToCol();
            int rookToCol = (toCol > fromCol) ? toCol - 1 : toCol + 1;
            int rookCol = (toCol > fromCol) ? 7 : 0;
            Piece rook = getPieceAt(fromRow, rookCol);
            pieces[fromRow][rookCol] = null;
            pieces[fromRow][rookToCol] = rook;
            rook.setPosition(fromRow, rookToCol);
            rook.setHasMoved(true);
        }
            Piece piece = move.getPiece();
            pieces[move.getToRow()][move.getToCol()] = piece;
            pieces[move.getFromRow()][move.getFromCol()] = null;
            piece.setPosition(move.getToRow(), move.getToCol());
            piece.setHasMoved(true);


    }
    private void unmakeCastleMove(Move move){
        move.getPiece().setHasMoved(false);
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        Piece king = move.getPiece();
        king.setPosition(fromRow, fromCol);
        pieces[fromRow][fromCol] = king;
        pieces[toRow][toCol] = null;

        king.setHasMoved(false);

        int rookCol = (toCol > fromCol) ? 7 : 0;
        int rookToCol = (toCol > fromCol) ? toCol-1 : toCol+1;
        Piece rook = pieces[toRow][rookToCol];
        rook.setPosition(fromRow, rookCol);
        pieces[fromRow][rookCol] = rook;
        pieces[toRow][rookToCol] = null;

        rook.setHasMoved(false);
    }
    private boolean isCastleMove(Move move){
        if (move.getPiece() == null)
            return false;

        if (move.getPiece().getName().equals("King"))
            return (Math.abs(move.getToCol() - move.getFromCol()) == 2);

        return false;
    }
    public Piece getPieceAt(int row, int col){
        return pieces[row][col];
    }
    public void setPieceAt(int row, int col, Piece piece){
        pieces[row][col] = piece;
    }
    public boolean isCheckmate(boolean white){
        return inCheck(white) && getTeamLegalMove(white).isEmpty();
    }
    public boolean isStalemate(boolean white){
        return !inCheck(white) && getTeamLegalMove(white).isEmpty();
    }
    public void checkPawnPromotions(){
        ArrayList<Piece> pawns = getAllPieces();
        ArrayList<Piece> notPawns = new ArrayList<>();
        for (Piece piece : pawns)
            if (!(piece instanceof Pawn))
                notPawns.add(piece);

        pawns.removeAll(notPawns);
        for (Piece pawn : pawns)
            if (pawn.getRow() == 0 || pawn.getRow() == 7)
                setPieceAt(pawn.getRow(), pawn.getCol(), new Queen(pawn.isWhite(), pawn.getRow(), pawn.getCol(), 4, 900));
    }
}
