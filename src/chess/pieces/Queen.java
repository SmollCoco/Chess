package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Position;
import board.ChessBoard;

public class Queen extends Piece {
    public Queen(PieceColor color, Position pos) {
        super(color, pos);
    }

    @Override
    public List<Position> getPossibleMoves(ChessBoard board) {
        List<Position> res = new ArrayList<>();

        int row = this.pos.getRow();
        int col = this.pos.getCol();

        // Combines rook and bishop movement
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
        // Moving up-right
        deltaRow = -1;
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
        return this.color == PieceColor.WHITE ? "♕" : "♛";
    }

    @Override
    public Queen copy() {
        Queen newQueen = new Queen(this.color, new Position(this.pos.getRow(), this.pos.getCol()));
        newQueen.hasMoved = this.hasMoved;
        return newQueen;
    }
}
