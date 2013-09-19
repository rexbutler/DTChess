package net.rexbutler.dtchess.movelogic;

import net.rexbutler.dtchess.PieceType;

public interface SpecificMoveLogic extends MoveLogic {
    public PieceType relevantPiece();
}
