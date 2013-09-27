package net.rexbutler.dtchess;

/**
 * Represents the color of a piece on the chessboard.
 * 
 * @author Rex Butler
 */
public enum PieceColor {

    // @formatter:off
    WHITE("White"),
    BLACK("Black"),
    NONE("None");
    // @formatter:on

    private final String description;

    private PieceColor(String aDescription) {
        description = aDescription;
    }

    /**
     * Returns the color opposite this color.
     * @return White if this color is set to black, and vise versa.
     */
    public PieceColor invert() {
        if (this == WHITE) {
            return BLACK;
        } else if (this == BLACK) {
            return WHITE;
        } else {
            return NONE;
        }
    }

    public String getDescription() {
        return description;
    }
}