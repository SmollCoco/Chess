import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import gui.ChessGUI;

public class Main {
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater() to ensure GUI runs on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system default for better appearance
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getLookAndFeel()
                );
                // Create and display the main chess GUI window
                ChessGUI chessGUI = new ChessGUI();
                chessGUI.setVisible(true);
            } catch (Exception e) {
                // Handle any startup exceptions gracefully
                System.err.println("Error starting Chess application: " + e.getMessage());
                e.printStackTrace();
                // Show error dialog to user
                JOptionPane.showMessageDialog(
                    null,
                    "Failed to start Chess application: " + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE
                );
                // Exit application on startup failure
                System.exit(1);
            }
        });
    }
}
