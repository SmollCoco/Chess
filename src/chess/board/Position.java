package board;

import java.util.Objects;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isValid() {
        return this.row >= 0 && this.row < 8 && this.col >= 0 && this.col < 8;
    }

    public Position add(int deltaRow, int deltaCol) {
        return new Position(this.row + deltaRow, this.col + deltaCol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        // Convert to chess notation (e.g., "e4", "a8")
        char file = (char)('a' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }
}
