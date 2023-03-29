package Engine.Openings.Resources;

import Logic.Board;
import Logic.Movement.Move;
import Logic.Pieces.Piece;
import View.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OpeningHelper {
    public static Move identifyMove(Board board, String move) {

        char identifier = move.toCharArray()[0];
        char colIdentifier = move.toCharArray()[1];
        int row = Integer.parseInt(String.valueOf(move.toCharArray()[2]));
        int col = -1;

        for (Map.Entry<Character, Integer> entry : getBoardCoordinates().entrySet())
            if (entry.getKey() == colIdentifier)
                col = entry.getValue();

        boolean isWhite = false;
        for (Map.Entry<Character, Integer> entry : getIdentifiers().entrySet())
            if (entry.getKey() == identifier)
                for (int i = 0; 8 > i; i++)
                    for (Piece piece : board.pieces[i])
                        if (piece != null)
                            if (piece.isWhite() == isWhite && piece.getID() == entry.getValue())
                                if (board.isValidMove(board.getPieceLegalMove(piece), (new Move(piece.getRow(), piece.getCol(), row, col, piece)))) {
                                    return new Move(piece.getRow(), piece.getCol(), row, col, piece);
                                }


        return null;
    }
    public static String identifyNotation(Move move){
        char identifier = '0';
        char colIdentifier = '0';
        int row = move.getToRow();
        int col = move.getToCol();

        Piece piece = move.getPiece();
        for (Map.Entry<Character, Integer> entry : getIdentifiers().entrySet())
            if (entry.getValue() == piece.getID()){
                identifier = entry.getKey();
                break;
            }
        for (Map.Entry<Character, Integer> entry : getBoardCoordinates().entrySet())
            if (entry.getValue() == col) {
                colIdentifier = entry.getKey();
                break;
            }

        String notatedMove = "" + identifier + colIdentifier + (row);

        if (notatedMove.startsWith("P"))
            notatedMove = notatedMove.substring(1);

        return notatedMove;
    }
    public static String getGameInNotation(){
        ArrayList<Move> gameMoves = GUI.gameMoves;
        StringBuilder game = new StringBuilder();
        for (Move move : gameMoves){
            game.append(identifyNotation(move)).append(" ");
        }
        return game.toString();
    }

    // Helper methods for identifyPiece and identifyNotation
    private static HashMap<Character, Integer> getBoardCoordinates() {
        HashMap<Character, Integer> coordinates = new HashMap<>();
        coordinates.put('h', 7);
        coordinates.put('g', 6);
        coordinates.put('f', 5);
        coordinates.put('e', 4);
        coordinates.put('d', 3);
        coordinates.put('c', 2);
        coordinates.put('b', 1);
        coordinates.put('a', 0);

        return coordinates;
    }
    private static HashMap<Character, Integer> getIdentifiers() {
        HashMap<Character, Integer> identifier = new HashMap<>();
        identifier.put('N', 2);
        identifier.put('B', 3);
        identifier.put('R', 1);
        identifier.put('K', 5);
        identifier.put('Q', 4);
        identifier.put('P', 6);

        return identifier;
    }


}
