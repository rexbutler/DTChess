package net.rexbutler.dtchess;

public enum CastleSide {

    QUEEN_SIDE("Queenside"),
    KING_SIDE("Kingside");

    CastleSide(String aDescription) {
        description = aDescription;
    }

    public String getDescription() {
        return description;
    }

    private final String description;
};