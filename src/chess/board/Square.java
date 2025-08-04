package board;

import pieces.Piece;

public class Square {
    private Piece piece;
    private Position pos;
    private boolean isLight;

    public Square(Piece piece, Position pos) {
        this.piece = piece;
        this.pos = pos;
        this.isLight = (this.pos.getRow() + this.pos.getCol()) % 2 == 0;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece new_piece) {
        this.piece = new_piece;
    }

    public void clear() {
        this.piece = null;
    }

    public Position getPosition() {
        return this.pos;
    }

    public boolean isLight() {
        return this.isLight;
    }

    public boolean isEmpty() {
        return this.piece == null;
    }
}
