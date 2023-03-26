package Logic.Pieces;

import Logic.Board;
import Logic.Movement.Move;

import java.util.ArrayList;

public abstract class Piece {
    
    private final boolean isWhite;
    private boolean hasMoved;
    private boolean isCaptured;
    private int row;
    private int col;
    private final int id;
    private int value;
    
    public Piece(boolean isWhite, int row, int col, int id, int value){
        this.isWhite = isWhite;
        this.row = row;
        this.col = col;
        this.id = id;
        this.value = value;
        this.isCaptured = false;
        this.hasMoved = false;
    }

    public abstract Piece copy();
    public abstract String getName();
    public abstract ArrayList<Move> getLegalMoves(Board board, int row, int col);
    
    // Non-abstract methods
    public int getID(){
        return id;
    }
    public int getValue(){
        return value;
    }
    public void setValue(int value){
        this.value = value;
    }
    public void setCaptured(boolean isCaptured){
        this.isCaptured = isCaptured;
    }    
    public boolean isCaptured(){
        return isCaptured;
    }
    public void setPosition(int row, int col){
        this.row = row;
        this.col = col;
    }
    public int getCol(){
        return col;
    }
    public int getRow(){
        return row;
    }
    public boolean isWhite(){
        return isWhite;
    }
    public void setHasMoved(boolean hasMoved){
        this.hasMoved = hasMoved;
    }
    public boolean hasMoved(){
        return hasMoved;
    }

}
