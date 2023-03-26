package Engine.Openings;

import Logic.Board;
import Logic.Movement.Move;
import Logic.Pieces.Piece;
import View.GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class OpeningGenerator {
    HashMap<Character, Integer> Coordinates;
    HashMap<Character, Integer> Identifier;

    File openingFile;
    Board board;
    ArrayList<String> opening;

    public OpeningGenerator(Board board){
        this.openingFile = new File("src/Engine/Openings/Resources/openings.txt");
        this.Coordinates = getBoardCoordinates();
        this.Identifier = getIdentifiers();
        this.board = board;
        this.opening = getRandomOpening();
    }

    public boolean move(){
        if (opening.size() > 0) {
            Move openingMove = getOpeningMove();
            board.makeMove(openingMove);
            GUI.previousMoves.push(openingMove);
            return true;
        }
        return false;
    }


    private ArrayList<String> getRandomOpening(){
        return getOpenings().get(new Random().nextInt(getOpenings().size()));
    }

    private ArrayList<ArrayList<String>> getOpenings(){
        ArrayList<ArrayList<String>> openings = new ArrayList<>();

        try {
            Scanner s = new Scanner(new FileReader(openingFile));

            while (s.hasNextLine())
                openings.add(new ArrayList<>(List.of(s.nextLine().split(" "))));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return openings;
    }

    private Move getOpeningMove() {
        Opening opening;
        // Find col
        int row = 8 - Integer.parseInt(String.valueOf(this.opening.get(0).toCharArray()[2]));
        // Find row
        int col = -1;
        for (Map.Entry<Character, Integer> entry : Coordinates.entrySet())
            if (entry.getKey() == this.opening.get(0).toCharArray()[1])
                col = entry.getValue();

        // Find identifier
        char identifier = this.opening.get(0).toCharArray()[0];

        opening = new Opening(getPieceFromIdentifier(identifier, row, col), board);

        this.opening.remove(0);

        return opening.getMove();
    }
    private HashMap<Character, Integer> getBoardCoordinates() {
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

    private Move getPieceFromIdentifier(Character identifier, int row, int col) {
        boolean isWhite = false;
        for (Map.Entry<Character, Integer> entry : Identifier.entrySet())
            if (entry.getKey() == identifier)
                for (int i = 0; 8 > i; i++)
                    for (Piece piece : board.pieces[i])
                        if (piece != null)
                            if (piece.isWhite() == isWhite && piece.getID() == entry.getValue())
                                if (board.isValidMove(board.getPieceLegalMove(piece), (new Move(piece.getRow(), piece.getCol(), row, col, piece))))
                                    return new Move(piece.getRow(), piece.getCol(), row, col, piece);


        return null;
    }

    private HashMap<Character, Integer> getIdentifiers() {
        HashMap<Character, Integer> identifier = new HashMap<>();
        identifier.put('N', 2);
        identifier.put('B', 3);
        identifier.put('R', 1);
        identifier.put('K', 5);
        identifier.put('Q', 4);
        identifier.put('P', 6);

        // MOVE PAWN IF NO CAPITAL IDENTIFIER IN SEQUENCE

        return identifier;
    }
}
