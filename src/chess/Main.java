import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import gui.ChessGUI;

public class Main {
    public static void main(String[] args) {
        // Ensure GUI runs on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look & feel to system default for better appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                // Launch GUI
                ChessGUI chessGUI = new ChessGUI();
                chessGUI.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error starting Chess application: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                    null,
                    "Failed to start Chess application: " + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        });
    }
}
