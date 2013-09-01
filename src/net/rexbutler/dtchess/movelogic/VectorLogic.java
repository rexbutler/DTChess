package net.rexbutler.dtchess.movelogic;

import net.rexbutler.dtchess.Chess;
import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.Piece;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Square;

public class VectorLogic implements MoveLogic {

    @Override
    public boolean caseApplies(Position position, Move move) {
        PieceType pieceType = position.getPieceAt(move.getStartSquare()).getType();
        // Every knight, bishop, rook, queen or "regular" king move is covered in this case,
        // except for king castling moves.
        if(pieceType.equals(PieceType.KNIGHT) 
                || pieceType.equals(PieceType.BISHOP)
                || pieceType.equals(PieceType.ROOK)
                || pieceType.equals(PieceType.QUEEN)
                || pieceType.equals(PieceType.KING)) {
            
            // This is necessary to rule out a castling move
            if(pieceType.equals(PieceType.KING) 
                    && Math.abs(move.deltaX()) >= Chess.CASTLING_ABS_DELTA_X) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isLegal(Position position, Move move) {
        final Square s1 = move.getStartSquare();
        final Square s2 = move.getEndSquare();

        final PieceType pieceType = position.getPieceAt(move.getStartSquare()).getType();

        if(pieceType == PieceType.PAWN) {
            return false;
        }
        if (!position.isMovablePieceAtSquare(s1)) {
            return false;
        }
        if (position.isCaptureOfOwnColor(move)) {
            return false;
        }
        if (move.getPromotionPieceType() != PieceType.NONE) {
            return false;
        }

        if (!Chess.isPossibleVectorForPiece(pieceType, s1, s2)) {
            return false;
        }

        for (final Square square : Chess.getSquaresBetween(s1, s2, false)) {
            if (!position.getPieceAt(square).equals(Piece.NONE)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean apply(Position position, Move move) {
        position.movePiece(move);
        return true;
    }

}
