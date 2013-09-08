package net.rexbutler.dtchess.movelogic;

import java.util.HashSet;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.MoveVector;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Position;

public class RookLogic extends VectorLogic implements MoveLogic {
    private static final HashSet<MoveVector> possibleVectors = new HashSet<>();

    public RookLogic() {
        for (int i = -1 * MoveVector.ABS_VECTOR_LIMIT; i <= MoveVector.ABS_VECTOR_LIMIT; i++) {
            if (i == 0) {
                continue;
            }
            possibleVectors.add(new MoveVector(i, 0));
            possibleVectors.add(new MoveVector(0, i));
        }            
    }
    
    public HashSet<MoveVector> getPossibleVectors() {
        return possibleVectors;
    }
    
    @Override
    public boolean caseApplies(Position position, Move move) {
        PieceType pieceType = position.getPieceAt(move.getStartSquare()).getType();
        return pieceType.equals(PieceType.ROOK);
    }
}