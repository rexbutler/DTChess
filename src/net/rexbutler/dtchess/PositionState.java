package net.rexbutler.dtchess;

import java.util.EnumMap;

import com.google.common.base.Optional;

/**
 * Represents the complete state of a chess position --- except for three-fold draw by repetition
 * info which requires a complete game log, see the Game class.
 * 
 * @author Rex Butler
 */
public class PositionState {

    // The fields below are in the order as described in the FEN standard.
    // See: http://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation

    // The array representing the position of the pieces on the chess board.
    protected Piece[][] board = new Piece[Chess.BOARD_SIZE][Chess.BOARD_SIZE];
    // The array representing which castling moves are still available.
    protected EnumMap<CastleLocation, Boolean> castlingRights = new EnumMap<>(CastleLocation.class);
    // The color of the player whose turn it is to move.
    protected PieceColor colorToMove = Chess.STARTING_PLAYER_COLOR;
    // The en passant square, if applicable. Set to null if not applicable.
    protected Optional<Square> enPassantSquare = null;
    // The number of half moves since the last pawn move.
    protected int halfMoveClock = Chess.STARTING_HALF_MOVE_CLOCK;
    // The number of full moves that have taken place in the game.
    protected int fullMoveCount = Chess.STARTING_FULL_MOVE_COUNT;

    /**
     * Constructor. Creates a new Position state with either an empty board or a board set to the
     * initial starting position.
     * 
     * @param addInitialPieces True if the board is to be populated with the pieces in starting position.
     */
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
    
    /**
     * Copy Constructor.
     * @param positionState The PositionState to copy.
     */
    public PositionState(PositionState positionState) {
        board = new Piece[Chess.BOARD_SIZE][Chess.BOARD_SIZE];
        for (int i = 0; i < Chess.BOARD_SIZE; i++) {
            for (int j = 0; j < Chess.BOARD_SIZE; j++) {
                board[i][j] = new Piece(positionState.board[i][j]);
            }
        }

        colorToMove = positionState.colorToMove;
        castlingRights = positionState.castlingRights.clone();
        enPassantSquare = positionState.enPassantSquare;
        halfMoveClock = positionState.halfMoveClock;
        fullMoveCount = positionState.fullMoveCount;
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

    public Optional<Square> getEnPassantSquare() {
        return enPassantSquare;
    }

    public int getFullMoveCount() {
        return fullMoveCount;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    /**
     * Return the piece at the given coordinates.
     */
    public Piece getPieceAt(int i, int j) {
        assert (0 <= i && i < Chess.BOARD_SIZE && 0 <= j && j < Chess.BOARD_SIZE) : "Invalid board coordinates.";
        return board[i][j];
    }

    /**
     * Return the piece at the given square.
     */
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

    public void setEnPassantSquare(Optional<Square> enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    public void setFullMoveCount(int fullMoveCount) {
        this.fullMoveCount = fullMoveCount;
    }

    public void setHalfMoveClock(int halfMoveCount) {
        this.halfMoveClock = halfMoveCount;
    }

    /**
     * Change the piece on the board at the given coordinates.
     */
    public void setPieceAt(int i, int j, Piece piece) {
        assert (0 <= i && i < Chess.BOARD_SIZE && 0 <= j && j < Chess.BOARD_SIZE) : "Invalid board coordinates.";
        board[i][j] = piece;
    }

    /**
     * Change the piece at the given square.
     */
    public void setPieceAt(Square square, Piece piece) {
        int i = square.getX();
        int j = square.getY();
        assert (0 <= i && i < Chess.BOARD_SIZE && 0 <= j && j < Chess.BOARD_SIZE) : "Invalid board coordinates.";
        board[i][j] = piece;
    }
}
