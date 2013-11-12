/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess.notation;

import com.google.common.base.Optional;

import net.rexbutler.dtchess.CastleLocation;
import net.rexbutler.dtchess.Chess;
import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.Piece;
import net.rexbutler.dtchess.PieceColor;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.PositionState;
import net.rexbutler.dtchess.Square;

/**
 * A class relevant to converting internal objects into standard chess notations.
 * 
 * @author Rex Butler
 */
public class NotationOut {

    public static String notationOf(PositionState position, Move move) {
        final Piece piece = position.getPieceAt(move.getStartSquare());
        String notation = "";
        notation += Notation.PIECE_TYPE_CODES.get(piece.getType());
        notation += notationOf(Optional.of(move.getStartSquare()));
        notation += notationOf(Optional.of(move.getEndSquare()));

        return notation;
    }

    public static String notationOf(Optional<Square> optSquare) {
        String notation;
        if (optSquare.isPresent()) {
            Square square = optSquare.get();
            notation = "";
            notation += Notation.X_TO_FILE.get(square.getX());
            notation += Notation.Y_TO_RANK.get(square.getY());
        } else {
            notation =  Notation.EN_PASSANT_FILLER;
        }
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
        String fenRow = "";
        int empty_count = 0;

        for (int i = 0; i < Chess.BOARD_SIZE; i++) {
            if (position.getPieceAt(i, j).equals(Piece.NONE)) {
                empty_count += 1;
                if ( !(i + 1 < Chess.BOARD_SIZE) || !position.getPieceAt(i + 1, j).equals(Piece.NONE) ) {
                    fenRow += "" + empty_count;
                    empty_count = 0;
                }
            } else {
                String n = Notation.PIECE_CODES.get(position.getPieceAt(i, j)); 
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

        for (int j = Chess.BOARD_SIZE - 1; j >= 0; j--) {
            for (int i = 0; i < Chess.BOARD_SIZE; i++) {
                final Piece piece = position.getPieceAt(i,j);
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
