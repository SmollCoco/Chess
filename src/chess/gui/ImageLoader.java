package gui;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import pieces.PieceColor;

public class ImageLoader {
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();
    private static final int PIECE_SIZE = 64; // Size of the piece images
    /**
     * Load and cache piece images
     */
    public static ImageIcon getPieceImage(String pieceName, PieceColor color) {
        String key = color.toString().toLowerCase() + "_" + pieceName.toLowerCase();
        
        if (imageCache.containsKey(key)) {
            return imageCache.get(key);
        }
        
        try {
            // Try to load from resources
            String imagePath = "/resources/images/" + key + ".png";
            InputStream is = ImageLoader.class.getResourceAsStream(imagePath);
            
            if (is != null) {
                BufferedImage originalImage = ImageIO.read(is);
                // Scale the image to fit the square
                Image scaledImage = originalImage.getScaledInstance(PIECE_SIZE, PIECE_SIZE, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);
                imageCache.put(key, icon);
                return icon;
            } else {
                // If image not found, return null (will fall back to symbols)
                System.out.println("Image not found: " + imagePath);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error loading image for " + key + ": " + e.getMessage());
            return null;
        }
    }
    /**
     * Check if images are available for pieces
     */
    public static boolean areImagesAvailable() {
        // Check if at least one piece image exists
        InputStream is = ImageLoader.class.getResourceAsStream("/resources/images/white_king.png");
        return is != null;
    }
    
    /**
     * Clear the image cache (useful for reloading)
     */
    public static void clearCache() {
        imageCache.clear();
    }
}
