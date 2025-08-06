package game;

import board.ChessBoard;
import board.Position;
import pieces.PieceColor;
import pieces.Piece;
import pieces.Pawn;
import pieces.Queen;

public class ChessGame {
    private ChessBoard board;
    private GameState state;

    public ChessGame() {
        resetGame();
    }

    public boolean makeMove(Position a, Position b) {
        if (!MoveValidator.isValidMove(board, a, b, this.state.getCurrentPlayer(), this.state)) {
            return false;
        }
        
        // Check if this is a pawn move that will result in promotion
        Piece movingPiece = board.getPiece(a);
        boolean willPromote = false;
        if (movingPiece instanceof Pawn) {
            int promotionRow = movingPiece.getColor() == PieceColor.WHITE ? 0 : 7;
            if (b.getRow() == promotionRow) {
                willPromote = true;
            }
        }
        
        // Handle special moves
        if (MoveValidator.isCastlingMove(board, a, b)) {
            board.performCastling(a, b);
        } else if (MoveValidator.isEnPassantMove(board, a, b, this.state)) {
            Position capturedPawnPos = this.state.getLastMoveTo();
            board.performEnPassant(a, b, capturedPawnPos);
        } else {
            // Regular move
            this.board.movePiece(a, b);
        }
        
        // Handle pawn promotion (default to Queen for now)
        if (willPromote) {
            board.promotePawn(b, Queen.class);
        }
        
        // Track the move for en passant detection
        this.state.setLastMove(a, b);
        this.state.nextTurn();
        updateGameStatus();
        return true;
    }
    
    private void updateGameStatus() {
        PieceColor currentPlayer = state.getCurrentPlayer();
        // Check for checkmate first
        if (MoveValidator.isCheckmate(board, currentPlayer, state)) {
            state.setStatus(GameState.Status.CHECKMATE);
        }
        // Check for stalemate
        else if (MoveValidator.isStalemate(board, currentPlayer, state)) {
            state.setStatus(GameState.Status.STALEMATE);
        }
        // Check for check
        else if (MoveValidator.isKingInCheck(board, currentPlayer)) {
            state.setStatus(GameState.Status.CHECK);
        }
        // Normal playing state
        else {
            state.setStatus(GameState.Status.PLAYING);
        }
    }

    public void resetGame() {
        this.board = new ChessBoard();
        this.state = new GameState();
        updateGameStatus(); // Set initial game status
    }

    public ChessBoard getBoard() {
        return this.board;
    }

    public GameState getGameState() {
        return this.state;
    }
}
