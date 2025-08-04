package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Position;
import board.ChessBoard;

public class Rook extends Piece {
    public Rook(PieceColor color, Position pos) {
        super(color, pos);
    }

    @Override
    public List<Position> getPossibleMoves(ChessBoard board) {
        List<Position> res = new ArrayList<>();

        int row = this.pos.getRow();
        int col = this.pos.getCol();

        // Moves horizontally or vertically any number of squares
        // Moving right
        int deltaCol = 1;
        while (board.isValidPosition(row, col + deltaCol)) {
            Position deltaPos = this.pos.add(0, deltaCol);
            if (board.getPiece(deltaPos) == null) {
                res.add(deltaPos);
            } else {
                if (this.color != board.getPiece(deltaPos).color) {
                    res.add(deltaPos);
                }
                break;
            }
            ++deltaCol;
        }
        // Moving left
        deltaCol = -1;
        while (board.isValidPosition(row, col + deltaCol)) {
            Position deltaPos = this.pos.add(0, deltaCol);
            if (board.getPiece(deltaPos) == null) {
                res.add(deltaPos);
            } else {
                if (this.color != board.getPiece(deltaPos).color) {
                    res.add(deltaPos);
                }
                break;
            }
            --deltaCol;
        }
        // Moving up
        int deltaRow = -1;
        while (board.isValidPosition(row + deltaRow, col)) {
            Position deltaPos = this.pos.add(deltaRow, 0);
            if (board.getPiece(deltaPos) == null) {
                res.add(deltaPos);
            } else {
                if (this.color != board.getPiece(deltaPos).color) {
                    res.add(deltaPos);
                }
                break;
            }
            --deltaRow;
        }
        // Moving down
        deltaRow = 1;
        while (board.isValidPosition(row + deltaRow, col)) {
            Position deltaPos = this.pos.add(deltaRow, 0);
            if (board.getPiece(deltaPos) == null) {
                res.add(deltaPos);
            } else {
                if (this.color != board.getPiece(deltaPos).color) {
                    res.add(deltaPos);
                }
                break;
            }
            ++deltaRow;
        }

        return res;
    }

    @Override
    public String getSymbol() {
        return this.color == PieceColor.WHITE ? "♖" : "♜";
    }

    @Override
    public Rook copy() {
        Rook newRook = new Rook(this.color, new Position(this.pos.getRow(), this.pos.getCol()));
        newRook.hasMoved = this.hasMoved;
        return newRook;
    }
}
