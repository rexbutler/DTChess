/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess.notation;

import java.util.EnumMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.rexbutler.dtchess.CastleLocation;
import net.rexbutler.dtchess.Chess;
import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.Piece;
import net.rexbutler.dtchess.PieceColor;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.Square;

/**
 * 
 * @author rex
 */
public class NotationIn {

    public static Position positionFromFEN(String FEN) {
        final String FENParts[] = FEN.split(Notation.FEN_SEGMENT_DELIMITER);

        if (FENParts.length != Notation.FEN_SEGMENTS) {
            return null;
        }

        final Piece[][] boardConfig = boardConfigFromFEN(FENParts[0]);
        final PieceColor pieceColorToMove = playerToMoveFromFEN(FENParts[1]);
        final EnumMap<CastleLocation, Boolean> ableToCastle = castleRightsFromFEN(FENParts[2]);
        final Square enPassantSquare = enPassantSquareFromFEN(FENParts[3]);
        final int halfMoveClock = halfMoveClockFromFEN(FENParts[4]);
        final int fullMoveCount = fullMoveCountFromFEN(FENParts[5]);

        final Position position = new Position(false);
        position.setBoard(boardConfig);
        position.setColorToMove(pieceColorToMove);
        position.setAbleToCastle(ableToCastle);
        position.setEnPassantSquare(enPassantSquare);
        position.setHalfMoveClock(halfMoveClock);
        position.setFullMoveCount(fullMoveCount);

        return position;
    }

    public static Piece[][] boardConfigFromFEN(String FENConfig) {
        final Piece[][] config = new Piece[Chess.BOARD_SIZE][Chess.BOARD_SIZE];
        final Piece[][] tempConfig = new Piece[Chess.BOARD_SIZE][Chess.BOARD_SIZE];
        Piece[] rowConfig;

        final String[] FENRows = FENConfig.split(Notation.ROW_SEPARATOR);
        String expandedRow;

        for (int j_rev = 0; j_rev < Chess.BOARD_SIZE; j_rev++) {
            expandedRow = expandSpacers(FENRows[j_rev]);
            rowConfig = rowFromRowStr(expandedRow);
            tempConfig[Chess.BOARD_SIZE - (1 + j_rev)] = rowConfig;
        }

        for (int j = 0; j < Chess.BOARD_SIZE; j++) {
            for (int i = 0; i < Chess.BOARD_SIZE; i++) {
                config[i][j] = tempConfig[j][i];
            }
        }

        return config;
    }

    private static Piece[] rowFromRowStr(String FENRow) {
        final Piece pieceRow[] = new Piece[Chess.BOARD_SIZE];
        String pieceCode;

        for (int i = 0; i < Chess.BOARD_SIZE; i++) {
            pieceCode = FENRow.substring(i, i + 1);
            pieceRow[i] = Notation.REV_PIECE_CODES.get(pieceCode);
        }
        return pieceRow;
    }

    private static String expandSpacers(String rowWithSpacers) {
        if (rowWithSpacers.equals("")) {
            return "";
        }
        String oldRow = "";
        String row = rowWithSpacers;
        for (int i = 0; i < Chess.BOARD_SIZE && (!row.equals(oldRow)); i++) {
            oldRow = new String(row);
            row = expandFirstSpacer(row);
        }
        return row;
    }

    private static String expandFirstSpacer(String rowWithSpacers) {
        for (int i = 0; i < rowWithSpacers.length(); i++) {
            final String cStr = rowWithSpacers.substring(i, i + 1);
            final char c = cStr.charAt(0);

            if (('1' <= c) && (c <= '9')) {

                final int replaceCount = c - '0';
                String filler = "";
                for (int rc = 0; rc < replaceCount; rc++) {
                    filler += Notation.ROW_FILLER;
                }

                return rowWithSpacers.replaceFirst(cStr, filler);
            }
        }
        return rowWithSpacers;
    }

    public static PieceColor playerToMoveFromFEN(String sideToMove) {
        return Notation.REV_COLOR_CODES.get(sideToMove);
    }

    public static EnumMap<CastleLocation, Boolean> castleRightsFromFEN(String castleRightsStr) {
        final String nc = Notation.NO_CASTLING_MARKER;
        final String wk = Notation.CASTLE_CODES.get(CastleLocation.WHITE_KING_SIDE);
        final String wq = Notation.CASTLE_CODES.get(CastleLocation.WHITE_QUEEN_SIDE);
        final String bk = Notation.CASTLE_CODES.get(CastleLocation.BLACK_KING_SIDE);
        final String bq = Notation.CASTLE_CODES.get(CastleLocation.BLACK_QUEEN_SIDE);
        // Pending changes to constants, patternString should be "-|(K?Q?k?q?)"
        final String patternString = String.format("%s|(%s?%s?%s?%s?)", nc, wk, wq, bk, bq);

        final Pattern pattern = Pattern.compile(patternString);
        final Matcher matcher = pattern.matcher(castleRightsStr);

        if (!matcher.matches() || castleRightsStr.equals("")) {
            return null;
        }

        final EnumMap<CastleLocation, Boolean> castleRights = new EnumMap<>(CastleLocation.class);

        for (final CastleLocation cl : CastleLocation.values()) {
            castleRights.put(cl, false);
        }

        if (castleRightsStr.equals(Notation.NO_CASTLING_MARKER)) {
            return castleRights;
        }

        for (int i = 0; i < castleRightsStr.length(); i++) {
            final String castleCode = castleRightsStr.substring(i, i + 1);
            CastleLocation castleRight;

            if (Notation.REV_CASTLE_CODES.containsKey(castleCode)) {
                castleRight = Notation.REV_CASTLE_CODES.get(castleCode);
                castleRights.put(castleRight, true);
            } else {
                return null;
            }
        }

        return castleRights;
    }

    public static Square enPassantSquareFromFEN(String enPassantSquare) {
        // Error if enPassantSquare is not two characters long
        if (enPassantSquare.length() != 2) {
            return null;
        }

        final int x = Notation.FILE_TO_X.get(enPassantSquare.substring(0, 1));
        final int y = Notation.RANK_TO_Y.get(enPassantSquare.substring(1, 2));

        return new Square(x, y);
    }

    public static Integer halfMoveClockFromFEN(String halfMoveClockStr) {
        return Integer.parseInt(halfMoveClockStr);
    }

    public static Integer fullMoveCountFromFEN(String fullMoveCountStr) {
        return Integer.parseInt(fullMoveCountStr);
    }

    public static Move moveFromNotation(String notation) {
        Move move;
        Square sqr1;
        Square sqr2;
        PieceType promotionType = PieceType.NONE;

        if (notation.length() > 5) {
            throw new IllegalArgumentException("Move notation string is too large.");
        }

        try {
            sqr1 = squareFromNotation(notation.substring(0, 2));
            sqr2 = squareFromNotation(notation.substring(2, 4));

            if (notation.length() == 5) {
                promotionType = Notation.REV_PIECE_TYPE_CODES.get(notation.substring(4, 5));
            }

            move = new Move(sqr1, sqr2, promotionType);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Malformed move string.");
        }
        return move;
    }

    public static Square squareFromNotation(String notation) {
        int x;
        int y;

        if (notation.length() > 2) {
            throw new IllegalArgumentException("Square notation string is too large.");
        }

        try {
            x = Notation.FILE_TO_X.get(notation.substring(0, 1));
            y = Notation.RANK_TO_Y.get(notation.substring(1, 2));
        } catch (final Exception e) {
            throw new IllegalArgumentException("Malformed square string.");
        }
        return new Square(x, y);
    }

}