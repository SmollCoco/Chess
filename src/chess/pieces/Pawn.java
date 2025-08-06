package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Position;
import board.ChessBoard;
import game.GameState;

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

    /**
     * Get possible moves including en passant for this pawn
     */
    public List<Position> getPossibleMoves(ChessBoard board, GameState gameState) {
        List<Position> res = getPossibleMoves(board);
        
        // Add en passant moves
        res.addAll(getEnPassantMoves(board, gameState));
        
        return res;
    }

    /**
     * Get possible en passant capture moves
     */
    public List<Position> getEnPassantMoves(ChessBoard board, GameState gameState) {
        List<Position> enPassantMoves = new ArrayList<>();
        
        // En passant is only possible for pawns on the 5th rank (for white) or 4th rank (for black)
        int expectedRow = this.color == PieceColor.WHITE ? 3 : 4;
        if (this.pos.getRow() != expectedRow) {
            return enPassantMoves;
        }
        
        // Check if the last move was a pawn moving two squares
        Position lastMoveFrom = gameState.getLastMoveFrom();
        Position lastMoveTo = gameState.getLastMoveTo();
        
        if (lastMoveFrom == null || lastMoveTo == null) {
            return enPassantMoves;
        }
        
        // The last moved piece must be a pawn
        Piece lastMovedPiece = board.getPiece(lastMoveTo);
        if (lastMovedPiece == null || !(lastMovedPiece instanceof Pawn)) {
            return enPassantMoves;
        }
        
        // The pawn must have moved two squares
        if (Math.abs(lastMoveFrom.getRow() - lastMoveTo.getRow()) != 2) {
            return enPassantMoves;
        }
        
        // The last moved pawn must be adjacent to this pawn
        if (lastMoveTo.getRow() != this.pos.getRow() || 
            Math.abs(lastMoveTo.getCol() - this.pos.getCol()) != 1) {
            return enPassantMoves;
        }
        
        // The last moved pawn must be of opposite color
        if (lastMovedPiece.getColor() == this.color) {
            return enPassantMoves;
        }
        
        // Add the en passant capture move
        int deltaRow = this.color == PieceColor.WHITE ? -1 : 1;
        Position enPassantTarget = new Position(this.pos.getRow() + deltaRow, lastMoveTo.getCol());
        enPassantMoves.add(enPassantTarget);
        
        return enPassantMoves;
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
        newPawn.setHasMoved(this.hasMoved);
        return newPawn;
    }

    /**
     * Check if this pawn can be promoted (reached the opposite end of the board)
     */
    public boolean canPromote() {
        int promotionRow = this.color == PieceColor.WHITE ? 0 : 7;
        return this.pos.getRow() == promotionRow;
    }
}
