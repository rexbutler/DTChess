package net.rexbutler.dtchess.movelogic;

import java.util.HashSet;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.MoveVector;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Position;

public class QueenLogic extends VectorLogic implements PieceLogic {
    private static final HashSet<MoveVector> possibleVectors = new HashSet<>();
    
    static {
        for (int i = -1 * MoveVector.ABS_VECTOR_LIMIT; i <= MoveVector.ABS_VECTOR_LIMIT; i++) {
            if (i != 0) {
                possibleVectors.add(new MoveVector(i, 0));
                possibleVectors.add(new MoveVector(0, i));
                possibleVectors.add(new MoveVector(i, i));
                possibleVectors.add(new MoveVector(-i, i));
            }
        }        
    }
    
    public QueenLogic() {
    }
    
    @Override
    public HashSet<MoveVector> getPossibleVectors() {
        return possibleVectors;
    }
    
    @Override
    public boolean caseApplies(Position position, Move move) {
        PieceType pieceType = position.getPieceAt(move.getStartSquare()).getType();
        return pieceType.equals(relevantPiece());
    }
    
    @Override
    public PieceType relevantPiece() {
        return PieceType.QUEEN;
    }       
}