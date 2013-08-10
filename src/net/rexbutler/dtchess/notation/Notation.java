/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess.notation;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import net.rexbutler.dtchess.CastleLocation;
import net.rexbutler.dtchess.Piece;
import net.rexbutler.dtchess.PieceColor;
import net.rexbutler.dtchess.PieceType;

/**
 * 
 * @author rex
 */
public class Notation {

    // A FEN String has 6 segments separated by spaces
    static final int FEN_SEGMENTS = 6;
    static final String FEN_SEGMENT_DELIMITER = " ";
    static final String ROW_SEPARATOR = "/";
    static final String ROW_FILLER = "-";
    static final String NO_CASTLING_MARKER = "-";
    static final String EN_PASSANT_FILLER = "-";
    static final String ALL_PIECE_CODES;
    static final Map<Piece, String> PIECE_CODES = new HashMap<Piece, String>();

    static final Map<PieceType, String> PIECE_TYPE_CODES = new EnumMap<>(PieceType.class);
    static final Map<String, PieceType> REV_PIECE_TYPE_CODES = new HashMap<>(); // TODO

    static final Map<String, Piece> REV_PIECE_CODES = new HashMap<>();
    static final String ALL_COLOR_CODES;
    static final Map<PieceColor, String> COLOR_CODES = new EnumMap<>(PieceColor.class);
    static final Map<String, PieceColor> REV_COLOR_CODES = new HashMap<>();
    static final String ALL_CASTLE_CODES;
    static final Map<CastleLocation, String> CASTLE_CODES = new EnumMap<>(CastleLocation.class);
    static final Map<String, CastleLocation> REV_CASTLE_CODES = new HashMap<>();
    static final Map<String, Integer> FILE_TO_X = new HashMap<>();
    static final Map<Integer, String> X_TO_FILE = new HashMap<>();
    static final Map<String, Integer> RANK_TO_Y = new HashMap<>();
    static final Map<Integer, String> Y_TO_RANK = new HashMap<>();

