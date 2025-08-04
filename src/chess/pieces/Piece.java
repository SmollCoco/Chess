package pieces;

import java.util.List;

import board.ChessBoard;
import board.Position;

public abstract class Piece {
    protected PieceColor color;
    protected Position pos;
    protected boolean hasMoved;

    public Piece(PieceColor color, Position pos) {
        this.color = color;
        this.pos = pos;
        this.hasMoved = false;
    }

    public abstract List<Position> getPossibleMoves(ChessBoard board);

    public abstract String getSymbol();

    public abstract Piece copy();

    public boolean isOpponent(Piece other) {
        return this.color != other.color;
    }

    public boolean isFriendly(Piece other) {
        return this.color == other.color;
    }

    public void setPosition(Position new_pos) {
        this.pos = new_pos;
        this.hasMoved = true;
    }

    public PieceColor getColor() {
        return this.color;
    }

    public Position getPosition() {
        return this.pos;
    }

    public boolean getHasMoved() {
        return this.hasMoved;
    }
}
