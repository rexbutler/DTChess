/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess;

import java.util.HashSet;

import net.rexbutler.dtchess.movelogic.ChessLogic;
import net.rexbutler.dtchess.movelogic.SimpleChessLogic;
import net.rexbutler.dtchess.movelogic.MoveLogic;
import net.rexbutler.dtchess.movelogic.PawnCaptureLogic;
import net.rexbutler.dtchess.movelogic.VectorLogic;
import net.rexbutler.dtchess.notation.NotationOut;

public class Position extends PositionState {

    public Position(boolean addInitialPieces) {
        super(addInitialPieces);
    }

    public Position(PositionState position) {
        super(position);
    }

    public HashSet<Move> allLegalMoves(boolean strictOnly) {
        MoveLogic chessLogic; 
        HashSet<Move> possibleMoves = new HashSet<>();
        final HashSet<Move> legalMoves = new HashSet<>();

        if(strictOnly) {
            chessLogic = new ChessLogic();
        } else {
            chessLogic = new SimpleChessLogic();
        }
        
        possibleMoves = possibleMoves();
        for (final Move move : possibleMoves) {
            if (chessLogic.isLegal(this, move)) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    public boolean isLegal(Move move, boolean strictOnly) {
        MoveLogic chessLogic; 

        if(strictOnly) {
            chessLogic = new ChessLogic();
        } else {
            chessLogic = new SimpleChessLogic();
        }    
        return chessLogic.isLegal(this, move);
    }
        
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

    public boolean movePiece(Move move) {
        final Piece pieceToMove = board[move.getStartSquare().getX()][move.getStartSquare().getY()];

        board[move.getEndSquare().getX()][move.getEndSquare().getY()] = pieceToMove;
        board[move.getStartSquare().getX()][move.getStartSquare().getY()] = Piece.NONE; // TODO

        return true; // TODO
    }

    public HashSet<Move> possibleMoves() {
        final HashSet<Move> moves = new HashSet<>();

        for (int bx = 0; bx < Chess.BOARD_SIZE; bx++) {
            for (int by = 0; by < Chess.BOARD_SIZE; by++) {
                final Square square1 = new Square(bx, by);

                if (!this.getPieceAt(new Square(bx, by)).equals(Piece.NONE)) {
                    moves.addAll(possibleMovesWhichStartAt(square1));
                }
            }
        }
        return moves;
    }

    public HashSet<Move> possibleMovesWhichStartAt(Square square1) {
        final HashSet<Move> moves = new HashSet<>();
        final PieceType pieceType = this.getPieceAt(square1).getType();

        for (final MoveVector moveVector : Chess.pieceVectors.get(pieceType)) {
            final Square square2 = square1.addVector(moveVector);
            if (square2.isOnBoard()) {
                moves.add(new Move(square1, square2));
            }
        }
        return moves;
    }

    public HashSet<Move> possibleMovesWhichEndAt(Square square2) {
        final HashSet<Move> moves = new HashSet<>();
        int x2 = square2.getX();
        int y2 = square2.getY();

        for (int x1 = 0; x1 < Chess.BOARD_SIZE; x1++) {
            for (int y1 = 0; y1 < Chess.BOARD_SIZE; y1++) {
                int adx = Math.abs(x2 - x1);
                int ady = Math.abs(y2 - y1);

                if (board[x1][y1].equals(Piece.NONE)) {
                    continue;
                }

                if (board[x1][y1].getColor() != getColorToMove()) {
                    continue;
                }

                if ((adx == 0) && (ady == 0)) {
                    continue;
                }

                if ((adx == 0) || (ady == 0) || (adx == ady) || (adx == 2 && ady == 1) || (adx == 1 && ady == 2)) {
                    Square square1 = new Square(x1, y1);

                    moves.add(new Move(square1, square2));
                    if (board[x1][y1].getType() == PieceType.PAWN) {
                        moves.add(new Move(square1, square2, PieceType.QUEEN));
                        moves.add(new Move(square1, square2, PieceType.KNIGHT));
                        moves.add(new Move(square1, square2, PieceType.ROOK));
                        moves.add(new Move(square1, square2, PieceType.BISHOP));
                    }
                }
            }
        }

        return moves;
    }

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

    public boolean isCapture(Move move) {
        final PieceColor pieceColor1 = this.getPieceAt(move.getEndSquare()).getColor();
        final PieceColor pieceColor2 = this.getPieceAt(move.getStartSquare()).getColor();

        if (pieceColor1 == PieceColor.NONE || pieceColor2 == PieceColor.NONE) {
            return false;
        }

        return (pieceColor1 != pieceColor2);
    }

    public boolean isCaptureOfOwnColor(Move move) {
        final PieceColor pieceColor1 = this.getPieceAt(move.getStartSquare()).getColor();
        final PieceColor pieceColor2 = this.getPieceAt(move.getEndSquare()).getColor();

        if (pieceColor1 == PieceColor.NONE) {
            return false;
        }

        return (pieceColor1 == pieceColor2);
    }

    public boolean isKingToMoveInCheck() {
        Position opponentToMovePosition = new Position(this);
        opponentToMovePosition.setColorToMove(this.getColorToMove().invert());

        return opponentToMovePosition.isKingLeftInCheck();
    }

    public boolean isKingLeftInCheck() {
        MoveLogic vectorLogic = new VectorLogic();
        MoveLogic pawnCaptureLogic = new PawnCaptureLogic();

        HashSet<Move> attackingMoves;
        PieceColor colorToMove = this.getColorToMove();
        int kx = -1; // If search succeeds, these should be set to "real" values
        int ky = -1;

        squareloop: for (int j = 0; j < Chess.BOARD_SIZE; j++) {
            for (int i = 0; i < Chess.BOARD_SIZE; i++) {
                Piece pieceHere = this.getPieceAt(new Square(i, j));
                if (pieceHere.getType() == PieceType.KING && pieceHere.getColor() == colorToMove.invert()) {
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
            if (vectorLogic.isLegal(this, move) || pawnCaptureLogic.isLegal(this, move)) {
                return true;
            }
        }
        return false;
    }

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

    public boolean isCheckmate() {
        if (allLegalMoves(true).size() == 0) {
            if (isKingToMoveInCheck()) {
                return true;
            }
        }
        return false;
    }

    public boolean isDraw() {
        if (allLegalMoves(true).size() == 0) {
            if (!isKingToMoveInCheck()) {
                return true;
            }
        }
        return false;
    }
}
