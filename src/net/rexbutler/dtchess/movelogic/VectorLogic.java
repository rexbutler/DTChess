package net.rexbutler.dtchess.movelogic;

import java.util.HashSet;

import net.rexbutler.dtchess.Chess;
import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.MoveVector;
import net.rexbutler.dtchess.Piece;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Square;

public abstract class VectorLogic {
    abstract public HashSet<MoveVector> getPossibleVectors();
    
    public boolean isLegal(Position position, Move move) {
        final HashSet<MoveVector> possibleVectors = getPossibleVectors();
        final MoveVector moveVector = new MoveVector(move);
        final Square s1 = move.getStartSquare();
        final Square s2 = move.getEndSquare();
        final PieceType pieceType = position.getPieceAt(move.getStartSquare()).getType();

        if(pieceType.equals(PieceType.PAWN)) {
            return false;
        }
        if (!position.isMovablePieceAtSquare(s1)) {
            return false;
        }
        if (position.isCaptureOfOwnColor(move)) {
            return false;
        }
        if (!move.getPromotionPieceType().equals(PieceType.NONE)) {
            return false;
        }
        if(!possibleVectors.contains(moveVector)) {
            return false;
        }
        
        if (!possibleVectors.contains(moveVector)) {
            return false;
        }

        for (final Square square : Chess.getSquaresBetween(s1, s2, false)) {
            if (!position.getPieceAt(square).equals(Piece.NONE)) {
                return false;
            }
        }
        return true;
    }

    public boolean apply(Position position, Move move) {
        position.movePiece(move);
        position.updateCastlingRights(move);
        position.updateBackgroundInfo(position.isCapture(move));        
        return true;
    }
}
