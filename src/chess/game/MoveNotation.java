package game;

import board.ChessBoard;
import board.Position;
import pieces.*;
import java.util.List;

/** Utility to format moves in Standard Algebraic Notation (SAN). */
public final class MoveNotation {
    private MoveNotation() {}

    public static String square(Position p) {
        if (p == null) return "?";
        char file = (char)('a' + p.getCol());
        int rank = 8 - p.getRow();
        return "" + file + rank;
    }

    public static String pieceLetter(Piece piece) {
        if (piece instanceof King) return "K";
        if (piece instanceof Queen) return "Q";
        if (piece instanceof Rook) return "R";
        if (piece instanceof Bishop) return "B";
        if (piece instanceof Knight) return "N";
        return ""; // pawns have no letter in SAN
    }

    /**
     * Generate SAN for a move, given the pre-move board/state and flags computed pre/post move.
     * - Disambiguation is computed on the pre-move board (other same pieces that could also reach 'to').
     * - Check/Checkmate markers (+/#) should be computed on the post-move board and passed in.
     */
    public static String san(ChessBoard preBoard,
                             GameState preState,
                             Piece movingPiece,
                             Position from,
                             Position to,
                             boolean isCastle,
                             boolean isEnPassant,
                             boolean isCapture,
                             Class<? extends Piece> promotionType,
                             boolean givesCheck,
                             boolean givesMate) {

        if (isCastle) {
            String castle = (to.getCol() > from.getCol()) ? "O-O" : "O-O-O";
            return castle + (givesMate ? "#" : givesCheck ? "+" : "");
        }

        StringBuilder sb = new StringBuilder();
        String letter = pieceLetter(movingPiece);
        boolean isPawn = letter.isEmpty();
        if (!isPawn) sb.append(letter);

        // Disambiguation for non-pawn moves
        if (!isPawn) {
            String dis = disambiguation(preBoard, preState, movingPiece, from, to);
            sb.append(dis);
        }

        // Captures
        if (isCapture) {
            if (isPawn) {
                // Pawn capture: file letter of from
                char fileFrom = (char)('a' + from.getCol());
                sb.append(fileFrom);
            }
            sb.append('x');
        }

        // Destination
        sb.append(square(to));

        // Promotion
        if (promotionType != null) {
            sb.append("=");
            if (promotionType == Queen.class) sb.append('Q');
            else if (promotionType == Rook.class) sb.append('R');
            else if (promotionType == Bishop.class) sb.append('B');
            else if (promotionType == Knight.class) sb.append('N');
        }

        // Check/Checkmate
        if (givesMate) sb.append('#');
        else if (givesCheck) sb.append('+');

        return sb.toString();
    }

    private static String disambiguation(ChessBoard board, GameState state, Piece movingPiece, Position from, Position to) {
        List<Piece> sameTypePieces = board.getAllPieces(movingPiece.getColor());
        boolean anySameFile = false;
        boolean anySameRank = false;
        for (Piece p : sameTypePieces) {
            if (p == movingPiece) continue;
            if (!p.getClass().equals(movingPiece.getClass())) continue;
            // Can this other piece also legally move to 'to'?
            if (MoveValidator.isValidMove(board, p.getPosition(), to, movingPiece.getColor(), state)) {
                if (p.getPosition().getCol() == from.getCol()) anySameFile = true;
                if (p.getPosition().getRow() == from.getRow()) anySameRank = true;
            }
        }
        if (!anySameFile && !anySameRank) return ""; // no ambiguity
        if (!anySameFile) {
            // Unique file of from distinguishes
            return String.valueOf((char)('a' + from.getCol()));
        }
        if (!anySameRank) {
            // Unique rank of from distinguishes
            return String.valueOf(8 - from.getRow());
        }
        // Need both file and rank
        return String.valueOf((char)('a' + from.getCol())) + (8 - from.getRow());
    }
}
