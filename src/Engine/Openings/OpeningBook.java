package Engine.Openings;

import Engine.Openings.Resources.OpeningHelper;
import Logic.Board;
import Logic.Movement.Move;
import Logic.Pieces.Piece;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class OpeningBook {
    private List<String> games;
    Board board;
    private final File openingFile = new File("src/Engine/Openings/Resources/openings.txt");

    // We will only call from this for the first 5 moves of the game.
    public OpeningBook(Board board){
        this.board = board;
        games = new ArrayList<>();

        loadFromFile();
    }
    public void addGame(String moves){
        for (int i=0; 60 > i; i++){
            moves = moves.replaceAll(i + ".", "");
        }
        moves = moves.replaceAll("x", "");
        // For testing purposes
        System.out.println(moves);
        games.add(moves);

        saveToFile();
    }
    public Move getMove(String currentMoves){
        // Not the issue
        for (String game : games){
            if (game.contains(currentMoves)){
                String nextMove = game.substring(currentMoves.length());
                String[] splitMoves = nextMove.split(" ");
                String move = splitMoves[0];

                if (move.length() == 2){
                    move = "P" + move;
                }

               return (OpeningHelper.identifyMove(board, move));
            }
        }
        return null;
    }
    private void loadFromFile(){
        // Not the issue
        ArrayList<String> openings = new ArrayList<>();
        try {
            Scanner s = new Scanner(new FileReader(openingFile));
            while (s.hasNextLine()) {
                openings.add(s.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int j = 0; j < openings.size(); j++) {
            String moves = openings.get(j);
            for (int i = 0; i < 60; i++) {
                moves = moves.replaceAll("x", "");
                moves = moves.replaceAll(i + "\\.", "");
            }
            openings.set(j, moves);
            // For testing purposes
        }
        for (int j=0; j < openings.size(); j++){
            String moves = openings.get(j);
            String[] move = moves.split(" ");
            StringBuilder notatedMove = new StringBuilder();
            for (int k=0; k<move.length; k++){
                StringBuilder moveBuilder = new StringBuilder();
                for (int z=0; z<move[k].length(); z++){
                    if (Character.isDigit(move[k].toCharArray()[z])){
                        int row = 8 - Integer.parseInt(String.valueOf(move[k].toCharArray()[z]));
                        moveBuilder.append(row);
                    } else {
                        moveBuilder.append(move[k].toCharArray()[z]);
                    }
                }
                move[k] = moveBuilder.toString();
                notatedMove.append(move[k]).append(" ");
            }
            openings.set(j, notatedMove.toString());
        }
        Collections.shuffle(openings);

        games = openings;
    }

    private void saveToFile(){
        // For when we want to use console commands to save a new opening
    }
}
