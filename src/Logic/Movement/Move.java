package Logic.Movement;

import Logic.Pieces.Piece;

public class Move {
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
    private Piece piece;
    private Piece capturedPiece;

    public Move(int fromRow, int fromCol, int toRow, int toCol, Piece piece){
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.piece = piece;
    }
    public int getFromRow(){
        return fromRow;
    }
    public int getFromCol(){
        return fromCol;
    }
    public int getToRow(){
        return toRow;
    }
    public int getToCol(){
        return toCol;
    }

    public Piece getPiece(){
        return piece;
    }
    public void setCapturedPiece(Piece piece){
        this.capturedPiece = piece;
    }
    public Piece getCapturedPiece(){
        return capturedPiece;
    }
    @Override
    public String toString(){
        return "Piece: " + getPiece().getName() + " | FromRow: " + getFromRow() + " | ToRow: " + getToRow() + " | FromCol: " + getFromCol() + " | ToCol: " + getToCol();
    }
}
