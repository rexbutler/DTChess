package net.rexbutler.dtchess.movelogic;

import java.util.HashSet;

import net.rexbutler.dtchess.Chess;
import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.MoveVector;
import net.rexbutler.dtchess.Piece;
import net.rexbutler.dtchess.PieceColor;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Square;

public class PawnAdvanceLogic implements PieceLogic {
    private static final HashSet<MoveVector> possibleVectors = new HashSet<>();

    public PawnAdvanceLogic() {
        possibleVectors.add(new MoveVector(0, 2));
        possibleVectors.add(new MoveVector(0, 1));
        possibleVectors.add(new MoveVector(0, -1));
        possibleVectors.add(new MoveVector(0, -2));
    }
    
    public HashSet<MoveVector> getPossibleVectors() {
        return possibleVectors;
    }    
    
    @Override
    public boolean caseApplies(Position position, Move move) {
        PieceType pieceType = position.getPieceAt(move.getStartSquare()).getType();
        boolean isRightPiece = pieceType.equals(PieceType.PAWN);
        boolean isRightDeltaX = (move.deltaX() == 0);
        
        return (isRightPiece && isRightDeltaX);
    }

    @Override
    public boolean isLegal(Position position, Move move) {
        final int x1 = move.getStartSquare().getX();
        final int y1 = move.getStartSquare().getY();
        final int x2 = move.getEndSquare().getX();
        final int y2 = move.getEndSquare().getY();

        final int dx = x2 - x1;
        final int dy = y2 - y1;
        final int ady = Math.abs(dy);

        final Piece pawnToMove = position.getPieceAt(move.getStartSquare());
        final PieceColor pawnColor = pawnToMove.getColor();
        final boolean promotionMove = (move.getPromotionPieceType() != PieceType.NONE);

        if (pawnToMove.getType() != PieceType.PAWN) {
            return false;
        }

        if (!position.isMovablePieceAtSquare(move.getStartSquare())) {
            return false;
        }
        
        if (position.isCapture(move) || position.isCaptureOfOwnColor(move)) {
            return false;
        }

        if (dx != 0) {
            return false;
        }

        if (dy < 0 && pawnColor == PieceColor.WHITE) {
            return false;
        }
        if (dy == 0) {
            return false;
        }
        if (dy > 0 && pawnColor == PieceColor.BLACK) {
            return false;
        }

        if (ady == 0) {
            return false;
        } else if (ady == 1) {
            if (promotionMove) {
                if (pawnColor == PieceColor.WHITE && y2 != Chess.WHITE_PAWN_PROMOTED_Y) {
                    return false;
                } else if (pawnColor == PieceColor.BLACK && y2 != Chess.BLACK_PAWN_PROMOTED_Y) {
                    return false;
                }
            }
        } else if (ady == 2) {
            if (pawnColor == PieceColor.WHITE) {
                if (y1 != Chess.WHITE_PAWN_START_Y || !position.getPieceAt(new Square(x1, y1 + 1)).equals(Piece.NONE)) {
                    return false;
                }
            } else if (pawnColor == PieceColor.BLACK) {
                if (y1 != Chess.BLACK_PAWN_START_Y || !position.getPieceAt(new Square(x1, y1 - 1)).equals(Piece.NONE)) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    @Override
    public boolean apply(Position position, Move move) {
        final Piece pawnToMove = position.getPieceAt(move.getStartSquare());

        if (move.getEndSquare().getY() == 0 || move.getEndSquare().getY() == 7) {
            final Piece promotionPiece = new Piece(pawnToMove.getColor(), move.getPromotionPieceType());
            position.setPieceAt(move.getEndSquare(), promotionPiece);
            position.setPieceAt(move.getStartSquare(), Piece.NONE);
        } else {
            position.movePiece(move);
        }

        position.updateCastlingRights(move);
        position.updateBackgroundInfo(true);
        return true;        
    }
    
    @Override
    public PieceType relevantPiece() {
        return PieceType.PAWN;
    }
}