    static {
        ALL_PIECE_CODES = "-PNBRQKpnbrqk";
        PIECE_CODES.put(Piece.NONE, "-");
        PIECE_CODES.put(Piece.W_PAWN, "P");
        PIECE_CODES.put(Piece.W_KNIGHT, "N");
        PIECE_CODES.put(Piece.W_BISHOP, "B");
        PIECE_CODES.put(Piece.W_ROOK, "R");
        PIECE_CODES.put(Piece.W_QUEEN, "Q");
        PIECE_CODES.put(Piece.W_KING, "K");
        PIECE_CODES.put(Piece.B_PAWN, "p");
        PIECE_CODES.put(Piece.B_KNIGHT, "n");
        PIECE_CODES.put(Piece.B_BISHOP, "b");
        PIECE_CODES.put(Piece.B_ROOK, "r");
        PIECE_CODES.put(Piece.B_QUEEN, "q");
        PIECE_CODES.put(Piece.B_KING, "k");
        REV_PIECE_CODES.put("-", Piece.NONE);
        REV_PIECE_CODES.put("P", Piece.W_PAWN);
        REV_PIECE_CODES.put("N", Piece.W_KNIGHT);
        REV_PIECE_CODES.put("B", Piece.W_BISHOP);
        REV_PIECE_CODES.put("R", Piece.W_ROOK);
        REV_PIECE_CODES.put("Q", Piece.W_QUEEN);
        REV_PIECE_CODES.put("K", Piece.W_KING);
        REV_PIECE_CODES.put("p", Piece.B_PAWN);
        REV_PIECE_CODES.put("n", Piece.B_KNIGHT);
        REV_PIECE_CODES.put("b", Piece.B_BISHOP);
        REV_PIECE_CODES.put("r", Piece.B_ROOK);
        REV_PIECE_CODES.put("q", Piece.B_QUEEN);
        REV_PIECE_CODES.put("k", Piece.B_KING);

        PIECE_TYPE_CODES.put(PieceType.NONE, "X");
        PIECE_TYPE_CODES.put(PieceType.PAWN, "P");
        PIECE_TYPE_CODES.put(PieceType.KNIGHT, "N");
        PIECE_TYPE_CODES.put(PieceType.BISHOP, "B");
        PIECE_TYPE_CODES.put(PieceType.ROOK, "R");
        PIECE_TYPE_CODES.put(PieceType.QUEEN, "Q");
        PIECE_TYPE_CODES.put(PieceType.KING, "K");
        REV_PIECE_TYPE_CODES.put("X", PieceType.NONE);
        REV_PIECE_TYPE_CODES.put("P", PieceType.PAWN);
        REV_PIECE_TYPE_CODES.put("N", PieceType.KNIGHT);
        REV_PIECE_TYPE_CODES.put("B", PieceType.BISHOP);
        REV_PIECE_TYPE_CODES.put("R", PieceType.ROOK);
        REV_PIECE_TYPE_CODES.put("Q", PieceType.QUEEN);
        REV_PIECE_TYPE_CODES.put("K", PieceType.KING);

        ALL_COLOR_CODES = "wb-";
        COLOR_CODES.put(PieceColor.WHITE, "w");
        COLOR_CODES.put(PieceColor.BLACK, "b");
        COLOR_CODES.put(PieceColor.NONE, "-");
        REV_COLOR_CODES.put("w", PieceColor.WHITE);
        REV_COLOR_CODES.put("b", PieceColor.BLACK);
        REV_COLOR_CODES.put("-", PieceColor.NONE);

        // See also variable 'NO_CASTLING_MARKER'
        ALL_CASTLE_CODES = "KQkq";
        CASTLE_CODES.put(CastleLocation.WHITE_KING_SIDE, "K");
        CASTLE_CODES.put(CastleLocation.WHITE_QUEEN_SIDE, "Q");
        CASTLE_CODES.put(CastleLocation.BLACK_KING_SIDE, "k");
        CASTLE_CODES.put(CastleLocation.BLACK_QUEEN_SIDE, "q");
        REV_CASTLE_CODES.put("K", CastleLocation.WHITE_KING_SIDE);
        REV_CASTLE_CODES.put("Q", CastleLocation.WHITE_QUEEN_SIDE);
        REV_CASTLE_CODES.put("k", CastleLocation.BLACK_KING_SIDE);
        REV_CASTLE_CODES.put("q", CastleLocation.BLACK_QUEEN_SIDE);

        FILE_TO_X.put("a", 0);
        FILE_TO_X.put("b", 1);
        FILE_TO_X.put("c", 2);
        FILE_TO_X.put("d", 3);
        FILE_TO_X.put("e", 4);
        FILE_TO_X.put("f", 5);
        FILE_TO_X.put("g", 6);
        FILE_TO_X.put("h", 7);

        X_TO_FILE.put(0, "a");
        X_TO_FILE.put(1, "b");
        X_TO_FILE.put(2, "c");
        X_TO_FILE.put(3, "d");
        X_TO_FILE.put(4, "e");
        X_TO_FILE.put(5, "f");
        X_TO_FILE.put(6, "g");
        X_TO_FILE.put(7, "h");

        RANK_TO_Y.put("1", 0);
        RANK_TO_Y.put("2", 1);
        RANK_TO_Y.put("3", 2);
        RANK_TO_Y.put("4", 3);
        RANK_TO_Y.put("5", 4);
        RANK_TO_Y.put("6", 5);
        RANK_TO_Y.put("7", 6);
        RANK_TO_Y.put("8", 7);

        Y_TO_RANK.put(0, "1");
        Y_TO_RANK.put(1, "2");
        Y_TO_RANK.put(2, "3");
        Y_TO_RANK.put(3, "4");
        Y_TO_RANK.put(4, "5");
        Y_TO_RANK.put(5, "6");
        Y_TO_RANK.put(6, "7");
        Y_TO_RANK.put(7, "8");
    }
}
