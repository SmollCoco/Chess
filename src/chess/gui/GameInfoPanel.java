package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import game.ChessGame;
import game.GameState;

public class GameInfoPanel extends JPanel {
    private ChessGame chessGame;
    private JLabel statusLabel;
    private JLabel turnLabel;
    private JLabel moveLabel;
    
    public GameInfoPanel(ChessGame chessGame) {
        this.chessGame = chessGame;
        setupPanel();
        createLabels();
        updateDisplay();
    }
    
    private void setupPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200, 0));
        // Add titled border
        TitledBorder border = BorderFactory.createTitledBorder("Game Information");
        setBorder(border);
        // Add some padding
        setBorder(BorderFactory.createCompoundBorder(
            border,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }
    
    private void createLabels() {
        // Status label
        statusLabel = new JLabel("Status: ");
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(statusLabel);
        add(Box.createVerticalStrut(10)); // Spacing
        // Turn label
        turnLabel = new JLabel("Turn: ");
        turnLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(turnLabel);
        add(Box.createVerticalStrut(10)); // Spacing
        // Move label
        moveLabel = new JLabel("Move: ");
        moveLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(moveLabel);
        // Add flexible space at bottom
        add(Box.createVerticalGlue());
    }
    
    public void updateDisplay() {
        GameState gameState = chessGame.getGameState();
        // Update status with color coding
        String statusText = "Status: " + getStatusMessage(gameState);
        statusLabel.setText(statusText);
        statusLabel.setForeground(getStatusColor(gameState));
        // Update turn
        String currentPlayer = gameState.getCurrentPlayer().toString();
        turnLabel.setText("Turn: " + currentPlayer);
        turnLabel.setForeground(Color.BLACK);
        // Update move count
        moveLabel.setText("Move: " + gameState.getMoveCount());
        moveLabel.setForeground(Color.BLACK);
        repaint();
    }
    
    private String getStatusMessage(GameState gameState) {
        switch (gameState.getStatus()) {
            case PLAYING:
                return "Playing";
            case CHECK:
                return gameState.getCurrentPlayer() + " in Check";
            case CHECKMATE:
                String winner = gameState.getCurrentPlayer().opposite().toString();
                return "Checkmate - " + winner + " Wins!";
            case STALEMATE:
                return "Stalemate - Draw";
            case DRAW:
                return "Draw";
            default:
                return "Unknown";
        }
    }
    
    private Color getStatusColor(GameState gameState) {
        switch (gameState.getStatus()) {
            case PLAYING:
                return Color.BLACK;
            case CHECK:
                return Color.ORANGE;
            case CHECKMATE:
                return Color.RED;
            case STALEMATE:
            case DRAW:
                return Color.BLUE;
            default:
                return Color.BLACK;
        }
    }
}
