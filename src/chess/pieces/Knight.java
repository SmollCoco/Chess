package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Position;
import board.ChessBoard;

public class Knight extends Piece {
    public Knight(PieceColor color, Position pos) {
        super(color, pos);
    }

    @Override
    public List<Position> getPossibleMoves(ChessBoard board) {
        List<Position> res = new ArrayList<>();

        int row = this.pos.getRow();
        int col = this.pos.getCol();

        // Up-Right 1
        int deltaRow = -2;
        int deltaCol = 1;
        Position next_pos = this.pos.add(deltaRow, deltaCol);
        if (
            board.isValidPosition(row + deltaRow, col + deltaCol)
            && (
                (
                    board.getPiece(next_pos) != null
                    && board.getPiece(next_pos).color != this.color
                ) 
                || 
                    board.getPiece(next_pos) == null
            )
        ) {
            res.add(this.pos.add(deltaRow, deltaCol));
        }

        // Up-Right 2
        deltaRow = -1;
        deltaCol = 2;
        next_pos = this.pos.add(deltaRow, deltaCol);
        if (
            board.isValidPosition(row + deltaRow, col + deltaCol)
            && (
                (
                    board.getPiece(next_pos) != null
                    && board.getPiece(next_pos).color != this.color
                ) 
                || 
                    board.getPiece(next_pos) == null
            )
        ) {
            res.add(this.pos.add(deltaRow, deltaCol));
        }

        // Up-Left 1
        deltaRow = -2;
        deltaCol = -1;
        next_pos = this.pos.add(deltaRow, deltaCol);
        if (
            board.isValidPosition(row + deltaRow, col + deltaCol)
            && (
                (
                    board.getPiece(next_pos) != null
                    && board.getPiece(next_pos).color != this.color
                ) 
                || 
                    board.getPiece(next_pos) == null
            )
        ) {
            res.add(this.pos.add(deltaRow, deltaCol));
        }

        // Up-Left 2
        deltaRow = -1;
        deltaCol = -2;
        next_pos = this.pos.add(deltaRow, deltaCol);
        if (
            board.isValidPosition(row + deltaRow, col + deltaCol)
            && (
                (
                    board.getPiece(next_pos) != null
                    && board.getPiece(next_pos).color != this.color
                ) 
                || 
                    board.getPiece(next_pos) == null
            )
        ) {
            res.add(this.pos.add(deltaRow, deltaCol));
        }

        // Down-Left 1
        deltaRow = 1;
        deltaCol = -2;
        next_pos = this.pos.add(deltaRow, deltaCol);
        if (
            board.isValidPosition(row + deltaRow, col + deltaCol)
            && (
                (
                    board.getPiece(next_pos) != null
                    && board.getPiece(next_pos).color != this.color
                ) 
                || 
                    board.getPiece(next_pos) == null
            )
        ) {
            res.add(this.pos.add(deltaRow, deltaCol));
        }

        // Down-Left 2
        deltaRow = 2;
        deltaCol = -1;
        next_pos = this.pos.add(deltaRow, deltaCol);
        if (
            board.isValidPosition(row + deltaRow, col + deltaCol)
            && (
                (
                    board.getPiece(next_pos) != null
                    && board.getPiece(next_pos).color != this.color
                ) 
                || 
                    board.getPiece(next_pos) == null
            )
        ) {
            res.add(this.pos.add(deltaRow, deltaCol));
        }

        // Down-Right 1
        deltaRow = 2;
        deltaCol = 1;
        next_pos = this.pos.add(deltaRow, deltaCol);
        if (
            board.isValidPosition(row + deltaRow, col + deltaCol)
            && (
                (
                    board.getPiece(next_pos) != null
                    && board.getPiece(next_pos).color != this.color
                ) 
                || 
                    board.getPiece(next_pos) == null
            )
        ) {
            res.add(this.pos.add(deltaRow, deltaCol));
        }

        // Down-Right 2
        deltaRow = 1;
        deltaCol = 2;
        next_pos = this.pos.add(deltaRow, deltaCol);
        if (
            board.isValidPosition(row + deltaRow, col + deltaCol)
            && (
                (
                    board.getPiece(next_pos) != null
                    && board.getPiece(next_pos).color != this.color
                ) 
                || 
                    board.getPiece(next_pos) == null
            )
        ) {
            res.add(this.pos.add(deltaRow, deltaCol));
        }

        return res;
    }

    @Override
    public String getSymbol() {
        return this.color == PieceColor.WHITE ? "♘" : "♞";
    }

    @Override
    public String getPieceName() {
        return "knight";
    }

    @Override
    public Knight copy() {
        Knight newKnight = new Knight(this.color, new Position(this.pos.getRow(), this.pos.getCol()));
        newKnight.setHasMoved(this.hasMoved);
        return newKnight;
    }
}
