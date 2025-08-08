package game;

import board.ChessBoard;
import board.Position;
import pieces.PieceColor;
import pieces.Piece;
import pieces.Pawn;
import pieces.Queen;
// imports removed if unused
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ChessGame {
    private ChessBoard board;
    private GameState state;
    private final List<String> moveHistory = new ArrayList<>();
    // Simple undo stack capturing board + state + SAN moved
    private final Deque<ChessBoard> boardHistory = new ArrayDeque<>();
    private final Deque<GameState> stateHistory = new ArrayDeque<>();
    private final Deque<String> sanHistory = new ArrayDeque<>();

    public ChessGame() {
        resetGame();
    }

    public boolean makeMove(Position a, Position b) {
        // Delegate to overload with default (null) promotion type which becomes Queen
        return makeMove(a, b, null);
    }

    /**
     * Makes a move and, if promotion occurs, promotes to the specified piece type.
     * If promotionType is null, defaults to Queen.
     */
    public boolean makeMove(Position a, Position b, Class<? extends Piece> promotionType) {
        if (!MoveValidator.isValidMove(board, a, b, this.state.getCurrentPlayer(), this.state)) {
            return false;
        }

    // Snapshot for undo
    boardHistory.push(this.board.copy());
    stateHistory.push(this.state.copy());

        // Check if this is a pawn move that will result in promotion
        Piece movingPiece = board.getPiece(a);
        boolean willPromote = false;
        if (movingPiece instanceof Pawn) {
            int promotionRow = movingPiece.getColor() == PieceColor.WHITE ? 0 : 7;
            if (b.getRow() == promotionRow) {
                willPromote = true;
            }
        }

    // Determine special flags and capture before mutating the board
        boolean isCastle = MoveValidator.isCastlingMove(board, a, b);
        boolean isEnPassant = MoveValidator.isEnPassantMove(board, a, b, this.state);
        boolean isCapture = false;
        if (!isCastle && !isEnPassant) {
            Piece destBefore = board.getPiece(b);
            isCapture = (destBefore != null && destBefore.getColor() != this.state.getCurrentPlayer());
        } else if (isEnPassant) {
            isCapture = true;
        }

        // Handle special moves
        if (isCastle) {
            board.performCastling(a, b);
        } else if (isEnPassant) {
            Position capturedPawnPos = this.state.getLastMoveTo();
            board.performEnPassant(a, b, capturedPawnPos);
        } else {
            // Regular move
            this.board.movePiece(a, b);
        }

        // Handle pawn promotion using provided type or default to Queen
        if (willPromote) {
            Class<? extends Piece> type = (promotionType != null) ? promotionType : Queen.class;
            board.promotePawn(b, type);
        }

    // Track the move for en passant detection
        this.state.setLastMove(a, b);
    // Compute check/mate on the post-move board for SAN suffix
    boolean givesCheck = MoveValidator.isKingInCheck(board, this.state.getCurrentPlayer());
    boolean givesMate = MoveValidator.isCheckmate(board, this.state.getCurrentPlayer(), this.state);
    Class<? extends Piece> promoTypeUsed = (willPromote ? (promotionType != null ? promotionType : Queen.class) : null);
    String san = MoveNotation.san(this.board, this.state, movingPiece, a, b, isCastle, isEnPassant, isCapture, promoTypeUsed, givesCheck, givesMate);
    moveHistory.add(san);
    sanHistory.push(san);
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
    this.moveHistory.clear();
    this.boardHistory.clear();
    this.stateHistory.clear();
    this.sanHistory.clear();
        updateGameStatus(); // Set initial game status
    }

    public ChessBoard getBoard() {
        return this.board;
    }

    public GameState getGameState() {
        return this.state;
    }

    public List<String> getMoveHistory() {
        return this.moveHistory;
    }

    // ---------- Undo Support ----------
    public boolean canUndo() {
        return !boardHistory.isEmpty() && !stateHistory.isEmpty();
    }

    public boolean undoLastMove() {
        if (!canUndo()) return false;
        // Restore previous board and state
        this.board = boardHistory.pop();
        this.state = stateHistory.pop();
        // Remove last SAN entry (both from stack and list)
        if (!sanHistory.isEmpty()) sanHistory.pop();
        if (!moveHistory.isEmpty()) moveHistory.remove(moveHistory.size() - 1);
        // Recompute status just in case
        updateGameStatus();
        return true;
    }

    /**
     * Returns move pairs with numbering like: "1. e4   e5".
     * If a black move is missing (odd count), the black side is blank.
     */
    public List<String> getPairedMoveHistory() {
        List<String> paired = new ArrayList<>();
        for (int i = 0; i < moveHistory.size(); i += 2) {
            int moveNumber = (i / 2) + 1;
            String white = moveHistory.get(i);
            String black = (i + 1 < moveHistory.size()) ? moveHistory.get(i + 1) : "";
            // Align with a bit of spacing; UI uses monospaced font already
            String line = moveNumber + ". " + white + (black.isEmpty() ? "" : ("    " + black));
            paired.add(line);
        }
        return paired;
    }
}
