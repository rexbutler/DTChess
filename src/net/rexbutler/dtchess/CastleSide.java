package net.rexbutler.dtchess;


/**
 * An enumeration representing the two sides available for a castling move.
 * 
 * @author Rex Butler
 */
public enum CastleSide {

    // @formatter:off
    QUEEN_SIDE("Queenside"),
    KING_SIDE("Kingside");
    // @formatter:on
    
    private CastleSide(String aDescription) {
        description = aDescription;
    }

    public String getDescription() {
        return description;
    }

    private final String description;
}