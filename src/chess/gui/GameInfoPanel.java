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
    private DefaultListModel<String> moveListModel;
    private JList<String> moveList;
    private JButton undoButton;
    
    public GameInfoPanel(ChessGame chessGame) {
        this.chessGame = chessGame;
        setupPanel();
        createLabels();
        updateDisplay();
    }
    
    private void setupPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250, 0)); // Increased width from 200 to 250
        setBackground(new Color(248, 248, 255)); // Light background color
        
        // Add improved titled border
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createRaisedBevelBorder(), 
            "Game Information",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(70, 70, 70)
        );
        setBorder(border);
        // Add some padding
        setBorder(BorderFactory.createCompoundBorder(
            border,
            BorderFactory.createEmptyBorder(15, 15, 15, 15) // Increased padding
        ));
    }
    
    private void createLabels() {
        // Create improved fonts
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font boldFont = new Font("Arial", Font.BOLD, 14);
        
        // Status label
        statusLabel = new JLabel("Status: ");
        statusLabel.setFont(boldFont);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(statusLabel);
        add(Box.createVerticalStrut(15)); // Increased spacing
        
        // Turn label
        turnLabel = new JLabel("Turn: ");
        turnLabel.setFont(labelFont);
        turnLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(turnLabel);
        add(Box.createVerticalStrut(15)); // Increased spacing
        
        // Move label
        moveLabel = new JLabel("Move: ");
        moveLabel.setFont(labelFont);
        moveLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(moveLabel);
        add(Box.createVerticalStrut(10));

        // Undo button
        undoButton = new JButton("Undo");
        undoButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        undoButton.addActionListener(e -> {
            if (chessGame.undoLastMove()) {
                // Refresh the main UI on EDT
                SwingUtilities.invokeLater(() -> {
                    Container top = SwingUtilities.getWindowAncestor(this);
                    if (top instanceof ChessGUI) {
                        ((ChessGUI) top).updateDisplay();
                    } else {
                        // Fallback: refresh this panel directly
                        updateDisplay();
                    }
                });
            }
        });
        add(undoButton);
        add(Box.createVerticalStrut(10));

    // Move list
    moveListModel = new DefaultListModel<>();
    moveList = new JList<>(moveListModel);
    moveList.setVisibleRowCount(12);
    moveList.setFont(new Font("Consolas", Font.PLAIN, 12));
    JScrollPane scroll = new JScrollPane(moveList);
    scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
    add(scroll);
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
        // Update move history (paired with move numbers)
        moveListModel.clear();
        for (String line : chessGame.getPairedMoveHistory()) {
            moveListModel.addElement(line);
        }
        if (!moveListModel.isEmpty()) {
            moveList.ensureIndexIsVisible(moveListModel.size() - 1);
        }
        // Enable/disable undo button
        if (undoButton != null) {
            undoButton.setEnabled(chessGame.canUndo());
        }
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
