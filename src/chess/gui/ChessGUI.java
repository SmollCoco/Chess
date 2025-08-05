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
        setTitle("Chess Game - Professional Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true); // Allow resizing
        setLayout(new BorderLayout());
        
        // Set minimum size
        setMinimumSize(new Dimension(1100, 900));
        
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
        menuBar.setBackground(new Color(240, 240, 245));
        
        // Create Game menu with improved styling
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setFont(new Font("Arial", Font.BOLD, 14));
        
        // New Game menu item
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.setFont(new Font("Arial", Font.PLAIN, 12));
        newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });
        // Exit menu item
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(new Font("Arial", Font.PLAIN, 12));
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
        // Create board panel (center) with reduced padding and centering
        boardPanel = new BoardPanel(chessGame);
        
        // Create a container that centers the board and maintains square shape
        JPanel boardContainer = new JPanel();
        boardContainer.setLayout(new BoxLayout(boardContainer, BoxLayout.Y_AXIS));
        boardContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Reduced from 20 to 10
        
        // Add vertical glue to center the board vertically
        boardContainer.add(Box.createVerticalGlue());
        
        // Create a panel to center the board horizontally
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Remove gaps
        centerPanel.add(boardPanel);
        boardContainer.add(centerPanel);
        
        // Add vertical glue to center the board vertically
        boardContainer.add(Box.createVerticalGlue());
        
        add(boardContainer, BorderLayout.CENTER);
        
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
