/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess;

import java.util.ArrayList;
import java.util.HashSet;

import net.rexbutler.dtchess.movelogic.BishopLogic;
import net.rexbutler.dtchess.movelogic.ChessLogic;
import net.rexbutler.dtchess.movelogic.KingCastlingLogic;
import net.rexbutler.dtchess.movelogic.KingLogic;
import net.rexbutler.dtchess.movelogic.KnightLogic;
import net.rexbutler.dtchess.movelogic.MoveAttacksKingLogic;
import net.rexbutler.dtchess.movelogic.PawnAdvanceLogic;
import net.rexbutler.dtchess.movelogic.PawnEnPassantLogic;
import net.rexbutler.dtchess.movelogic.PieceLogic;
import net.rexbutler.dtchess.movelogic.QueenLogic;
import net.rexbutler.dtchess.movelogic.RookLogic;
import net.rexbutler.dtchess.movelogic.SimpleChessLogic;
import net.rexbutler.dtchess.movelogic.MoveLogic;
import net.rexbutler.dtchess.movelogic.PawnCaptureLogic;
import net.rexbutler.dtchess.movelogic.VectorLogic;
import net.rexbutler.dtchess.notation.NotationOut;

/**
 * Extends the data only PositionState class with common methods related to the position of chess
 * board.
 * 
 * @author Rex Butler
 */
public class Position extends PositionState {

    /**
     * Constructor. Initialized a position with either an empty board or a board with the pieces in
     * initial position.
     * @param addInitialPieces True if the board the initial pieces are to be added.
     */
    public Position(boolean addInitialPieces) {
        super(addInitialPieces);
    }

    /**
     * Constructor. Initialize this position with a specific setup.
     * @param position The position data to initialize with.
     */
    public Position(PositionState position) {
        super(position);
    }

