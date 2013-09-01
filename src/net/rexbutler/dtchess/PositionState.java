package net.rexbutler.dtchess;

import java.util.EnumMap;

/**
 * 
 * @author rex
 */
public class PositionState {

    // The fields below are in the order as described in the FEN standard
    protected Piece[][] board = new Piece[Chess.BOARD_SIZE][Chess.BOARD_SIZE];
    protected EnumMap<CastleLocation, Boolean> castlingRights = new EnumMap<>(CastleLocation.class);
    protected PieceColor colorToMove = Chess.STARTING_PLAYER_COLOR;
    protected Square enPassantSquare = null;
    protected int fullMoveCount = Chess.STARTING_FULL_MOVE_COUNT;
    protected int halfMoveClock = Chess.STARTING_HALF_MOVE_CLOCK;

    public PositionState(boolean addInitialPieces) {
        if (addInitialPieces) {
            board = Chess.STARTING_CONFIG;

            castlingRights.put(CastleLocation.BLACK_QUEEN_SIDE, true);
            castlingRights.put(CastleLocation.BLACK_KING_SIDE, true);
            castlingRights.put(CastleLocation.WHITE_QUEEN_SIDE, true);
            castlingRights.put(CastleLocation.WHITE_KING_SIDE, true);
        } else {
            board = Chess.EMPTY_BOARD;

            castlingRights.put(CastleLocation.BLACK_QUEEN_SIDE, false);
            castlingRights.put(CastleLocation.BLACK_KING_SIDE, false);
            castlingRights.put(CastleLocation.WHITE_QUEEN_SIDE, false);
            castlingRights.put(CastleLocation.WHITE_KING_SIDE, false);
        }
    }

    public PositionState(PositionState position) {
        board = new Piece[Chess.BOARD_SIZE][Chess.BOARD_SIZE];
        for(int i = 0; i < Chess.BOARD_SIZE; i++) {
            for(int j = 0; j < Chess.BOARD_SIZE; j++) {
                board[i][j] = new Piece(position.board[i][j]);
            }
        }

        colorToMove = position.colorToMove;
        castlingRights = position.castlingRights.clone();
        if (position.enPassantSquare != null) {
            enPassantSquare = new Square(position.enPassantSquare);
        } else {
            enPassantSquare = null;
        }
        halfMoveClock = position.halfMoveClock;
        fullMoveCount = position.fullMoveCount;
    }

    public EnumMap<CastleLocation, Boolean> getAbleToCastle() {
        return castlingRights.clone();
    }

    public boolean getCastleRights(CastleLocation cLocation) {
        return castlingRights.get(cLocation);
    }
    
    public PieceColor getColorToMove() {
        return colorToMove;
    }

    public Square getEnPassantSquare() {
        return enPassantSquare;
    }
    
    public int getFullMoveCount() {
        return fullMoveCount;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public Piece getPieceAt(int i, int j) {
        assert (0 <= i && i < Chess.BOARD_SIZE && 0 <= j && j < Chess.BOARD_SIZE) : "Invalid board coordinates.";
        return board[i][j];
    }

    public Piece getPieceAt(Square square) {
        int i = square.getX();
        int j = square.getY();
        assert (0 <= i && i < Chess.BOARD_SIZE && 0 <= j && j < Chess.BOARD_SIZE) : "Invalid board coordinates.";
        return board[i][j];
    }

    public void setAbleToCastle(EnumMap<CastleLocation, Boolean> ableToCastle) {
        this.castlingRights = ableToCastle.clone();
    }

    public void setBoard(Piece[][] boardConfig) {
        this.board = boardConfig.clone();
    }

    public void setCastleRights(CastleLocation cLocation, boolean canCastle) {
        castlingRights.put(cLocation, canCastle);
    }

    public void setColorToMove(PieceColor playerToMove) {
        this.colorToMove = playerToMove;
    }

    public void setEnPassantSquare(Square enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    public void setFullMoveCount(int fullMoveCount) {
        this.fullMoveCount = fullMoveCount;
    }

    public void setHalfMoveClock(int halfMoveCount) {
        this.halfMoveClock = halfMoveCount;
    }

    public void setPieceAt(int i, int j, Piece piece) {
        assert (0 <= i && i < Chess.BOARD_SIZE && 0 <= j && j < Chess.BOARD_SIZE) : "Invalid board coordinates.";
        board[i][j] = piece;
    }

    public void setPieceAt(Square square, Piece piece) {
        int i = square.getX();
        int j = square.getY();
        assert (0 <= i && i < Chess.BOARD_SIZE && 0 <= j && j < Chess.BOARD_SIZE) : "Invalid board coordinates.";
        board[i][j] = piece;
    }
}
