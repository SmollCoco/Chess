package board;

import java.util.List;
import java.util.ArrayList;

import pieces.*;

public class ChessBoard {
    private Square[][] board;
    private Piece white_king;
    private Piece black_king;

    public ChessBoard() {
        this.board = new Square[8][8];
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                Position pos = new Position(row, col);
                this.board[row][col] = new Square(null, pos);
            }
        }
        // Setting up the pieces
        setupInitialPieces();
    }

    private void setupInitialPieces() {
        // Black pieces (rows 0-1)
        // Back rank - row 0
        placePiece(new Rook(PieceColor.BLACK, new Position(0, 0)), 0, 0);
        placePiece(new Knight(PieceColor.BLACK, new Position(0, 1)), 0, 1);
        placePiece(new Bishop(PieceColor.BLACK, new Position(0, 2)), 0, 2);
        placePiece(new Queen(PieceColor.BLACK, new Position(0, 3)), 0, 3);
        this.black_king = new King(PieceColor.BLACK, new Position(0, 4));
        placePiece(this.black_king, 0, 4);
        placePiece(new Bishop(PieceColor.BLACK, new Position(0, 5)), 0, 5);
        placePiece(new Knight(PieceColor.BLACK, new Position(0, 6)), 0, 6);
        placePiece(new Rook(PieceColor.BLACK, new Position(0, 7)), 0, 7);
        
        // Black pawns - row 1
        for (int col = 0; col < 8; col++) {
            placePiece(new Pawn(PieceColor.BLACK, new Position(1, col)), 1, col);
        }
        
        // White pawns - row 6
        for (int col = 0; col < 8; col++) {
            placePiece(new Pawn(PieceColor.WHITE, new Position(6, col)), 6, col);
        }
        
        // White pieces (rows 6-7)
        // Back rank - row 7
        placePiece(new Rook(PieceColor.WHITE, new Position(7, 0)), 7, 0);
        placePiece(new Knight(PieceColor.WHITE, new Position(7, 1)), 7, 1);
        placePiece(new Bishop(PieceColor.WHITE, new Position(7, 2)), 7, 2);
        placePiece(new Queen(PieceColor.WHITE, new Position(7, 3)), 7, 3);
        this.white_king = new King(PieceColor.WHITE, new Position(7, 4));
        placePiece(this.white_king, 7, 4);
        placePiece(new Bishop(PieceColor.WHITE, new Position(7, 5)), 7, 5);
        placePiece(new Knight(PieceColor.WHITE, new Position(7, 6)), 7, 6);
        placePiece(new Rook(PieceColor.WHITE, new Position(7, 7)), 7, 7);
    }

    public Piece getPiece(Position pos) {
        return this.board[pos.getRow()][pos.getCol()].getPiece();
    }

    public Square getSquare(int row, int col) {
        if (!isValidPosition(row, col)) {
            return null;
        }
        return this.board[row][col];
    }

    public void placePiece(Piece piece, int row, int col) {
        if (!isValidPosition(row, col)) {
            return;
        }
        this.board[row][col].setPiece(piece);
    }

    public void movePiece(Position a, Position b) {
        Piece piece = this.board[a.getRow()][a.getCol()].getPiece();
        if (piece != null) {
            this.board[a.getRow()][a.getCol()].clear();
            piece.setPosition(b); // Update piece position and set hasMoved
            this.board[b.getRow()][b.getCol()].setPiece(piece);
            // Update king references if a king was moved
            if (piece instanceof King) {
                if (piece.getColor() == PieceColor.WHITE) {
                    this.white_king = piece;
                } else {
                    this.black_king = piece;
                }
            }
        }
    }

    /**
     * Performs castling move - moves both king and rook
     */
    public void performCastling(Position kingFrom, Position kingTo) {
        Piece king = getPiece(kingFrom);
        if (king == null || !(king instanceof King)) {
            return;
        }

        // Determine if it's kingside or queenside castling
        boolean isKingside = kingTo.getCol() > kingFrom.getCol();
        int rookFromCol = isKingside ? 7 : 0;
        int rookToCol = isKingside ? kingTo.getCol() - 1 : kingTo.getCol() + 1;
        
        Position rookFrom = new Position(kingFrom.getRow(), rookFromCol);
        Position rookTo = new Position(kingFrom.getRow(), rookToCol);
        
        // Move king
        movePiece(kingFrom, kingTo);
        // Move rook
        movePiece(rookFrom, rookTo);
    }

    /**
     * Performs en passant capture - removes the captured pawn
     */
    public void performEnPassant(Position pawnFrom, Position pawnTo, Position capturedPawnPos) {
        // Move the attacking pawn
        movePiece(pawnFrom, pawnTo);
        // Remove the captured pawn
        this.board[capturedPawnPos.getRow()][capturedPawnPos.getCol()].clear();
    }

    public List<Piece> getAllPieces(PieceColor color) {
        List<Piece> res = new ArrayList<Piece>();
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                Piece piece = this.board[row][col].getPiece();
                if (piece != null && piece.getColor() == color) {
                    res.add(piece);
                }
            }
        }
        return res;
    }

    public Piece getKing(PieceColor color) {
        return color == PieceColor.WHITE ? this.white_king : this.black_king;
    }

    public ChessBoard copy() {
        ChessBoard res = new ChessBoard();
        // Clear the initial setup since we're copying
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                res.board[row][col].clear();
            }
        }
        // Copy all pieces
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                Piece piece = this.board[row][col].getPiece();
                if (piece != null) {
                    Piece copiedPiece = piece.copy();
                    res.placePiece(copiedPiece, row, col);
                    // Update king references in copied board
                    if (copiedPiece instanceof King) {
                        if (copiedPiece.getColor() == PieceColor.WHITE) {
                            res.white_king = copiedPiece;
                        } else {
                            res.black_king = copiedPiece;
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * Promotes a pawn to the specified piece type
     */
    public void promotePawn(Position pawnPos, Class<? extends Piece> pieceType) {
        Piece pawn = getPiece(pawnPos);
        if (pawn == null || !(pawn instanceof Pawn)) {
            return;
        }

        try {
            // Create new piece of the specified type
            Piece newPiece = null;
            PieceColor color = pawn.getColor();
            
            if (pieceType == Queen.class) {
                newPiece = new Queen(color, pawnPos);
            } else if (pieceType == Rook.class) {
                newPiece = new Rook(color, pawnPos);
            } else if (pieceType == Bishop.class) {
                newPiece = new Bishop(color, pawnPos);
            } else if (pieceType == Knight.class) {
                newPiece = new Knight(color, pawnPos);
            } else {
                // Default to Queen if invalid piece type
                newPiece = new Queen(color, pawnPos);
            }
            
            // Replace the pawn with the new piece
            newPiece.setHasMoved(true); // Mark as moved since it's a promotion
            this.board[pawnPos.getRow()][pawnPos.getCol()].setPiece(newPiece);
            
        } catch (Exception e) {
            // If anything goes wrong, default to Queen
            Queen queen = new Queen(pawn.getColor(), pawnPos);
            queen.setHasMoved(true);
            this.board[pawnPos.getRow()][pawnPos.getCol()].setPiece(queen);
        }
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}
