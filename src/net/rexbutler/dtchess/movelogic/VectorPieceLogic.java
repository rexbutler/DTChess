package net.rexbutler.dtchess.movelogic;

import java.util.HashSet;

import net.rexbutler.dtchess.MoveVector;

public interface VectorPieceLogic extends PieceLogic {
    public HashSet<MoveVector> getPossibleVectors();
}
