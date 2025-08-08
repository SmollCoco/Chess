package gui;

import javax.swing.*;
import java.awt.*;

import board.Square;
import pieces.Piece;

public class SquarePanel extends JPanel {
    private Square square;
    private boolean isHighlighted;
    private boolean isLegalMoveTarget;
    private boolean isLastMove;
    private boolean isInCheck;
    
    // Colors for chess board
    private static final Color LIGHT_COLOR = UIConstants.LIGHT_SQUARE;
    private static final Color DARK_COLOR = UIConstants.DARK_SQUARE;
    private static final Color HIGHLIGHT_COLOR = UIConstants.SELECT_HIGHLIGHT; // Semi-transparent yellow
    private static final Color LEGAL_MOVE_COLOR = UIConstants.LEGAL_MOVE_FILL; // Semi-transparent green
    
    public SquarePanel(Square square) {
        this.square = square;
        this.isHighlighted = false;
        this.isLegalMoveTarget = false;
        this.isLastMove = false;
    this.isInCheck = false;
        
    setPreferredSize(new Dimension(UIConstants.SQUARE_DEFAULT_SIZE, UIConstants.SQUARE_DEFAULT_SIZE));
        setOpaque(true);
        setDoubleBuffered(true);
    }
    
    @Override
    public Dimension getPreferredSize() {
        // Ensure square stays square even when resized
        if (getParent() != null) {
            Dimension parentSize = getParent().getSize();
            int size = Math.min(parentSize.width / 8, parentSize.height / 8);
            size = Math.max(size, UIConstants.SQUARE_MIN_SIZE); // Minimum size
            return new Dimension(size, size);
        }
        return new Dimension(UIConstants.SQUARE_DEFAULT_SIZE, UIConstants.SQUARE_DEFAULT_SIZE);
    }
    
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(UIConstants.SQUARE_MIN_SIZE, UIConstants.SQUARE_MIN_SIZE);
    }
    
    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
    
    public void setSquare(Square square) {
        this.square = square;
        repaint();
    }
    
    public void setHighlighted(boolean highlighted) {
        this.isHighlighted = highlighted;
        repaint();
    }
    
    public void setLegalMoveTarget(boolean isTarget) {
        this.isLegalMoveTarget = isTarget;
        repaint();
    }
    
    public void setLastMove(boolean isLast) {
        this.isLastMove = isLast;
        repaint();
    }
    
    public void setInCheck(boolean inCheck) {
        this.isInCheck = inCheck;
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
        
        // Draw last-move highlight (subtle outline)
        if (isLastMove) {
            g2d.setColor(UIConstants.LAST_MOVE_BORDER);
            g2d.setStroke(UIConstants.STROKE_MEDIUM);
            g2d.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
        }

        // Draw in-check overlay (soft red tint) for king's square
        if (isInCheck) {
            g2d.setColor(UIConstants.INCHECK_TINT);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(UIConstants.INCHECK_BORDER);
            g2d.setStroke(UIConstants.STROKE_MEDIUM);
            g2d.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
        }

        // Draw highlight overlay if selected
        if (isHighlighted) {
            g2d.setColor(HIGHLIGHT_COLOR);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Draw legal move indicator if this square is a legal move target
        if (isLegalMoveTarget) {
            g2d.setColor(LEGAL_MOVE_COLOR);
            if (square.isEmpty()) {
                // Draw a filled circle in the center for empty squares
                int circleSize = 25; // Increased size
                int x = (getWidth() - circleSize) / 2;
                int y = (getHeight() - circleSize) / 2;
                g2d.fillOval(x, y, circleSize, circleSize);
                
                // Add a darker border to the circle
                g2d.setColor(UIConstants.LEGAL_MOVE_BORDER);
                g2d.setStroke(UIConstants.STROKE_THIN);
                g2d.drawOval(x, y, circleSize, circleSize);
            } else {
                // Draw a thicker border around the square if it contains an opponent piece
                g2d.setColor(UIConstants.CAPTURE_BORDER); // Red for capture
                g2d.setStroke(UIConstants.STROKE_THICK);
                g2d.drawRect(3, 3, getWidth() - 6, getHeight() - 6);
            }
        }
        
        // Draw piece symbol if square is occupied
        Piece piece = square.getPiece();
        if (piece != null) {
            drawPiece(g2d, piece);
        }
        
        g2d.dispose();
    }
    
    private void drawPiece(Graphics2D g2d, Piece piece) {
    // Try to load piece image first, scaled to square size with padding
    int size = Math.min(getWidth(), getHeight());
        int iconSize = Math.max(UIConstants.PIECE_ICON_MIN, (int)(size * UIConstants.PIECE_ICON_SCALE)); // leave a small margin
    ImageIcon pieceImage = ImageLoader.getPieceImage(piece.getPieceName(), piece.getColor(), iconSize);
        if (pieceImage != null) {
            // Draw the image centered in the square
            int x = (getWidth() - pieceImage.getIconWidth()) / 2;
            int y = (getHeight() - pieceImage.getIconHeight()) / 2;
            g2d.drawImage(pieceImage.getImage(), x, y, this);
        } else {
            // Fall back to text symbols
            drawPieceSymbol(g2d, piece);
        }
    }
    
    private void drawPieceSymbol(Graphics2D g2d, Piece piece) {
    // Use cached font for piece symbols
    g2d.setFont(PieceSymbolFontHolder.FONT);
        
        // Set color for piece text with better contrast and shadows
        g2d.setColor(Color.WHITE); // White outline/shadow
        String symbol = piece.getSymbol();
        
        // Center the text in the square
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(symbol);
        int textHeight = fm.getAscent();
        
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2 - fm.getDescent();
        
        // Draw shadow/outline effect
        g2d.drawString(symbol, x + 2, y + 2); // Shadow
        g2d.drawString(symbol, x - 1, y - 1); // Outline
        g2d.drawString(symbol, x + 1, y - 1); // Outline
        g2d.drawString(symbol, x - 1, y + 1); // Outline
        g2d.drawString(symbol, x + 1, y + 1); // Outline
        
        // Draw the main text in black
        g2d.setColor(Color.BLACK);
        g2d.drawString(symbol, x, y);
    }
}

// Lazy-loaded shared font holder to avoid per-paint allocations
class PieceSymbolFontHolder {
    static final Font FONT = new Font("DejaVu Sans", Font.BOLD, 60);
}
