package gui;

import javax.swing.*;
import java.awt.*;

import board.Square;
import pieces.Piece;

public class SquarePanel extends JPanel {
    private Square square;
    private boolean isHighlighted;
    
    // Colors for chess board
    private static final Color LIGHT_COLOR = new Color(240, 217, 181);
    private static final Color DARK_COLOR = new Color(181, 136, 99);
    private static final Color HIGHLIGHT_COLOR = new Color(255, 255, 0, 128); // Semi-transparent yellow
    
    public SquarePanel(Square square) {
        this.square = square;
        this.isHighlighted = false;
        
        setPreferredSize(new Dimension(80, 80));
        setOpaque(true);
    }
    
    public void setSquare(Square square) {
        this.square = square;
        repaint();
    }
    
    public void setHighlighted(boolean highlighted) {
        this.isHighlighted = highlighted;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable antialiasing for smooth text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Draw square background
        Color backgroundColor = square.isLight() ? LIGHT_COLOR : DARK_COLOR;
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw highlight overlay if selected
        if (isHighlighted) {
            g2d.setColor(HIGHLIGHT_COLOR);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Draw piece symbol if square is occupied
        Piece piece = square.getPiece();
        if (piece != null) {
            drawPiece(g2d, piece);
        }
        
        g2d.dispose();
    }
    
    private void drawPiece(Graphics2D g2d, Piece piece) {
        // Set font for piece symbols
        Font pieceFont = new Font("Serif", Font.PLAIN, 48);
        g2d.setFont(pieceFont);
        
        // Set color for piece text (high contrast)
        g2d.setColor(Color.BLACK);
        
        // Get piece symbol
        String symbol = piece.getSymbol();
        
        // Center the text in the square
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(symbol);
        int textHeight = fm.getAscent();
        
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2 - fm.getDescent();
        
        g2d.drawString(symbol, x, y);
    }
}
