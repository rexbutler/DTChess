package net.rexbutler.dtchess.movelogic;

import java.util.HashSet;

import net.rexbutler.dtchess.CastleLocation;
import net.rexbutler.dtchess.Chess;
import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.PieceColor;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Square;

public class KingCastlingLogic implements MoveLogic {

    @Override
    public boolean caseApplies(Position position, Move move) {
        PieceType pieceType = position.getPieceAt(move.getStartSquare()).getType();
        if(pieceType.equals(PieceType.KING) 
                && Math.abs(move.deltaX()) == Chess.CASTLING_ABS_DELTA_X) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isLegal(Position position, Move move) {
        HashSet<Square> squaresBetweenKingAndRook;
        HashSet<Square> squaresBetweenKingStartAndEnd;

        if(position.isKingToMoveInCheck()) {
            return false;
        }
        
        final CastleLocation castleLocation = CastleLocation.getCastlingType(move);
        if (castleLocation == null || !position.getCastleRights(castleLocation)) {
            return false;
        }

        final Square kingStartSquare = move.getStartSquare();
        final Square rookStartSquare = castleLocation.getRookMove().getStartSquare();

        final PieceColor playerToMoveColor = position.getColorToMove();

        // King of appropriate color on castling starting square
        if (position.getPieceAt(kingStartSquare).getType() != PieceType.KING) {
            return false;
        }
        if (position.getPieceAt(rookStartSquare).getColor() != playerToMoveColor) {
            return false;
        }

        // Rook of appropriate color on castling starting square
        if (position.getPieceAt(rookStartSquare).getType() != PieceType.ROOK) {
            return false;
        }
        if (position.getPieceAt(rookStartSquare).getColor() != playerToMoveColor) {
            return false;
        }

        // squares Between King and Rook empty
        squaresBetweenKingAndRook = Chess.getSquaresBetween(castleLocation.getKingMove().getStartSquare(), castleLocation
                .getRookMove().getStartSquare(), false);
        for (final Square betweenSquare : squaresBetweenKingAndRook) {
            if (position.isAttackedByColor(betweenSquare, playerToMoveColor.invert())) {
                return false;
            }
        }

        // squares (inclusive) Between King Start and End not attacked
        squaresBetweenKingStartAndEnd = Chess.getSquaresBetween(castleLocation.getKingMove().getStartSquare(), castleLocation
                .getKingMove().getEndSquare(), true);
        for (final Square betweenSquare : squaresBetweenKingStartAndEnd) {
            if (position.isAttackedByColor(betweenSquare, playerToMoveColor.invert())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean apply(Position position, Move move) {
        final CastleLocation castleLocation = CastleLocation.getCastlingType(move);
        final Move rookMove = castleLocation.getRookMove();
        position.movePiece(move);
        position.movePiece(rookMove);
        return true;
    }
}
