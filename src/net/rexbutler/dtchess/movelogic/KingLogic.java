package net.rexbutler.dtchess.movelogic;

import java.util.HashSet;

import net.rexbutler.dtchess.Chess;
import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.MoveVector;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Position;

public class KingLogic extends VectorLogic implements PieceLogic {
    private static final HashSet<MoveVector> possibleVectors = new HashSet<>();

    public KingLogic() {
        possibleVectors.add(new MoveVector(0, 1));
        possibleVectors.add(new MoveVector(0, -1));
        possibleVectors.add(new MoveVector(1, 0));
        possibleVectors.add(new MoveVector(-1, 0));
        possibleVectors.add(new MoveVector(1, 1));
        possibleVectors.add(new MoveVector(-1, -1));
        possibleVectors.add(new MoveVector(-1, 1));
        possibleVectors.add(new MoveVector(1, -1));
    }
    
    @Override
    public HashSet<MoveVector> getPossibleVectors() {
        return possibleVectors;
    }   
    
    @Override
    public boolean caseApplies(Position position, Move move) {
        PieceType pieceType = position.getPieceAt(move.getStartSquare()).getType();
        MoveVector moveVector = new MoveVector(move);
        boolean rightPiece = pieceType.equals(PieceType.KING);
        boolean rightDx = Math.abs(moveVector.getDeltaX()) < Chess.CASTLING_ABS_DELTA_X;
        return rightPiece && rightDx;
    }

    @Override
    public PieceType relevantPiece() {
        return PieceType.KING;
    }
}
