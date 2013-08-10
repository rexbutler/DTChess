package net.rexbutler.dtchess.movetypes;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Position;

public interface MoveType {
    public boolean caseApplies(Position position, Move move);
    public boolean isLegalMove(Position position, Move move);
    public boolean applyMove(Position position, Move move);
}
