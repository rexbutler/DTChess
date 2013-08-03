/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess;

/**
 * 
 * @author rex
 */
public class NotationOut {

    public static String notationOf(PositionState position, Move move) {
        final int x1 = move.getStartSquare().getX();
        final int y1 = move.getStartSquare().getY();
        final Piece piece = position.getBoard()[x1][y1];
        String notation = "";

        notation += Notation.PIECE_TYPE_CODES.get(piece.getType());
        notation += notationOf(move.getStartSquare());
        notation += notationOf(move.getEndSquare());

        return notation;
    }

    public static String notationOf(Square square) {
        if (square == null) {
            return Notation.EN_PASSANT_FILLER;
        }

        String notation = "";
        notation += Notation.X_TO_FILE.get(square.getX());
        notation += Notation.Y_TO_RANK.get(square.getY());

        return notation;
    }

    public static String positionFEN(PositionState position) {
        String FEN = "";

        for (int j = Chess.BOARD_SIZE - 1; j >= 0; j--) {
            FEN += configRowFEN(position, j);
            if (j > 0) {
                FEN += Notation.ROW_SEPARATOR;
            }
        }

        FEN += " ";
        FEN += sideToMoveFEN(position);
        FEN += " ";
        FEN += castleRightsFEN(position); // Castling rights
        FEN += " ";
        FEN += enPassantSquareFEN(position); // En passant square
        FEN += " ";
        FEN += halfMoveClockFEN(position);
        FEN += " ";
        FEN += fullMoveCountFEN(position);

        return FEN;
    }

    public static String configRowFEN(PositionState position, int j) {
        final Piece[][] config = position.getBoard();
        String fenRow = "";
        int empty_count = 0;

        for (int i = 0; i < Chess.BOARD_SIZE; i++) {
            if (config[i][j] == Piece.NONE) {
                empty_count += 1;
                if ( !(i + 1 < Chess.BOARD_SIZE) || (config[i + 1][j] != Piece.NONE) ) {
                    fenRow += "" + empty_count;
                    empty_count = 0;
                }
            } else {
                String n = Notation.PIECE_CODES.get(config[i][j]); 
                fenRow += n;
            }
        }
        return fenRow;
    }

    public static String sideToMoveFEN(PositionState position) {
        return (position.getColorToMove() == PieceColor.WHITE) ? "w" : "b";
    }

    public static String castleRightsFEN(PositionState position) {
        final String wKSide = (position.getCastleRights(CastleLocation.WHITE_KING_SIDE) == true) ? Notation.CASTLE_CODES
                .get(CastleLocation.WHITE_KING_SIDE) : "";
        final String wQSide = (position.getCastleRights(CastleLocation.WHITE_QUEEN_SIDE) == true) ? Notation.CASTLE_CODES
                .get(CastleLocation.WHITE_QUEEN_SIDE) : "";
        final String bKSide = (position.getCastleRights(CastleLocation.BLACK_KING_SIDE) == true) ? Notation.CASTLE_CODES
                .get(CastleLocation.BLACK_KING_SIDE) : "";
        final String bQSide = (position.getCastleRights(CastleLocation.BLACK_QUEEN_SIDE) == true) ? Notation.CASTLE_CODES
                .get(CastleLocation.BLACK_QUEEN_SIDE) : "";
        String rightsFEN = wKSide + wQSide + bKSide + bQSide;

        if ("".equals(rightsFEN)) {
            rightsFEN = Notation.NO_CASTLING_MARKER;
        }

        return rightsFEN;
    }

    public static String enPassantSquareFEN(PositionState position) {
        return notationOf(position.getEnPassantSquare());
    }

    public static String halfMoveClockFEN(PositionState position) {
        return "" + position.getHalfMoveClock();
    }

    public static String fullMoveCountFEN(PositionState position) {
        return "" + position.getFullMoveCount();
    }

    public static String gridString(PositionState position) {
        String grid_str = "";

        final Piece[][] board = position.getBoard();

        for (int j = Chess.BOARD_SIZE - 1; j >= 0; j--) {
            for (int i = 0; i < Chess.BOARD_SIZE; i++) {
                final Piece piece = board[i][j];
                grid_str += Notation.PIECE_CODES.get(piece);
            }
            grid_str += "\n";
        }
        return grid_str;
    }

    public static String longDescription(PositionState position) {
        String fullLongDesc = "";

        fullLongDesc += gridString(position);

        if (position.getColorToMove() == PieceColor.WHITE) {
            fullLongDesc += "Side to move: white\n";
        } else if (position.getColorToMove() == PieceColor.BLACK) {
            fullLongDesc += "Side to move: black\n";
        } else {
            fullLongDesc += "Side to move: not set\n";
        }

        fullLongDesc += "Castling rights: ";
        fullLongDesc += castleRightsFEN(position);
        fullLongDesc += "\n";

        fullLongDesc += "En passant square: ";
        fullLongDesc += enPassantSquareFEN(position);
        fullLongDesc += "\n";

        fullLongDesc += "Half move clock: ";
        fullLongDesc += halfMoveClockFEN(position);
        fullLongDesc += "\n";

        fullLongDesc += "Full move count: ";
        fullLongDesc += fullMoveCountFEN(position);
        fullLongDesc += "\n";

        return fullLongDesc;
    }

    public static String notationOfMove(Move move) {
        String notation = "";
        final PieceType promotionType = move.getPromotionPieceType();

        notation += notationOfSquare(move.getStartSquare());
        notation += notationOfSquare(move.getEndSquare());
        if (promotionType != PieceType.NONE) {
            notation += Notation.PIECE_TYPE_CODES.get(promotionType);
        }

        return notation;
    }

    public static String notationOfSquare(Square square) {
        String notation = "";

        notation += Notation.X_TO_FILE.get(square.getX());
        notation += Notation.Y_TO_RANK.get(square.getY());

        return notation;
    }
}
