/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess;

import java.util.EnumMap;
import java.util.HashSet;

import net.rexbutler.dtchess.movelogic.SimpleChessLogic;
import net.rexbutler.dtchess.movelogic.MoveLogic;

public class Chess {
    public static final int BOARD_SIZE = 8;

    public static final int STARTING_HALF_MOVE_CLOCK = 0;
    public static final int STARTING_FULL_MOVE_COUNT = 1;

    public static final int WHITE_PAWN_Y_STEP = 1;
    public static final int BLACK_PAWN_Y_STEP = -1;

    public static final int BOARD_BOTTOM_ROW_Y = 0;
    public static final int BOARD_TOP_ROW_Y = 7;
    public static final int WHITE_PAWN_START_Y = 1;
    public static final int BLACK_PAWN_START_Y = 6;
    public static final int WHITE_PAWN_ALMOST_PROMOTED_Y = 6;
    public static final int BLACK_PAWN_ALMOST_PROMOTED_Y = 1;
    public static final int WHITE_PAWN_PROMOTED_Y = 7;
    public static final int BLACK_PAWN_PROMOTED_Y = 0;

    public static final int CASTLING_ABS_DELTA_X = 2;
    
    public static final PieceColor STARTING_PLAYER_COLOR = PieceColor.WHITE;
    public static final Piece[][] EMPTY_BOARD = new Piece[BOARD_SIZE][BOARD_SIZE];
    public static final Piece[][] STARTING_CONFIG = {
            { Piece.W_ROOK, Piece.W_PAWN, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.B_PAWN, Piece.B_ROOK },
            { Piece.W_KNIGHT, Piece.W_PAWN, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.B_PAWN, Piece.B_KNIGHT },
            { Piece.W_BISHOP, Piece.W_PAWN, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.B_PAWN, Piece.B_BISHOP },
            { Piece.W_QUEEN, Piece.W_PAWN, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.B_PAWN, Piece.B_QUEEN },
            { Piece.W_KING, Piece.W_PAWN, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.B_PAWN, Piece.B_KING },
            { Piece.W_BISHOP, Piece.W_PAWN, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.B_PAWN, Piece.B_BISHOP },
            { Piece.W_KNIGHT, Piece.W_PAWN, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.B_PAWN, Piece.B_KNIGHT },
            { Piece.W_ROOK, Piece.W_PAWN, Piece.NONE, Piece.NONE, Piece.NONE, Piece.NONE, Piece.B_PAWN, Piece.B_ROOK } };

    public static final MoveLogic CHESS_LOGIC = new SimpleChessLogic();
   
    static {
        for (int j = 0; j < Chess.BOARD_SIZE; j++) {
            for (int i = 0; i < Chess.BOARD_SIZE; i++) {
                EMPTY_BOARD[i][j] = Piece.NONE;
            }
        }
    }
    
    public static HashSet<Square> getSquaresBetween(Square s1, Square s2, boolean includeEnds) {
        int x1 = s1.getX();
        int y1 = s1.getY();
        final int x2 = s2.getX();
        final int y2 = s2.getY();
        final int dx = x2 - x1;
        final int dy = y2 - y1;
        final int adx = Math.abs(dx);
        final int ady = Math.abs(dy);
        int steps;
        int incX;
        int incY;

        int startI;
        int endI;

        final HashSet<Square> squares = new HashSet<>();

        if (adx > 0 && ady > 0 && adx != ady) {
            return squares;
        }

        if (dx > 0) {
            incX = 1;
        } else if (dx == 0) {
            incX = 0;
        } else {
            incX = -1;
        }

        if (dy > 0) {
            incY = 1;
        } else if (dy == 0) {
            incY = 0;
        } else {
            incY = -1;
        }

        steps = Math.max(adx, ady);

        if (includeEnds) {
            startI = 0;
            endI = steps;
        } else {
            startI = 1;
            endI = steps - 1;
            x1 += incX;
            y1 += incY;
        }

        for (int i = startI; i <= endI; i++) {
            squares.add(new Square(x1, y1));
            x1 += incX;
            y1 += incY;
        }
        return squares;
    }

    public static boolean vectorMask(MoveVector moveVector) {
        int adx = Math.abs(moveVector.getDeltaX());
        int ady = Math.abs(moveVector.getDeltaY());
        boolean nonTrivial = (adx != 0 || ady != 0);
        boolean diagonalVector = (adx == ady);
        boolean rookVector = (adx == 0) || (ady == 0);
        boolean knightVector = (adx == 2 && ady == 1) || (adx == 1 && ady == 2);
        
        return (nonTrivial && (knightVector || diagonalVector || rookVector));
    }
}
