package net.rexbutler.dtchess.movelogic;

import net.rexbutler.dtchess.PieceType;

public interface PieceLogic extends MoveLogic {
    public PieceType relevantPiece();
}
