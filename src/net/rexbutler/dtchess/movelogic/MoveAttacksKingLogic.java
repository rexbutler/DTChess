package net.rexbutler.dtchess.movelogic;

import java.util.ArrayList;
import java.util.HashSet;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.MoveVector;
import net.rexbutler.dtchess.Position;

public class MoveAttacksKingLogic implements MoveLogic{

    public static final ArrayList<MoveLogic> CHECK_MOVE_LOGICS = new ArrayList<>();
    public static final MoveLogic PAWN_CAPTURE_LOGIC = new PawnCaptureLogic();
    public static final MoveLogic PAWN_EN_PASSANT_LOGIC = new PawnEnPassantLogic();
    public static final MoveLogic KNIGHT_LOGIC = new KnightLogic();
    public static final MoveLogic BISHOP_LOGIC = new BishopLogic();
    public static final MoveLogic ROOK_LOGIC = new RookLogic();
    public static final MoveLogic QUEEN_LOGIC = new QueenLogic();
    
    private static final HashSet<MoveVector> possibleVectors = new HashSet<>();    
   
    public MoveAttacksKingLogic() {
        CHECK_MOVE_LOGICS.add(PAWN_CAPTURE_LOGIC);
        CHECK_MOVE_LOGICS.add(PAWN_EN_PASSANT_LOGIC);
        CHECK_MOVE_LOGICS.add(KNIGHT_LOGIC);
        CHECK_MOVE_LOGICS.add(BISHOP_LOGIC);
        CHECK_MOVE_LOGICS.add(ROOK_LOGIC);        
        CHECK_MOVE_LOGICS.add(QUEEN_LOGIC);        
        for(MoveLogic moveLogic : CHECK_MOVE_LOGICS) {
            possibleVectors.addAll(moveLogic.getPossibleVectors());
        }
    }

    public HashSet<MoveVector> getPossibleVectors() {
        return possibleVectors;
    }   
    
    @Override
    public boolean caseApplies(Position position, Move move) {
        return true;
    }

    @Override
    public boolean isLegal(Position position, Move move) {
        for(MoveLogic moveLogic : CHECK_MOVE_LOGICS) {
            if(moveLogic.caseApplies(position, move)) {
                return moveLogic.isLegal(position, move);
            }
        }
        assert false : "Unexpected alternative in move validation.";
        return false;
    }

    @Override
    public boolean apply(Position position, Move move) {
        for(MoveLogic moveLogic : CHECK_MOVE_LOGICS) {
            if(moveLogic.caseApplies(position, move)) {
                return moveLogic.apply(position, move);
            }
        }
        assert false : "Unexpected alternative in move application.";
        return false;
    }
}
