package gui;

import javax.swing.*;
import java.awt.*;

import pieces.*;

/**
 * Modal dialog to choose a pawn promotion piece with icons.
 */
public class PromotionDialog extends JDialog {
    private Class<? extends Piece> selected;

    public PromotionDialog(Window owner, PieceColor color) {
        super(owner, "Pawn Promotion", ModalityType.APPLICATION_MODAL);
    // Do not allow closing without a selection
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Promote pawn to:");
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(title, BorderLayout.NORTH);

        JPanel options = new JPanel(new GridLayout(1, 4, 10, 10));
        options.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int iconSize = 64;
        JButton q = makeButton("Queen", ImageLoader.getPieceImage("queen", color, iconSize), Queen.class);
        JButton r = makeButton("Rook", ImageLoader.getPieceImage("rook", color, iconSize), Rook.class);
        JButton b = makeButton("Bishop", ImageLoader.getPieceImage("bishop", color, iconSize), Bishop.class);
        JButton k = makeButton("Knight", ImageLoader.getPieceImage("knight", color, iconSize), Knight.class);

        options.add(q);
        options.add(r);
        options.add(b);
        options.add(k);
        add(options, BorderLayout.CENTER);

        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private JButton makeButton(String text, Icon icon, Class<? extends Piece> pieceClass) {
        JButton btn = new JButton(text, icon);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.addActionListener(e -> {
            selected = pieceClass;
            dispose();
        });
        btn.setToolTipText(text);
        return btn;
    }

    public Class<? extends Piece> choose() {
        setVisible(true);
        return selected; // may be null if closed; caller should default
    }
}
