package Engine;

import Engine.Openings.OpeningBook;
import Engine.Openings.Resources.OpeningHelper;
import Logic.Board;
import Logic.Movement.Move;
import Logic.Pieces.Piece;
import View.GUI;
import View.Resources.Sounds.AudioPlayer;

import java.util.ArrayList;

import static Engine.Evaluations.evaluate;
public class AI {

    Board board;
    OpeningBook openingBook;

    int openMovesLeft;
    public AI(Board board){
        this.board = board;
        this.openingBook = new OpeningBook(board);
        this.openMovesLeft = 5;
    }
    public void move(Board board, int depth){
        String gameNotation = OpeningHelper.getGameInNotation();
        Move move = openingBook.getMove(gameNotation);

        if (openMovesLeft <= 0 || move == null){
            move = search(depth);
        }

        board.makeMove(move);
        openMovesLeft--;
        GUI.previousMoves.push(move);
        GUI.gameMoves.add(move);
        AudioPlayer.playSound(move.getCapturedPiece() != null);

    }
    public Move search(int depth){
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int bestScore = 0;

        Move bestMove = null;
        boolean first = true;
        for (Move move : Evaluations.getOrderedMoves(board, false)){
            board.makeMove(move);
            int score = min(board, alpha, beta, depth - 1);
            board.unmakeMove(move);
            if (first || score > bestScore){
                first = false;
                bestScore = score;
                bestMove = move;
            }
            alpha = bestScore;
        }
        return bestMove;
    }

    private int min(Board board, int alpha, int beta, int depth){
        if (depth == 0){
            return evaluate(board, false);
        }
        Piece king = board.getKing(false);
        if (king == null){
            return Integer.MIN_VALUE;
        }

        int minScore = Integer.MAX_VALUE;
        for (Move move : Evaluations.getOrderedMoves(board, true)){
            board.makeMove(move);
            int score = max(board, alpha, beta, depth - 1);
            board.unmakeMove(move);
            minScore = Math.min(minScore, score);
            beta = Math.min(beta, minScore);
            if (beta <= alpha){
                break;
            }
        }

        // Perform quiescence search if we have reached a "quiet" position
        if (depth <= 0 && isQuietPosition(board)){
            return quiescenceSearch(board, alpha, beta);
        }

        return minScore;
    }

    private int max(Board board, int alpha, int beta, int depth){
        if (depth == 0){
            return evaluate(board, false);
        }
        Piece king = board.getKing(false);
        if (king == null){
            return Integer.MIN_VALUE;
        }

        int maxScore = Integer.MIN_VALUE;
        for (Move move : Evaluations.getOrderedMoves(board, false)){
            board.makeMove(move);
            int score = min(board, alpha, beta, depth - 1);
            board.unmakeMove(move);
            maxScore = Math.max(maxScore, score);
            alpha = Math.max(alpha, maxScore);
            if (beta <= alpha){
                break;
            }
        }

        // Perform quiescence search if we have reached a "quiet" position
        if (depth <= 0 && isQuietPosition(board)){
            return quiescenceSearch(board, alpha, beta);
        }

        return maxScore;
    }

    private int quiescenceSearch(Board board, int alpha, int beta) {
        int standPat = evaluate(board, false);

        if (standPat >= beta){
            return beta;
        }

        if (alpha < standPat){
            alpha = standPat;
        }

        for (Move move : Evaluations.getOrderedMoves(board, true)){
            if (move.getCapturedPiece() == null) {
                continue;
            }
            board.makeMove(move);
            int score = -quiescenceSearch(board, -beta, -alpha);
            board.unmakeMove(move);

            if (score >= beta){
                return beta;
            }

            if (score > alpha){
                alpha = score;
            }
        }

        return alpha;
    }

    private boolean isQuietPosition(Board board) {
        // Check if any pieces can be captured on the current board
        for (Piece piece : board.getTeamPieces(false)) {
            ArrayList<Move> captures = board.getPieceCaptures(piece);
            if (!captures.isEmpty()) {
                return false;
            }
        }
        return true;
    }
  //  public Move search(int depth){
  //      int alpha = Integer.MIN_VALUE;
  //      int beta = Integer.MAX_VALUE;
  //      int bestScore = 0;
//
  //      Move bestMove = null;
  //      boolean first = true;
  //      for (Move move : Evaluations.getOrderedMoves(board, false)){
  //          board.makeMove(move);
  //          int score = min(board, alpha, beta, depth - 1);
  //          board.unmakeMove(move);
  //          if (first || score > bestScore){
  //              first = false;
  //              bestScore = score;
  //              bestMove = move;
  //          }
  //          alpha = bestScore;
  //      }
  //      return bestMove;
  //  }
  //  private int min(Board board, int alpha, int beta, int depth){
  //      if (depth == 0){
  //          return evaluate(board, false);
  //      }
  //      Piece king = board.getKing(false);
  //      if (king == null){
  //          // if moves lead to black king missing, we will immediately prune branch.\
  //          return Integer.MIN_VALUE;
  //      }
  //      int minScore = Integer.MAX_VALUE;
  //      for (Move move : Evaluations.getOrderedMoves(board, true)){
  //          board.makeMove(move);
  //          int score = max(board, alpha, beta, depth - 1);
  //          board.unmakeMove(move);
  //          minScore = Math.min(minScore, score);
  //          beta = Math.min(beta, minScore);
  //          if (beta <= alpha){
  //              break;
  //          }
  //      }
  //      return minScore;
  //  }
  //  private int max(Board board, int alpha, int beta, int depth){
  //      if (depth == 0){
  //          return evaluate(board, false);
  //      }
  //      Piece king = board.getKing(false);
  //      if (king == null){
  //          return Integer.MIN_VALUE;
  //      }
  //      int maxScore = Integer.MIN_VALUE;
  //      for (Move move : Evaluations.getOrderedMoves(board, false)){
  //          board.makeMove(move);
  //          int score = min(board, alpha, beta, depth - 1);
  //          board.unmakeMove(move);
  //          maxScore = Math.max(maxScore, score);
  //          alpha = Math.max(alpha, maxScore);
  //          if (beta <= alpha){
  //              break;
  //          }
  //      }
  //      return maxScore;
  //  }



}
