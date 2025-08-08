package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import game.ChessGame;
import game.MoveValidator;
import game.GameState;
import pieces.Piece;
import pieces.PieceColor;
import pieces.Pawn;
import pieces.Queen;
import board.Position;

public class BoardPanel extends JPanel {
    private ChessGame chessGame;
    private SquarePanel[][] squarePanels;
    private Position selectedPosition;
    private List<Position> legalMoves;
    
    public BoardPanel(ChessGame chessGame) {
        this.chessGame = chessGame;
        this.selectedPosition = null;
        this.legalMoves = new ArrayList<>();
        
        // Set up the panel
    setLayout(new GridLayout(8, 8));
    setPreferredSize(new Dimension(UIConstants.BOARD_PREFERRED_SIZE, UIConstants.BOARD_PREFERRED_SIZE));
        
        // Create the square panels
        createSquarePanels();
    }
    
    @Override
    public Dimension getPreferredSize() {
        // Always return a square dimension with smaller margin
        Dimension parentSize = getParent() != null ? getParent().getSize() : new Dimension(800, 800);
        int size = Math.min(parentSize.width - 10, parentSize.height - 10); // Reduced margin from 40 to 10
        size = Math.max(size, 400); // Minimum size
        return new Dimension(size, size);
    }
    
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(400, 400);
    }
    
    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
    
    private void createSquarePanels() {
        squarePanels = new SquarePanel[8][8];
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                SquarePanel squarePanel = new SquarePanel(chessGame.getBoard().getSquare(row, col));
                
                // Add mouse click handler
                final int finalRow = row;
                final int finalCol = col;
                squarePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(new Position(finalRow, finalCol));
                    }
                });
                
                squarePanels[row][col] = squarePanel;
                add(squarePanel);
            }
        }
    }
    
    private void handleSquareClick(Position clickedPosition) {
        // Ignore clicks if game is over
        if (chessGame.getGameState().isGameOver()) {
            return;
        }
        
        if (selectedPosition == null) {
            // First click - try to select piece
            handleFirstClick(clickedPosition);
        } else {
            // Second click - try to move or deselect
            handleSecondClick(clickedPosition);
        }
    }
    
    private void handleFirstClick(Position position) {
        // Check if there's a piece at this position belonging to current player
        var piece = chessGame.getBoard().getPiece(position);
        if (piece != null && piece.getColor() == chessGame.getGameState().getCurrentPlayer()) {
            // Select this piece
            selectedPosition = position;
            highlightSquare(position, true);
            
            // Get and highlight legal moves for this piece
            legalMoves = MoveValidator.getLegalMoves(chessGame.getBoard(), position, 
                chessGame.getGameState().getCurrentPlayer(), chessGame.getGameState());
            highlightLegalMoves(true);
        }
        // Ignore clicks on empty squares or opponent pieces
    }
    
    private void handleSecondClick(Position position) {
        if (position.equals(selectedPosition)) {
            // Clicking same square - deselect
            deselectPiece();
        } else {
            // Try to make the move (with promotion dialog if needed)
            boolean moveSuccessful = attemptMoveWithPromotion(selectedPosition, position);
            
            if (moveSuccessful) {
                // Move was successful - update display
                deselectPiece();
                refreshBoard();
                
                // Update the main GUI - find the ChessGUI window
                SwingUtilities.invokeLater(() -> {
                    ChessGUI parentGUI = (ChessGUI) SwingUtilities.getWindowAncestor(this);
                    if (parentGUI != null) {
                        parentGUI.updateDisplay();
                    }
                });
            } else {
                // Invalid move - just deselect
                deselectPiece();
            }
        }
    }

    private boolean attemptMoveWithPromotion(Position from, Position to) {
        // If this is a pawn moving to the last rank, prompt for promotion piece
        Piece moving = chessGame.getBoard().getPiece(from);
        Class<? extends Piece> promotionType = null;
        if (moving instanceof Pawn) {
            int targetRow = to.getRow();
            int promotionRow = moving.getColor() == PieceColor.WHITE ? 0 : 7;
            if (targetRow == promotionRow) {
                promotionType = promptPromotionPiece();
                if (promotionType == null) {
                    promotionType = Queen.class; // default fallback
                }
            }
        }

        return chessGame.makeMove(from, to, promotionType);
    }

    private Class<? extends Piece> promptPromotionPiece() {
        Piece moving = chessGame.getBoard().getPiece(selectedPosition);
        PieceColor color = moving != null ? moving.getColor() : PieceColor.WHITE;
        Window owner = SwingUtilities.getWindowAncestor(this);
        PromotionDialog dialog = new PromotionDialog(owner, color);
        Class<? extends Piece> chosen = dialog.choose();
        return chosen != null ? chosen : Queen.class;
    }
    
    private void deselectPiece() {
        if (selectedPosition != null) {
            highlightSquare(selectedPosition, false);
            highlightLegalMoves(false);
            selectedPosition = null;
            legalMoves.clear();
        }
    }
    
    private void highlightLegalMoves(boolean highlight) {
        for (Position move : legalMoves) {
            squarePanels[move.getRow()][move.getCol()].setLegalMoveTarget(highlight);
        }
    }
    
    private void highlightSquare(Position position, boolean highlight) {
        squarePanels[position.getRow()][position.getCol()].setHighlighted(highlight);
    }
    
    public void refreshBoard() {
        // Optional: warm icon cache for current square size once per session
        int width = getWidth() / 8;
        if (width > 0) {
            int iconSize = Math.max(UIConstants.PIECE_ICON_MIN, (int) Math.floor(width * UIConstants.PIECE_ICON_SCALE));
            ImageLoader.warmCache(iconSize);
        }
        // Update all square panels to reflect current board state
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squarePanels[row][col].setSquare(chessGame.getBoard().getSquare(row, col));
                squarePanels[row][col].setHighlighted(false); // Clear highlights
                squarePanels[row][col].setLegalMoveTarget(false); // Clear legal move indicators
                squarePanels[row][col].setLastMove(false);
                squarePanels[row][col].setInCheck(false);
            }
        }
        // Highlight last move if available
        GameState state = chessGame.getGameState();
        if (state.getLastMoveFrom() != null && state.getLastMoveTo() != null) {
            Position from = state.getLastMoveFrom();
            Position to = state.getLastMoveTo();
            squarePanels[from.getRow()][from.getCol()].setLastMove(true);
            squarePanels[to.getRow()][to.getCol()].setLastMove(true);
        }
        // Highlight the king in check
        if (state.getStatus() == GameState.Status.CHECK || state.getStatus() == GameState.Status.CHECKMATE) {
            PieceColor checkedColor = state.getCurrentPlayer();
            Piece king = chessGame.getBoard().getKing(checkedColor);
            if (king != null && king.getPosition() != null) {
                Position kp = king.getPosition();
                squarePanels[kp.getRow()][kp.getCol()].setInCheck(true);
            }
        }
    selectedPosition = null;
    legalMoves.clear();
    repaint();
    }
}
