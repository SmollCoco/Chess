package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.ChessGame;

public class ChessGUI extends JFrame {
    private ChessGame chessGame;
    private BoardPanel boardPanel;
    private GameInfoPanel gameInfoPanel;
    
    public ChessGUI() {
        // Initialize the chess game
        this.chessGame = new ChessGame();
        // Set up the main window
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        // Create and add components
        createMenuBar();
        createPanels();
        // Pack and center the window
        pack();
        setLocationRelativeTo(null); // Center on screen
        // Make visible
        setVisible(true);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        // Create Game menu
        JMenu gameMenu = new JMenu("Game");
        // New Game menu item
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });
        // Exit menu item
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // Add items to menu
        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        // Add menu to menu bar
        menuBar.add(gameMenu);
        // Set menu bar
        setJMenuBar(menuBar);
    }
    
    private void createPanels() {
        // Create board panel (center)
        boardPanel = new BoardPanel(chessGame);
        add(boardPanel, BorderLayout.CENTER);
        // Create game info panel (east)
        gameInfoPanel = new GameInfoPanel(chessGame);
        add(gameInfoPanel, BorderLayout.EAST);
    }
    
    private void startNewGame() {
        // Reset the game
        chessGame.resetGame();
        // Refresh the display
        boardPanel.refreshBoard();
        gameInfoPanel.updateDisplay();
        // Show message
        JOptionPane.showMessageDialog(this, "New game started!", "New Game", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void updateDisplay() {
        // Update both panels when game state changes
        boardPanel.refreshBoard();
        gameInfoPanel.updateDisplay();
    }
}
