package game;

/**
 * MoveValidator centralizes chess rules validation.
 * Contracts:
 * - isValidMove: verifies piece ownership, move legality (including en passant/castling), and that own king is not left in check.
 * - getLegalMoves: returns only moves that do not expose own king to check.
 * - isKingInCheck / isCheckmate / isStalemate: board-state evaluations without mutating original board.
 * Notes:
 * - Uses board.copy() to simulate moves safely.
 * - Castling validated for rook/king unmoved, empty path, and not moving through/into check.
 */

import java.util.ArrayList;
import java.util.List;

import board.ChessBoard;
import board.Position;
import pieces.Piece;
import pieces.PieceColor;
import pieces.King;
import pieces.Pawn;
import pieces.Rook;

public class MoveValidator {
    
    public static boolean isValidMove(ChessBoard board, Position from, Position to, PieceColor player, GameState gameState) {
        // Source square must contain a piece of the current player
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != player) {
            return false;
        }
        
        // Check if this is a special move
        if (isSpecialMove(board, from, to, piece, gameState)) {
            return isValidSpecialMove(board, from, to, piece, gameState);
        }
        
        // Destination must be in piece's possible moves list
        List<Position> possibleMoves = piece.getPossibleMoves(board);
        if (!possibleMoves.contains(to)) {
            return false;
        }
        
