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

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}
