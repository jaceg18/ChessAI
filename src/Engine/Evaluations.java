package Engine;

import Logic.Board;
import Logic.Pieces.Pawn;
import Logic.Pieces.Piece;

import java.util.ArrayList;
import java.util.Arrays;

public class Evaluations {
    public static int evaluate(Board board, boolean white){
        // Simple evaluation function, will improve soon.
        int score = 0;

        score += getMaterialScore(board, white);
        score += getCenterScore(board, white);
        score += getPawnPromotionScore(board, white);

        if (board.isCheckmate(white)) {
            score = (white) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }
        if (board.isStalemate(white)) {
            score = 0;
        }
        return score;
    }

    private static int getMaterialScore(Board board, boolean white){
        int score = 0;
        ArrayList<Piece> boardPieces = board.getAllPieces();
        for (Piece piece : boardPieces){
            score += piece.getValue() * ((white == piece.isWhite()) ? 1 : -1);
        }

        return score;
    }
    private static int getCenterScore(Board board, boolean white){
        int score = 0;
        int centerWeight = 10;
        int[][] centerSquares = {{3, 3}, {3, 4}, {4, 3}, {4, 4}};
        for (int[] centerSquare : centerSquares) {
            Piece centerPiece = board.getPieceAt(centerSquare[0], centerSquare[1]);
            if (centerPiece != null) {
                score += centerWeight * ((white == centerPiece.isWhite()) ? 1 : -1);
            }
        }
        return score;
    }
    private static int getPawnPromotionScore(Board board, boolean white) {
        int score = 0;
        int promotionWeight = 400;
        int[][] promotionSquares = (white) ? new int[][]{{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}}
                : new int[][]{{7, 0}, {7, 1}, {7, 2}, {7, 3}, {7, 4}, {7, 5}, {7, 6}, {7, 7}};

        for (int[] promotionSquare : promotionSquares) {
            Piece promotionPiece = board.getPieceAt(promotionSquare[0], promotionSquare[1]);
            if (promotionPiece instanceof Pawn && promotionPiece.isWhite() == white) {
                score += promotionWeight;
            }
        }

        return score;
    }
}
