package gui;

import java.awt.Color;
import java.awt.BasicStroke;

/**
 * Centralized UI constants for colors, sizes, and proportions.
 * Keep visuals consistent and high-DPI friendly.
 */
public final class UIConstants {
    private UIConstants() {}

    // Board sizing
    public static final int BOARD_PREFERRED_SIZE = 800;
    public static final int BOARD_MIN_SIZE = 400;
    public static final int BOARD_MARGIN = 10; // used when computing preferred square size

    // Square sizing
    public static final int SQUARE_DEFAULT_SIZE = 100;
    public static final int SQUARE_MIN_SIZE = 50;
    public static final double PIECE_ICON_SCALE = 0.8; // portion of square
    public static final int PIECE_ICON_MIN = 24;

    // Board colors
    public static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    public static final Color DARK_SQUARE  = new Color(181, 136, 99);

    // Highlights
    public static final Color SELECT_HIGHLIGHT = new Color(255, 255, 0, 128);
    public static final Color LEGAL_MOVE_FILL  = new Color(0, 255, 0, 100);
    public static final Color LEGAL_MOVE_BORDER = new Color(0, 200, 0, 150);
    public static final Color CAPTURE_BORDER   = new Color(255, 100, 100, 180);
    public static final Color LAST_MOVE_BORDER = new Color(50, 120, 255, 160);
    public static final Color INCHECK_TINT     = new Color(255, 0, 0, 90);
    public static final Color INCHECK_BORDER   = new Color(200, 0, 0, 160);

    // Reusable strokes to avoid allocating per paint
    public static final BasicStroke STROKE_THIN   = new BasicStroke(2f);
    public static final BasicStroke STROKE_MEDIUM = new BasicStroke(3f);
    public static final BasicStroke STROKE_THICK  = new BasicStroke(6f);
}
