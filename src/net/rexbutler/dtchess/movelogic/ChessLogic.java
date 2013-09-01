package net.rexbutler.dtchess.movelogic;

import java.util.ArrayList;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.PieceType;

public class ChessLogic implements MoveLogic {

    public static final ArrayList<MoveLogic> ALL_LOGICS = new ArrayList<>();
    public static final MoveLogic KING_CASTLING_LOGIC = new KingCastlingLogic();
    public static final MoveLogic PAWN_ADVANCE_LOGIC = new PawnAdvanceLogic();
    public static final MoveLogic PAWN_CAPTURE_LOGIC = new PawnCaptureLogic();
    public static final MoveLogic PAWN_EN_PASSANT_LOGIC = new PawnEnPassantLogic();
    public static final MoveLogic VECTOR_LOGIC = new VectorLogic();

    public ChessLogic() {
        ALL_LOGICS.add(KING_CASTLING_LOGIC);
        ALL_LOGICS.add(PAWN_ADVANCE_LOGIC);
        ALL_LOGICS.add(PAWN_CAPTURE_LOGIC);
        ALL_LOGICS.add(PAWN_EN_PASSANT_LOGIC);
        ALL_LOGICS.add(VECTOR_LOGIC);
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
