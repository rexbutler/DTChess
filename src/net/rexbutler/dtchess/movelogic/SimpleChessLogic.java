package net.rexbutler.dtchess.movelogic;

import java.util.ArrayList;
import java.util.HashSet;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.MoveVector;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.PieceType;

public class SimpleChessLogic implements MoveLogic {
    public static final ArrayList<MoveLogic> ALL_LOGICS = new ArrayList<>();
    public static final MoveLogic PAWN_ADVANCE_LOGIC = new PawnAdvanceLogic();
    public static final MoveLogic PAWN_CAPTURE_LOGIC = new PawnCaptureLogic();
    public static final MoveLogic PAWN_EN_PASSANT_LOGIC = new PawnEnPassantLogic();
    public static final MoveLogic KNIGHT_LOGIC = new KnightLogic();
    public static final MoveLogic BISHOP_LOGIC = new BishopLogic();
    public static final MoveLogic ROOK_LOGIC = new RookLogic();
    public static final MoveLogic QUEEN_LOGIC = new QueenLogic();
    public static final MoveLogic KING_LOGIC = new KingLogic();
    public static final MoveLogic KING_CASTLING_LOGIC = new KingCastlingLogic();
    
    private static final HashSet<MoveVector> possibleVectors = new HashSet<>();    
   
    public SimpleChessLogic() {
        ALL_LOGICS.add(PAWN_ADVANCE_LOGIC);
        ALL_LOGICS.add(PAWN_CAPTURE_LOGIC);
        ALL_LOGICS.add(PAWN_EN_PASSANT_LOGIC);
        ALL_LOGICS.add(KNIGHT_LOGIC);
        ALL_LOGICS.add(BISHOP_LOGIC);
        ALL_LOGICS.add(ROOK_LOGIC);        
        ALL_LOGICS.add(QUEEN_LOGIC);        
        ALL_LOGICS.add(KING_LOGIC);
        ALL_LOGICS.add(KING_CASTLING_LOGIC);
        
        for(MoveLogic moveLogic : ALL_LOGICS) {
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
        for(MoveLogic moveLogic : ALL_LOGICS) {
            if(moveLogic.caseApplies(position, move)) {
                return moveLogic.isLegal(position, move);
            }
        }
        assert false : "Unexpected alternative in move validation.";
        return false;
    }

    @Override
    public boolean apply(Position position, Move move) {
        for(MoveLogic moveLogic : ALL_LOGICS) {
            if(moveLogic.caseApplies(position, move)) {
                return moveLogic.apply(position, move);
            }
        }
        assert false : "Unexpected alternative in move application.";
        return false;
    }
}
