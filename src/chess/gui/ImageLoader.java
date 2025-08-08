package gui;

import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import pieces.PieceColor;

public class ImageLoader {
    private static final int DEFAULT_PIECE_SIZE = 64;
    private static final int MAX_ICON_CACHE = 64; // cap sized icons to bound memory

    // Base images cache (original PNGs)
    private static final Map<String, BufferedImage> baseImageCache = new LinkedHashMap<>(16, 0.75f, true);

    // LRU icon cache by key@size
    private static final Map<String, ImageIcon> imageCache = new LinkedHashMap<String, ImageIcon>(32, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, ImageIcon> eldest) {
            return size() > MAX_ICON_CACHE;
        }
    };

    /**
     * Load and cache piece images at default size (backward compatible).
     */
    public static ImageIcon getPieceImage(String pieceName, PieceColor color) {
        return getPieceImage(pieceName, color, DEFAULT_PIECE_SIZE);
    }

    /**
     * Load and cache piece images scaled to the requested size.
     * Falls back to PNGs from classpath at /resources/images.
     */
    public static ImageIcon getPieceImage(String pieceName, PieceColor color, int size) {
        if (pieceName == null || color == null || size <= 0) return null;

        String baseKey = color.toString().toLowerCase() + "_" + pieceName.toLowerCase();
        String cacheKey = baseKey + "@" + size;

        ImageIcon cached = imageCache.get(cacheKey);
        if (cached != null) return cached;

        try {
            BufferedImage src = loadBaseImage(baseKey);
            if (src == null) {
                return null;
            }
            BufferedImage scaled = scaleImage(src, size, size);
            ImageIcon icon = new ImageIcon(scaled);
            imageCache.put(cacheKey, icon);
            return icon;
        } catch (Exception e) {
            System.out.println("Error loading image for " + baseKey + ": " + e.getMessage());
            return null;
        }
    }

    private static BufferedImage loadBaseImage(String baseKey) {
        BufferedImage img = baseImageCache.get(baseKey);
        if (img != null) return img;
        String imagePath = "/resources/images/" + baseKey + ".png";
        try (InputStream is = ImageLoader.class.getResourceAsStream(imagePath)) {
            if (is != null) {
                img = ImageIO.read(is);
                baseImageCache.put(baseKey, img);
                return img;
            } else {
                System.out.println("Image not found: " + imagePath);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error reading image: " + imagePath + " - " + e.getMessage());
            return null;
        }
    }

    private static BufferedImage scaleImage(BufferedImage src, int width, int height) {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dst.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(src, 0, 0, width, height, null);
        g2.dispose();
        return dst;
    }

    /**
     * Check if images are available for pieces.
     */
    public static boolean areImagesAvailable() {
        try (InputStream is = ImageLoader.class.getResourceAsStream("/resources/images/white_king.png")) {
            return is != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Clear the image cache (useful for reloading when square size changes).
     */
    public static void clearCache() {
        imageCache.clear();
        baseImageCache.clear();
    }

    /**
     * Preload and scale all piece icons for the given size to avoid first-use lag.
     */
    public static void warmCache(int size) {
        if (size <= 0) return;
        String[] names = {"king", "queen", "rook", "bishop", "knight", "pawn"};
        for (String name : names) {
            // Preload both colors
            getPieceImage(name, PieceColor.WHITE, size);
            getPieceImage(name, PieceColor.BLACK, size);
        }
    }
}
