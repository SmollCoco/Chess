package game;

/**
 * GameState holds turn, status, move count, and last move.
 * - Status reflects PLAYING, CHECK, CHECKMATE, STALEMATE, or DRAW.
 * - lastMoveFrom/To are used for last-move highlights and en passant detection.
 */

import pieces.PieceColor;
import board.Position;

public class GameState {
    public enum Status {
        PLAYING, CHECK, CHECKMATE, STALEMATE, DRAW
    }
    
    private PieceColor currPlayer;
    private Status gameStatus;
    private int moveCount;
    private Position lastMoveFrom;
    private Position lastMoveTo;

    public GameState() {
        this.currPlayer = PieceColor.WHITE;
        this.gameStatus = Status.PLAYING;
        moveCount = 0;
        this.lastMoveFrom = null;
        this.lastMoveTo = null;
    }

    public PieceColor getCurrentPlayer() {
        return this.currPlayer;
    }

    public Status getStatus() {
        return this.gameStatus;
    }

    public int getMoveCount() {
        return this.moveCount;
    }

    public void setStatus(Status status) {
        this.gameStatus = status;
    }

    public void nextTurn() {
        this.currPlayer = currPlayer.opposite();
        ++this.moveCount;
    }

    public void setLastMove(Position from, Position to) {
        this.lastMoveFrom = from;
        this.lastMoveTo = to;
    }

    public Position getLastMoveFrom() {
        return this.lastMoveFrom;
    }

    public Position getLastMoveTo() {
        return this.lastMoveTo;
    }

    public boolean isGameOver() {
        if (
            this.gameStatus == Status.CHECKMATE
            || this.gameStatus == Status.STALEMATE
            || this.gameStatus == Status.DRAW
        ) {
            return true;
        }
        return false;
    }

    public String getStatusMessage() {
        if (this.gameStatus == Status.CHECKMATE) {
            if (this.currPlayer == PieceColor.WHITE) {
                return "Checkmate! White won.";
            }
            return "Checkmate! Black wins.";
        }
        if (this.gameStatus == Status.STALEMATE) {
            return "Draw by stalemate";
        }
        if (this.gameStatus == Status.DRAW) {
            return "Draw.";
        }
        if (this.gameStatus == Status.CHECK) {
            if (this.currPlayer == PieceColor.WHITE) {
                return "Game is running. Black is in check";
            }
            return "Game is running. White is in check";
        }
        return "Game is running.";
    }

    /**
     * Creates a deep copy of the current GameState, including last move positions.
     */
    public GameState copy() {
        GameState gs = new GameState();
        gs.currPlayer = this.currPlayer; // enum copy
        gs.gameStatus = this.gameStatus; // enum copy
        gs.moveCount = this.moveCount;
        if (this.lastMoveFrom != null) {
            gs.lastMoveFrom = new Position(this.lastMoveFrom.getRow(), this.lastMoveFrom.getCol());
        }
        if (this.lastMoveTo != null) {
            gs.lastMoveTo = new Position(this.lastMoveTo.getRow(), this.lastMoveTo.getCol());
        }
        return gs;
    }
}
