package game;

import board.ChessBoard;
import board.Position;
import pieces.PieceColor;

public class ChessGame {
    private ChessBoard board;
    private GameState state;

    public ChessGame() {
        resetGame();
    }

    public boolean makeMove(Position a, Position b) {
        if (!MoveValidator.isValidMove(board, a, b, this.state.getCurrentPlayer())) {
            return false;
        }
        this.board.movePiece(a, b);
        this.state.nextTurn();
        updateGameStatus();
        return true;
    }
    
    private void updateGameStatus() {
        PieceColor currentPlayer = state.getCurrentPlayer();
        // Check for checkmate first
        if (MoveValidator.isCheckmate(board, currentPlayer)) {
            state.setStatus(GameState.Status.CHECKMATE);
        }
        // Check for stalemate
        else if (MoveValidator.isStalemate(board, currentPlayer)) {
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
