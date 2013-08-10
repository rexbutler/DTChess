package net.rexbutler.dtchess.movetypes;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.PieceType;
import net.rexbutler.dtchess.Position;

public class KingCastlingMove implements MoveType {
    public boolean caseApplies(Position position, Move move) {
        return false;
    }
    public boolean isLegalMove(Position position, Move move) {
        return false;
    }
    public boolean applyMove(Position position, Move move) {
        return false;
    }
}