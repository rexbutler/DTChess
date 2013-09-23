package net.rexbutler.dtchess.movelogic;

import java.util.HashSet;

import net.rexbutler.dtchess.Chess;
import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.MoveVector;
import net.rexbutler.dtchess.Piece;
import net.rexbutler.dtchess.PieceColor;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.Square;

public class PawnEnPassantLogic implements PieceLogic{
    private static final HashSet<MoveVector> possibleVectors = new HashSet<>();
    
    static {
        possibleVectors.add(new MoveVector(1, 1));
        possibleVectors.add(new MoveVector(-1, -1));
        possibleVectors.add(new MoveVector(-1, 1));
        possibleVectors.add(new MoveVector(1, -1));               
    }
    
    public PawnEnPassantLogic() {
    }
    
    public HashSet<MoveVector> getPossibleVectors() {
        return possibleVectors;
    }    
    
    @Override
    public boolean caseApplies(Position position, Move move) {
        PieceType pieceType = position.getPieceAt(move.getStartSquare()).getType();
        boolean isRightPiece = pieceType.equals(PieceType.PAWN);
        boolean isCapture = position.isCapture(move);
        boolean isRightDeltaX = (move.deltaX() != 0);
        
        return (isRightPiece && isRightDeltaX && !isCapture);
    }

    @Override    
    public boolean isLegal(Position position, Move move) {
        final int x1 = move.getStartSquare().getX();
        final int y1 = move.getStartSquare().getY();
        final int x2 = move.getEndSquare().getX();
        final int y2 = move.getEndSquare().getY();

        final int dx = x2 - x1;
        final int dy = y2 - y1;
        final int adx = Math.abs(dx);
        final int ady = Math.abs(dy);

        final Piece pawnToMove = position.getPieceAt(move.getStartSquare());
        final Square enPassantSquare = position.getEnPassantSquare();

        if (!position.isMovablePieceAtSquare(move.getStartSquare())) {
            return false;
        }
        if (pawnToMove.getType() != relevantPiece()) {
            return false;
        }
        if (move.getPromotionPieceType() != PieceType.NONE) {
            return false;
        }

        if (adx != 1 || ady != 1) {
            return false;
        }
        if (dy == Chess.WHITE_PAWN_Y_STEP && pawnToMove.getColor() == PieceColor.BLACK) {
            return false;
        }
        if (dy == Chess.BLACK_PAWN_Y_STEP && pawnToMove.getColor() == PieceColor.WHITE) {
            return false;
        }

        if (!position.getPieceAt(move.getEndSquare()).equals(Piece.NONE)) {
            return false;
        }
        // If an En Passant move is from (x1,y1) to (x2,y2) it
        // captures the piece at (x2,y1)
        if (position.getPieceAt(new Square(x2, y1)).equals(Piece.NONE)) {
            return false;
        }

        if (enPassantSquare == null) {
            return false;
        }
        if (enPassantSquare.getX() != x2 || enPassantSquare.getY() != y2) {
            return false;
        }
        return true;
    }

    @Override
    public boolean apply(Position position, Move move) {
        Square enPassantSquare = new Square(move.getEndSquare().getX(), move.getStartSquare().getY());
        position.movePiece(move);
        // Clear the En Passant Square
        position.setPieceAt(enPassantSquare, Piece.NONE); // TODO

        position.updateCastlingRights(move);
        position.updateBackgroundInfo(true);
        return true;
    }

    @Override
    public PieceType relevantPiece() {
        return PieceType.PAWN;
    }    
}
