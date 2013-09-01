package net.rexbutler.dtchess.movelogic;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.Position;

public interface MoveLogic {
     public boolean caseApplies(Position position, Move move);
     public boolean isLegal(Position position, Move move);
     public boolean apply(Position position, Move move);
}
