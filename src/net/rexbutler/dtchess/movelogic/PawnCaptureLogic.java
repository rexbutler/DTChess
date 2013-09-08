package net.rexbutler.dtchess.movelogic;

import java.util.HashSet;

import net.rexbutler.dtchess.Chess;
import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.MoveVector;
import net.rexbutler.dtchess.Piece;
import net.rexbutler.dtchess.PieceColor;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.PieceType;

public class PawnCaptureLogic implements MoveLogic {
    private static final HashSet<MoveVector> possibleVectors = new HashSet<>();
    
    public PawnCaptureLogic() {
        possibleVectors.add(new MoveVector(1, 1));
        possibleVectors.add(new MoveVector(-1, -1));
        possibleVectors.add(new MoveVector(-1, 1));
        possibleVectors.add(new MoveVector(1, -1));        
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
        
        return (isRightPiece && isRightDeltaX && isCapture);
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

        final Piece pawnToMove = position.getPieceAt(move.getStartSquare());
        final PieceColor pawnColor = pawnToMove.getColor();

        if (pawnToMove.getType() != PieceType.PAWN) {
            return false;
        }
        if (!position.isMovablePieceAtSquare(move.getStartSquare())) {
            return false;
        }

        if (!position.isCapture(move)) {
            return false;
        }

        if (adx != 1) {
            return false;
        }
        if (pawnColor == PieceColor.WHITE && dy != 1) {
            return false;
        }
        if (pawnColor == PieceColor.BLACK && dy != -1) {
            return false;
        }

        if (pawnColor == PieceColor.WHITE && y2 == Chess.WHITE_PAWN_PROMOTED_Y) {
            if (move.getPromotionPieceType() == PieceType.NONE) {
                return false;
            }
        }

        if (pawnColor == PieceColor.BLACK && y2 == Chess.BLACK_PAWN_PROMOTED_Y) {
            if (move.getPromotionPieceType() == PieceType.NONE) {
                return false;
            }
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
}
