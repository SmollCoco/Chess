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

        // Add castling moves
        res.addAll(getCastlingMoves(board));

        return res;
    }

    /**
     * Get possible castling moves for this king
     */
    public List<Position> getCastlingMoves(ChessBoard board) {
        List<Position> castlingMoves = new ArrayList<>();
        
        // Can't castle if king has moved
        if (this.hasMoved) {
            return castlingMoves;
        }
        
        int row = this.pos.getRow();
        
        // Check kingside castling (short castling)
        if (canCastleKingside(board)) {
            castlingMoves.add(new Position(row, 6));
        }
        
        // Check queenside castling (long castling)
        if (canCastleQueenside(board)) {
            castlingMoves.add(new Position(row, 2));
        }
        
        return castlingMoves;
    }

    /**
     * Check if kingside castling is possible
     */
    private boolean canCastleKingside(ChessBoard board) {
        int row = this.pos.getRow();
        
        // Check if rook is in position and hasn't moved
        Piece rook = board.getPiece(new Position(row, 7));
        if (rook == null || !(rook instanceof Rook) || rook.getHasMoved()) {
            return false;
        }
        
        // Check if squares between king and rook are empty
        for (int col = 5; col <= 6; col++) {
            if (board.getPiece(new Position(row, col)) != null) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Check if queenside castling is possible
     */
    private boolean canCastleQueenside(ChessBoard board) {
        int row = this.pos.getRow();
        
        // Check if rook is in position and hasn't moved
        Piece rook = board.getPiece(new Position(row, 0));
        if (rook == null || !(rook instanceof Rook) || rook.getHasMoved()) {
            return false;
        }
        
        // Check if squares between king and rook are empty
        for (int col = 1; col <= 3; col++) {
            if (board.getPiece(new Position(row, col)) != null) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public String getSymbol() {
        return this.color == PieceColor.WHITE ? "♔" : "♚";
    }

    @Override
    public String getPieceName() {
        return "king";
    }

    @Override
    public King copy() {
        King newKing = new King(this.color, new Position(this.pos.getRow(), this.pos.getCol()));
        newKing.setHasMoved(this.hasMoved);
        return newKing;
    }
}