        // Move must not expose own king to check
        if (wouldMoveExposeKing(board, from, to, player)) {
            return false;
        }
        return true;
    }
    
    /**
     * Check if this is a special move (castling or en passant)
     */
    private static boolean isSpecialMove(ChessBoard board, Position from, Position to, Piece piece, GameState gameState) {
        // Check for castling
        if (piece instanceof King && Math.abs(to.getCol() - from.getCol()) == 2) {
            return true;
        }
        
        // Check for en passant
        if (piece instanceof Pawn) {
            Pawn pawn = (Pawn) piece;
            List<Position> enPassantMoves = pawn.getEnPassantMoves(board, gameState);
            if (enPassantMoves.contains(to)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validate special moves (castling and en passant)
     */
    private static boolean isValidSpecialMove(ChessBoard board, Position from, Position to, Piece piece, GameState gameState) {
        // Handle castling
        if (piece instanceof King && Math.abs(to.getCol() - from.getCol()) == 2) {
            return isValidCastling(board, from, to, (King) piece);
        }
        
        // Handle en passant
        if (piece instanceof Pawn) {
            Pawn pawn = (Pawn) piece;
            List<Position> enPassantMoves = pawn.getEnPassantMoves(board, gameState);
            if (enPassantMoves.contains(to)) {
                return isValidEnPassant(board, from, to, pawn, gameState);
            }
        }
        
        return false;
    }
    
    /**
     * Validate castling move
     */
    private static boolean isValidCastling(ChessBoard board, Position from, Position to, King king) {
        // King must not have moved
        if (king.getHasMoved()) {
            return false;
        }

        // King must not be in check
        if (isKingInCheck(board, king.getColor())) {
            return false;
        }

        // Determine side and rook positions
        boolean isKingside = to.getCol() > from.getCol();
        int row = from.getRow();
        int rookCol = isKingside ? 7 : 0;
        Position rookPos = new Position(row, rookCol);

        // Rook must exist, be a Rook, same color, and not have moved
        Piece rook = board.getPiece(rookPos);
        if (rook == null || !(rook instanceof Rook) || rook.getColor() != king.getColor() || rook.getHasMoved()) {
            return false;
        }

        // Squares between king and rook must be empty
        int startCol = Math.min(from.getCol(), rookCol) + 1;
        int endCol = Math.max(from.getCol(), rookCol) - 1;
        for (int c = startCol; c <= endCol; c++) {
            if (board.getPiece(new Position(row, c)) != null) {
                return false;
            }
        }

        // King cannot pass through check or end up in check
        int step = to.getCol() > from.getCol() ? 1 : -1;
        int currentCol = from.getCol();
        
        // Check each square the king passes through (including destination)
        while (currentCol != to.getCol() + step) {
            currentCol += step;
            Position checkPos = new Position(from.getRow(), currentCol);
            
            // Simulate king being on this square
            ChessBoard tempBoard = board.copy();
            tempBoard.movePiece(from, checkPos);
            
            if (isKingInCheck(tempBoard, king.getColor())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Validate en passant move
     */
    private static boolean isValidEnPassant(ChessBoard board, Position from, Position to, Pawn pawn, GameState gameState) {
        // Create a copy of the board and simulate the en passant move
        ChessBoard tempBoard = board.copy();
        Position capturedPawnPos = gameState.getLastMoveTo();
        
        // Remove the captured pawn and move the attacking pawn
        tempBoard.getSquare(capturedPawnPos.getRow(), capturedPawnPos.getCol()).clear();
        tempBoard.movePiece(from, to);
        
        // Check if this would expose the king to check
        return !isKingInCheck(tempBoard, pawn.getColor());
    }
    
    public static boolean isKingInCheck(ChessBoard board, PieceColor kingColor) {
        // Get the king of the specified color
        Piece king = board.getKing(kingColor);
        if (king == null) {
            return false;
        }
        Position kingPosition = king.getPosition();
        PieceColor opponentColor = kingColor.opposite();
        // Check all opponent pieces' possible moves
        List<Piece> opponentPieces = board.getAllPieces(opponentColor);
        for (Piece opponentPiece : opponentPieces) {
            List<Position> possibleMoves = opponentPiece.getPossibleMoves(board);
            if (possibleMoves.contains(kingPosition)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isCheckmate(ChessBoard board, PieceColor color, GameState gameState) {
        // King must be in check
        if (!isKingInCheck(board, color)) {
            return false;
        }
        // No legal move exists that removes check
        List<Piece> pieces = board.getAllPieces(color);
        for (Piece piece : pieces) {
            List<Position> legalMoves = getLegalMoves(board, piece.getPosition(), color, gameState);
            if (!legalMoves.isEmpty()) {
                return false; // Found a legal move
            }
        }
        return true; // No legal moves found
    }
    
    public static boolean isStalemate(ChessBoard board, PieceColor color, GameState gameState) {
        // King must NOT be in check
        if (isKingInCheck(board, color)) {
            return false;
        }
        // No legal moves available for any piece
        List<Piece> pieces = board.getAllPieces(color);
        for (Piece piece : pieces) {
            List<Position> legalMoves = getLegalMoves(board, piece.getPosition(), color, gameState);
            if (!legalMoves.isEmpty()) {
                return false; // Found a legal move
            }
        }
        return true; // No legal moves found and not in check
    }
    
    // Overloaded methods for backward compatibility
    public static boolean isCheckmate(ChessBoard board, PieceColor color) {
        return isCheckmate(board, color, null);
    }
    
    public static boolean isStalemate(ChessBoard board, PieceColor color) {
        return isStalemate(board, color, null);
    }
    
    /**
     * Check if a move is a castling move
     */
    public static boolean isCastlingMove(ChessBoard board, Position from, Position to) {
        Piece piece = board.getPiece(from);
        return piece instanceof King && Math.abs(to.getCol() - from.getCol()) == 2;
    }
    
    /**
     * Check if a move is an en passant move
     */
    public static boolean isEnPassantMove(ChessBoard board, Position from, Position to, GameState gameState) {
        Piece piece = board.getPiece(from);
        if (!(piece instanceof Pawn)) {
            return false;
        }
        
        Pawn pawn = (Pawn) piece;
        return pawn.getEnPassantMoves(board, gameState).contains(to);
    }
    
    public static boolean wouldMoveExposeKing(ChessBoard board, Position from, Position to, PieceColor color) {
        ChessBoard boardCopy = board.copy();
        boardCopy.movePiece(from, to);
        // Check if king would be in check after this move
        return isKingInCheck(boardCopy, color);
    }
    
    /**
     * Get all legal moves for a piece at the given position
     * Returns only moves that don't expose the king to check
     */
    public static List<Position> getLegalMoves(ChessBoard board, Position from, PieceColor player, GameState gameState) {
        List<Position> legalMoves = new ArrayList<>();
        
        // Get the piece at the from position
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != player) {
            return legalMoves; // Empty list if no piece or wrong color
        }
        
        // Get all possible moves for this piece
        List<Position> possibleMoves = piece.getPossibleMoves(board);
        
        // For pawns, also include en passant moves
        if (piece instanceof Pawn) {
            Pawn pawn = (Pawn) piece;
            possibleMoves.addAll(pawn.getEnPassantMoves(board, gameState));
        }
        
        // Filter out moves that would expose the king to check
        for (Position to : possibleMoves) {
            if (isValidMove(board, from, to, player, gameState)) {
                legalMoves.add(to);
            }
        }
        
        return legalMoves;
    }
    
    // Overload for backward compatibility
    public static List<Position> getLegalMoves(ChessBoard board, Position from, PieceColor player) {
        return getLegalMoves(board, from, player, null);
    }
}