    /**
     * Returns a collection containing all legal moves in this given position.
     * @param strictOnly True if testing legality in the usual sense: moves which put one's king in
     * check are not allowed, or false under "blitz" / chess engine rules where such moves should be
     * considered.
     * @return A HashSet containing the legal moves.
     */
    public HashSet<Move> allLegalMoves(boolean strictOnly) {
        MoveLogic chessLogic;
        HashSet<Move> possibleMoves = potentialMoves();
        final HashSet<Move> legalMoves = new HashSet<>();

        if (strictOnly) {
            chessLogic = new ChessLogic();
        } else {
            chessLogic = new SimpleChessLogic();
        }

        for (final Move move : possibleMoves) {
            if (chessLogic.isLegal(this, move)) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    /**
     * Determines whether a given move is legal in the current position.
     * @param move The move to validate.
     * @param strictOnly True if testing legality in the usual sense: moves which put one's king in
     * check are not allowed, or false under "blitz" / chess engine rules where such moves should be
     * considered.
     * @return The legality of the move as a boolean.
     */
    public boolean isLegal(Move move, boolean strictOnly) {
        MoveLogic chessLogic;

        if (strictOnly) {
            chessLogic = new ChessLogic();
        } else {
            chessLogic = new SimpleChessLogic();
        }
        return chessLogic.isLegal(this, move);
    }

    /**
     * Updates the auxiliary, non-board setup related, information about a chess position given a
     * move has just been made.
     * 
     * @param resetHalfMoveClock Flag determining whether the halfMoveClock count is reset to zero.
     */
    public void updateBackgroundInfo(boolean resetHalfMoveClock) {
        // Invert the color to move
        colorToMove = colorToMove.invert();
        // Clear the En Passant square, E.P. capture is available for only one
        // move
        enPassantSquare = null;
        // Reset or increment half move clock
        if (resetHalfMoveClock) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }
        // Increment the full move count
        fullMoveCount++;
    }

    /**
     * Updates the castling rights given that a certain move has been made, i.e. if a king or rook
     * has moved the associated castling move is no longer legal.
     * @param move The move made.
     */
    public void updateCastlingRights(Move move) {
        // If pieces move off of the king or rook castling start square, that
        // castling option is
        // no longer available
        for (CastleLocation castleLocation : CastleLocation.values()) {
            Square kingStartSquare = castleLocation.getKingMove().getStartSquare();
            Square rookStartSquare = castleLocation.getRookMove().getStartSquare();

            if (move.getStartSquare().equals(kingStartSquare)
                    || move.getStartSquare().equals(rookStartSquare)) {

                castlingRights.put(castleLocation, false);
            }
        }
    }

    /**
     * A helper method to execute a simple non-compound move on this position. This applies to all
     * moves except pawn promotion moves, pawn en passant moves, and caslting moves.
     * @param move The move to execute.
     * @return A flag indicating the move was successfully executed.
     */
    public boolean movePiece(Move move) {
        final Piece pieceToMove = board[move.getStartSquare().getX()][move.getStartSquare().getY()];

        setPieceAt(move.getEndSquare(), pieceToMove);
        setPieceAt(move.getStartSquare(), Piece.NONE);
        return true;
    }

    /**
     * Returns a collection of potentially legal moves determined by whether the move has the move
     * vector for the given piece.
     * 
     * @return A HashSet of the potential moves.
     */
    public HashSet<Move> potentialMoves() {
        final HashSet<Move> moves = new HashSet<>();

        for (int bx = 0; bx < Chess.BOARD_SIZE; bx++) {
            for (int by = 0; by < Chess.BOARD_SIZE; by++) {
                final Square square1 = new Square(bx, by);

                if (!this.getPieceAt(new Square(bx, by)).equals(Piece.NONE)) {
                    moves.addAll(potentialMovesWhichStartAt(square1));
                }
            }
        }
        return moves;
    }

    /**
     * Returns a collection of potentially legal moves determined by whether the move has the move
     * vector for the given piece.
     * 
     * @param startSquare The start square of the potential moves.
     * @return A HashSet of the resulting moves.
     */
    public HashSet<Move> potentialMovesWhichStartAt(Square startSquare) {
        final ArrayList<PieceLogic> pieceLogics = new ArrayList<>();
        final HashSet<Move> moves = new HashSet<>();
        final PieceType pieceType = this.getPieceAt(startSquare).getType();

        pieceLogics.add(new PawnAdvanceLogic()); // TODO: Refactor
        pieceLogics.add(new PawnCaptureLogic());
        pieceLogics.add(new PawnEnPassantLogic());
        pieceLogics.add(new KnightLogic());
        pieceLogics.add(new BishopLogic());
        pieceLogics.add(new RookLogic());
        pieceLogics.add(new QueenLogic());
        pieceLogics.add(new KingLogic());
        pieceLogics.add(new KingCastlingLogic());

        for (PieceLogic pieceLogic : pieceLogics) {
            if (pieceLogic.relevantPiece().equals(pieceType)) {
                for (MoveVector moveVector : pieceLogic.getPossibleVectors()) {
                    final Square square2 = startSquare.addVector(moveVector);
                    if (square2.isOnBoard()) {
                        moves.add(new Move(startSquare, square2));
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Returns a collection of potentially legal moves determined by whether the move has the move
     * vector for the given piece.
     * 
     * @param endSquare The end square of the potential moves.
     * @return A HashSet of the resulting moves.
     */
    public HashSet<Move> possibleMovesWhichEndAt(Square endSquare) {
        final HashSet<Move> moves = new HashSet<>();
        int x2 = endSquare.getX();
        int y2 = endSquare.getY();

        for (int x1 = 0; x1 < Chess.BOARD_SIZE; x1++) {
            for (int y1 = 0; y1 < Chess.BOARD_SIZE; y1++) {

                if (!this.isMovablePieceAtSquare(new Square(x1, y1))) {
                    continue;
                }

                MoveVector moveVector = new MoveVector(Math.abs(x2 - x1), Math.abs(y2 - y1));

                if (Chess.vectorMask(moveVector)) {
                    Square square1 = new Square(x1, y1);

                    moves.add(new Move(square1, endSquare));
                    if (board[x1][y1].getType().equals(PieceType.PAWN)) {
                        moves.add(new Move(square1, endSquare, PieceType.QUEEN));
                        moves.add(new Move(square1, endSquare, PieceType.KNIGHT));
                        moves.add(new Move(square1, endSquare, PieceType.ROOK));
                        moves.add(new Move(square1, endSquare, PieceType.BISHOP));
                    }
                }
            }
        }

        return moves;
    }

    /**
     * Determines whether a particular square is attacked by a piece of the given color.
     */
    public boolean isAttackedByColor(Square target, PieceColor color) {
        MoveLogic simpleChessLogic = new SimpleChessLogic();
        Position modPosition;

        if (this.getPieceAt(target).getColor().invert() != color) {
            return false;
        }

        if (this.getColorToMove() != color) {
            modPosition = new Position(this);
            modPosition.setColorToMove(color);
        } else {
            modPosition = this;
        }

        for (int i = 0; i < Chess.BOARD_SIZE; i++) {
            for (int j = 0; j < Chess.BOARD_SIZE; j++) {
                final Square source = new Square(i, j);
                final Move move = new Move(source, target);
                if (simpleChessLogic.isLegal(this, move)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Determines whether a move is a capture of a piece of one color taking a piece of another
     * color and thus a legal (in that sense).
     */
    public boolean isCapture(Move move) {
        final PieceColor pieceColor1 = this.getPieceAt(move.getEndSquare()).getColor();
        final PieceColor pieceColor2 = this.getPieceAt(move.getStartSquare()).getColor();

        if (pieceColor1 == PieceColor.NONE || pieceColor2 == PieceColor.NONE) {
            return false;
        }

        return (pieceColor1 != pieceColor2);
    }

    /**
     * Determines whether a move is a capture of a piece of one color taking a piece of the same
     * color and thus not legal.
     */
    public boolean isCaptureOfOwnColor(Move move) {
        final PieceColor pieceColor1 = this.getPieceAt(move.getStartSquare()).getColor();
        final PieceColor pieceColor2 = this.getPieceAt(move.getEndSquare()).getColor();

        if (pieceColor1 == PieceColor.NONE) {
            return false;
        }

        return (pieceColor1 == pieceColor2);
    }

    /**
     * Determines whether the king with the move is currently attacked by a piece of the opposite
     * color.
     */
    public boolean isKingToMoveInCheck() {
        Position opponentToMovePosition = new Position(this);
        opponentToMovePosition.setColorToMove(this.getColorToMove().invert());

        return opponentToMovePosition.isKingLeftInCheck();
    }

    /**
     * Determines whether the king can be captured by the player with the move.
     */
    public boolean isKingLeftInCheck() {
        MoveLogic attacksKingLogic = new MoveAttacksKingLogic();

        HashSet<Move> attackingMoves;
        PieceColor colorToMove = this.getColorToMove();
        int kx = -1; // If search succeeds, these should be set to "real" values
        int ky = -1;

        squareloop:
        for (int j = 0; j < Chess.BOARD_SIZE; j++) {
            for (int i = 0; i < Chess.BOARD_SIZE; i++) {
                Piece pieceHere = this.getPieceAt(new Square(i, j));
                if (pieceHere.getType() == PieceType.KING
                        && pieceHere.getColor() == colorToMove.invert()) {
                    kx = i;
                    ky = j;
                    break squareloop;
                }
            }
        }

        assert ((kx != -1) || (ky != -1)) : "Unexpected search failure, inconsistent state.  King not found.";
        Square kingSquare = new Square(kx, ky);

        attackingMoves = possibleMovesWhichEndAt(kingSquare);

        for (Move move : attackingMoves) {
            if (attacksKingLogic.isLegal(this, move)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether the piece at the given square can be moved, i.e. that there is actually a
     * piece there and that its color matches the color to move.
     */
    public boolean isMovablePieceAtSquare(Square startSquare) {
        final Piece pieceToMove = getPieceAt(startSquare);

        // There should be a piece on the starting square
        if (pieceToMove.equals(Piece.NONE)) {
            return false;
        }
        // The color of the piece being moved must be the color
        // of the player to move
        return (pieceToMove.getColor() == getColorToMove());
    }

    /**
     * Determines whether the current position is a checkmate. In other words, that the player to
     * move's king is attacked and that there are no legal moves available.
     */
    public boolean isCheckmate() {
        if (allLegalMoves(true).size() == 0) {
            if (isKingToMoveInCheck()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether the current position is a stalemate. In other words, the player to move
     * has no legal moves available but that the king is not attacked.
     */
    public boolean isStatemate() {
        if (allLegalMoves(true).size() == 0) {
            if (!isKingToMoveInCheck()) {
                return true;
            }
        }
        return false;
    }
}
