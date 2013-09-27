package net.rexbutler.dtchess;

/**
 * Represents the type of a piece on the chessboard, IE pawn, knight, bishop, or etc...
 * 
 * @author Rex Butler
 */
public enum PieceType {
    // @formatter: off
    NONE("None", false),
    PAWN("Pawn", false),
    KNIGHT("Knight", true),
    BISHOP("Bishop", true),
    ROOK("Rook", true),
    QUEEN("Queen",  true),
    KING("King", false);
    // @formatter: on
    
    private String description;
    private boolean promotionOption;

    private PieceType(String aDescription, boolean isOption) {
        description = aDescription;
        promotionOption = isOption;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPromotionOption() {
        return promotionOption;
    }
}
