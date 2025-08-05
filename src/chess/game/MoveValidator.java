package game;

import java.util.ArrayList;
import java.util.List;

import board.ChessBoard;
import board.Position;
import pieces.Piece;
import pieces.PieceColor;

public class MoveValidator {
    
    public static boolean isValidMove(ChessBoard board, Position from, Position to, PieceColor player) {
        // Source square must contain a piece of the current player
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != player) {
            return false;
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
    
    public static boolean isCheckmate(ChessBoard board, PieceColor color) {
        // King must be in check
        if (!isKingInCheck(board, color)) {
            return false;
        }
        // No legal move exists that removes check
        List<Piece> pieces = board.getAllPieces(color);
        for (Piece piece : pieces) {
            List<Position> possibleMoves = piece.getPossibleMoves(board);
            for (Position move : possibleMoves) {
                if (!wouldMoveExposeKing(board, piece.getPosition(), move, color)) {
                    return false; // Found a legal move
                }
            }
        }
        return true; // No legal moves found
    }
    
    public static boolean isStalemate(ChessBoard board, PieceColor color) {
        // King must NOT be in check
        if (isKingInCheck(board, color)) {
            return false;
        }
        // No legal moves available for any piece
        List<Piece> pieces = board.getAllPieces(color);
        for (Piece piece : pieces) {
            List<Position> possibleMoves = piece.getPossibleMoves(board);
            for (Position move : possibleMoves) {
                if (!wouldMoveExposeKing(board, piece.getPosition(), move, color)) {
                    return false; // Found a legal move
                }
            }
        }
        return true; // No legal moves found and not in check
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
    public static List<Position> getLegalMoves(ChessBoard board, Position from, PieceColor player) {
        List<Position> legalMoves = new ArrayList<>();
        
        // Get the piece at the from position
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != player) {
            return legalMoves; // Empty list if no piece or wrong color
        }
        
        // Get all possible moves for this piece
        List<Position> possibleMoves = piece.getPossibleMoves(board);
        
        // Filter out moves that would expose the king to check
        for (Position to : possibleMoves) {
            if (!wouldMoveExposeKing(board, from, to, player)) {
                legalMoves.add(to);
            }
        }
        
        return legalMoves;
    }
}
