package View;

import Engine.Evaluations;
import Logic.Board;
import Logic.Movement.Move;
import Logic.Pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class GUI extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

    // Window Constants
    private final int BOARD_SIZE = 512;
    private final int SQUARE_SIZE = BOARD_SIZE / 8;
    private final Image[] pieceSprites;
    // For drag and drop mechanism
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Piece selectedPiece;
    // Usages
    Board board;
    Timer timer;
    int[] toHighlight = null;
    boolean whitesMove = true;
    int depth;

    public static Stack<Move> previousMoves = new Stack<>();

    public GUI(){
        board = new Board();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type a depth: ");
        this.depth = scanner.nextInt();

        Thread console = new Thread(() -> {
            while (true){
               if (scanner.hasNextLine())
                   if (scanner.nextLine().equals("undo"))
                       if (!previousMoves.isEmpty())
                           board.unmakeMove(previousMoves.pop());
                       else
                           System.out.println("Last move was null?");
           }
        });
        setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        pieceSprites = new Image[12];
        doImage();
        timer = new Timer(15, this);

        timer.start();
        console.start();
    }

    @Override
    public void paint(Graphics g){
        super.paintComponent(g);

        // Draw squares
        for (int row=0; row<8; row++){
            for (int col=0; col<8; col++){
                Color color = ((row + col) % 2 == 0) ? new Color(255, 206, 158) : new Color(209, 139, 71);

                g.setColor(color);
                g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        // Draw mouse
        if (toHighlight != null){
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(toHighlight[1] * SQUARE_SIZE, toHighlight[0] * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        }
        //Draw legal moves
        if (selectedPiece != null){
            for (Move move : board.getPieceLegalMove(selectedPiece)){
                g.setColor(Color.ORANGE);
                g.fillRect(move.getToCol() * SQUARE_SIZE, move.getToRow() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        // Draw pieces
        for (int i=0; i<8; i++)
            for (Piece piece : board.pieces[i])
                if (piece != null)
                    g.drawImage(getIconByPiece(piece), piece.getCol() * 64, piece.getRow() * 64, null);
    }

    private void doImage(){
        try {
            BufferedImage all = ImageIO.read(new File("src/View/Resources/chess.png"));
            int ind=0;
            for (int y=0; y < 400; y+=200){
                for (int x=0; x < 1200; x+=200){
                    pieceSprites[ind] = all.getSubimage(x, y, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
                    ind++;
                }
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    private Image getIconByPiece(Piece piece){
        switch (piece.getID()) {
            case 6 -> {
                return pieceSprites[(piece.isWhite()) ? 5 : 11];
            }
            case 5 -> {
                return pieceSprites[(piece.isWhite()) ? 0 : 6];
            }
            case 4 -> {
                return pieceSprites[(piece.isWhite()) ? 1 : 7];
            }
            case 3 -> {
                return pieceSprites[(piece.isWhite()) ? 2 : 8];
            }
            case 2 -> {
                return pieceSprites[(piece.isWhite()) ? 3 : 9];
            }
            case 1 -> {
                return pieceSprites[(piece.isWhite()) ? 4 : 10];
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selectedPiece = null;
        int row = e.getY() / SQUARE_SIZE;
        int col = e.getX() / SQUARE_SIZE;

        Piece piece = board.getPieceAt(row, col);

        if (piece != null){
            selectedPiece = piece;
            selectedRow = row;
            selectedCol = col;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedPiece != null){
            int row = e.getY() / SQUARE_SIZE;
            int col = e.getX() / SQUARE_SIZE;


            Move move = new Move(selectedRow, selectedCol, row, col, selectedPiece);
            if (board.isValidMove(board.getPieceLegalMove(selectedPiece), move)){
                selectedPiece.setHasMoved(true);
                board.makeMove(move);
                previousMoves.push(move);
                whitesMove = false;

                repaint();
            }
        }
        selectedPiece = null;
        selectedRow = -1;
        selectedCol = -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        board.checkPawnPromotions();
        repaint();
        if (!whitesMove) {
            board.ai.move(board, depth);
            System.out.println(Evaluations.evaluate(board, true));
            whitesMove = true;
        } else {
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int row = e.getY() / SQUARE_SIZE;
        int col = e.getX() / SQUARE_SIZE;

        toHighlight = new int[]{row, col};
    }

    // Unnecessary mouse methods
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

}
