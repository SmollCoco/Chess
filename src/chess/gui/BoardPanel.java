package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game.ChessGame;
import board.Position;

public class BoardPanel extends JPanel {
    private ChessGame chessGame;
    private SquarePanel[][] squarePanels;
    private Position selectedPosition;
    
    public BoardPanel(ChessGame chessGame) {
        this.chessGame = chessGame;
        this.selectedPosition = null;
        
        // Set up the panel
        setLayout(new GridLayout(8, 8));
        setPreferredSize(new Dimension(640, 640)); // 80x80 per square
        
        // Create the square panels
        createSquarePanels();
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
        }
        // Ignore clicks on empty squares or opponent pieces
    }
    
    private void handleSecondClick(Position position) {
        if (position.equals(selectedPosition)) {
            // Clicking same square - deselect
            deselectPiece();
        } else {
            // Try to make the move
            boolean moveSuccessful = chessGame.makeMove(selectedPosition, position);
            
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
    
    private void deselectPiece() {
        if (selectedPosition != null) {
            highlightSquare(selectedPosition, false);
            selectedPosition = null;
        }
    }
    
    private void highlightSquare(Position position, boolean highlight) {
        squarePanels[position.getRow()][position.getCol()].setHighlighted(highlight);
    }
    
    public void refreshBoard() {
        // Update all square panels to reflect current board state
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squarePanels[row][col].setSquare(chessGame.getBoard().getSquare(row, col));
                squarePanels[row][col].setHighlighted(false); // Clear highlights
            }
        }
        selectedPosition = null;
        repaint();
    }
}
