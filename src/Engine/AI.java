package Engine;

import Engine.Openings.OpeningGenerator;
import Logic.Board;
import Logic.Movement.Move;
import Logic.Pieces.Piece;
import View.GUI;

import static Engine.Evaluations.evaluate;

public class AI {

    Board board;
    OpeningGenerator openingGenerator;
    public AI(Board board){
        this.board = board;
        this.openingGenerator = new OpeningGenerator(board);
    }
    public void move(Board board, int depth){
        if (!openingGenerator.move()) {
            Move move = search(depth);
            board.makeMove(move);
            GUI.previousMoves.push(move);
        }
    }

    private int positionsEvaluated = 0;
    public Move search(int depth){
        positionsEvaluated = 0;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int bestScore = 0;

        Move bestMove = null;
        boolean first = true;
        for (Move move : board.getTeamLegalMove(false)){
            board.makeMove(move);
            int score = min(board, alpha, beta, depth - 1);
            board.unmakeMove(move);
            if (first || score > bestScore){
                first = false;
                bestScore = score;
                bestMove = move;
            }
            alpha = Math.max(alpha, bestScore);
        }
        System.out.println(positionsEvaluated);
        return bestMove;
    }
    private int min(Board board, int alpha, int beta, int depth){
        positionsEvaluated++;
        if (depth == 0){
            return evaluate(board, false);
        }
        Piece blackKing = board.getKing(false);
        if (blackKing == null){
            // if moves lead to black king missing, we will immediately prune branch.\
            return Integer.MIN_VALUE;
        }
        int minScore = Integer.MAX_VALUE;
        for (Move move : board.getTeamLegalMove(true)){
            board.makeMove(move);
            int score = max(board, alpha, beta, depth - 1);
            board.unmakeMove(move);
            minScore = Math.min(minScore, score);
            beta = Math.min(beta, minScore);
            if (beta <= alpha){
                break;
            }
        }
        return minScore;
    }
    private int max(Board board, int alpha, int beta, int depth){
        positionsEvaluated++;
        if (depth == 0){
            return evaluate(board, false);
        }
        Piece king = board.getKing(false);
        if (king == null){
            return Integer.MIN_VALUE;
        }
        int maxScore = Integer.MIN_VALUE;
        for (Move move : board.getTeamLegalMove(false)){
            board.makeMove(move);
            int score = min(board, alpha, beta, depth - 1);
            board.unmakeMove(move);
            maxScore = Math.max(maxScore, score);
            alpha = Math.max(alpha, maxScore);
            if (beta <= alpha){
                break;
            }
        }
        return maxScore;
    }

}
