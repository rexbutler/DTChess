package net.rexbutler.dtchess;

public enum PieceType {

    NONE("None", false),
    PAWN("Pawn", false),
    KNIGHT("Knight", true),
    BISHOP("Bishop", true),
    ROOK("Rook", true),
    QUEEN("Queen",  true),
    KING("King", false);

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
