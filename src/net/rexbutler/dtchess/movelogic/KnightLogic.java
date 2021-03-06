package net.rexbutler.dtchess.movelogic;

import java.util.HashSet;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.MoveVector;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Position;

public class KnightLogic extends VectorLogic implements PieceLogic {
    private static final HashSet<MoveVector> possibleVectors = new HashSet<>();

    static {
        possibleVectors.add(new MoveVector(1, 2));
        possibleVectors.add(new MoveVector(2, 1));
        possibleVectors.add(new MoveVector(-1, 2));
        possibleVectors.add(new MoveVector(-2, 1));
        possibleVectors.add(new MoveVector(1, -2));
        possibleVectors.add(new MoveVector(2, -1));
        possibleVectors.add(new MoveVector(-1, -2));
        possibleVectors.add(new MoveVector(-2, -1));        
    }
    
    public KnightLogic() {
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
        return PieceType.KNIGHT;
    }    
}
