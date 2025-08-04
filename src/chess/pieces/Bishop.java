package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Position;
import board.ChessBoard;

public class Bishop extends Piece {
    public Bishop(PieceColor color, Position pos) {
        super(color, pos);
    }

    @Override
    public List<Position> getPossibleMoves(ChessBoard board) {
        List<Position> res = new ArrayList<>();

        int row = this.pos.getRow();
        int col = this.pos.getCol();

        // Moves diagonally any number of squares
        // Moving up-right
        int deltaRow = -1;
        int deltaCol = 1;
        while (board.isValidPosition(row + deltaRow, col + deltaCol)) {
            Position deltaPos = this.pos.add(deltaRow, deltaCol);
            if (board.getPiece(deltaPos) == null) {
                res.add(deltaPos);
            } else {
                if (this.color != board.getPiece(deltaPos).color) {
                    res.add(deltaPos);
                }
                break;
            }
            --deltaRow;
            ++deltaCol;
        }
        // Moving up-left
        deltaRow = -1;
        deltaCol = -1;
        while (board.isValidPosition(row + deltaRow, col + deltaCol)) {
            Position deltaPos = this.pos.add(deltaRow, deltaCol);
            if (board.getPiece(deltaPos) == null) {
                res.add(deltaPos);
            } else {
                if (this.color != board.getPiece(deltaPos).color) {
                    res.add(deltaPos);
                }
                break;
            }
            --deltaRow;
            --deltaCol;
        }
        // Moving down-left
        deltaRow = 1;
        deltaCol = -1;
        while (board.isValidPosition(row + deltaRow, col + deltaCol)) {
            Position deltaPos = this.pos.add(deltaRow, deltaCol);
            if (board.getPiece(deltaPos) == null) {
                res.add(deltaPos);
            } else {
                if (this.color != board.getPiece(deltaPos).color) {
                    res.add(deltaPos);
                }
                break;
            }
            ++deltaRow;
            --deltaCol;
        }
        // Moving down-right
        deltaRow = 1;
        deltaCol = 1;
        while (board.isValidPosition(row + deltaRow, col + deltaCol)) {
            Position deltaPos = this.pos.add(deltaRow, deltaCol);
            if (board.getPiece(deltaPos) == null) {
                res.add(deltaPos);
            } else {
                if (this.color != board.getPiece(deltaPos).color) {
                    res.add(deltaPos);
                }
                break;
            }
            ++deltaRow;
            ++deltaCol;
        }

        return res;
    }

    @Override
    public String getSymbol() {
        return this.color == PieceColor.WHITE ? "♗" : "♝";
    }

    @Override
    public Bishop copy() {
        Bishop newBishop = new Bishop(this.color, new Position(this.pos.getRow(), this.pos.getCol()));
        newBishop.hasMoved = this.hasMoved;
        return newBishop;
    }
}
