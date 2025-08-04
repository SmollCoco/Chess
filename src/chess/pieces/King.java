package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Position;
import board.ChessBoard;

public class King extends Piece {
    public King(PieceColor color, Position pos) {
        super(color, pos);
    }

    @Override
    public List<Position> getPossibleMoves(ChessBoard board) {
        List<Position> res = new ArrayList<>();

        int row = this.pos.getRow();
        int col = this.pos.getCol();

        // Moves one square in any direction (8 possible moves)
        int[] deltaRows = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] deltaCols = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int deltaRow = deltaRows[i];
            int deltaCol = deltaCols[i];
            
            if (board.isValidPosition(row + deltaRow, col + deltaCol)) {
                Position deltaPos = this.pos.add(deltaRow, deltaCol);
                if (board.getPiece(deltaPos) == null) {
                    res.add(deltaPos);
                } else {
                    if (this.color != board.getPiece(deltaPos).color) {
                        res.add(deltaPos);
                    }
                }
            }
        }

        return res;
    }

    @Override
    public String getSymbol() {
        return this.color == PieceColor.WHITE ? "♔" : "♚";
    }

    @Override
    public King copy() {
        King newKing = new King(this.color, new Position(this.pos.getRow(), this.pos.getCol()));
        newKing.hasMoved = this.hasMoved;
        return newKing;
    }
}
