package game;

import pieces.PieceColor;

public class GameState {
    public enum Status {
        PLAYING, CHECK, CHECKMATE, STALEMATE, DRAW
    }
    
    private PieceColor currPlayer;
    private Status gameStatus;
    private int moveCount;

    public GameState() {
        this.currPlayer = PieceColor.WHITE;
        this.gameStatus = Status.PLAYING;
        moveCount = 0;
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
}
