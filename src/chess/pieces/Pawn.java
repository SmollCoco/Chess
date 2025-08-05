package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Position;
import board.ChessBoard;

public class Pawn extends Piece {
    public Pawn(PieceColor color, Position pos) {
        super(color, pos);
    }

    @Override
    public List<Position> getPossibleMoves(ChessBoard board) {
        List<Position> res = new ArrayList<>();

        int row = this.pos.getRow();
        int col = this.pos.getCol();

        int deltaRow = this.color == PieceColor.WHITE ? -1 : 1;

        // Moves forward one square if unoccupied
        Position one_delta = this.pos.add(deltaRow, 0);
        if (board.isValidPosition(row + deltaRow, col) && board.getPiece(one_delta) == null) {
            res.add(one_delta);
        }

        // Moves forward two squares from starting position if both squares unoccupied
        Position two_delta = this.pos.add(2 * deltaRow, 0);
        if (
            !this.hasMoved 
            && board.isValidPosition(row + deltaRow, col) && board.getPiece(one_delta) == null 
            && board.isValidPosition(row + 2 * deltaRow, col) && board.getPiece(two_delta) == null
            ) {
            res.add(two_delta);
        }

        // Captures diagonally forward (one square)
        Position delta_right = this.pos.add(deltaRow, 1);
        if (board.isValidPosition(row + deltaRow, col + 1) && board.getPiece(delta_right) != null && board.getPiece(delta_right).getColor() != this.color) {
            res.add(delta_right);
        }
        Position delta_left = this.pos.add(deltaRow, -1);
        if (board.isValidPosition(row + deltaRow, col - 1) && board.getPiece(delta_left) != null && board.getPiece(delta_left).getColor() != this.color) {
            res.add(delta_left);
        }

        return res;
    }

    @Override
    public String getSymbol() {
        return this.color == PieceColor.WHITE ? "♙" : "♟";
    }

    @Override
    public String getPieceName() {
        return "pawn";
    }

    @Override
    public Pawn copy() {
        Pawn newPawn = new Pawn(this.color, new Position(this.pos.getRow(), this.pos.getCol()));
        newPawn.hasMoved = this.hasMoved;
        return newPawn;
    }
}
