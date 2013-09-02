package net.rexbutler.dtchess.movelogic;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.Position;

public class ChessLogic implements MoveLogic {
    public ChessLogic() {
    }
    
    @Override
    public boolean caseApplies(Position position, Move move) {
        return true;
    }

    @Override
    public boolean isLegal(Position position, Move move) {
        MoveLogic simpleChessLogic = new SimpleChessLogic();
        boolean legal = simpleChessLogic.isLegal(position, move);
        
        if (legal == false) {
            return false;
        } else {
            // At this point, check if the king is in check
            Position newPosition = new Position(position);
            simpleChessLogic.apply(newPosition, move);
            return !newPosition.isKingLeftInCheck();
        }
    }

    @Override
    public boolean apply(Position position, Move move) {
        MoveLogic simpleChessLogic = new SimpleChessLogic();
        simpleChessLogic.apply(position, move);
        return true;
    }

}
