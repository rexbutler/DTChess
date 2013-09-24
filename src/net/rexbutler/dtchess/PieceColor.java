package net.rexbutler.dtchess;

/**
 * Represents the color of a piece on the chessboard.
 *  
 * @author rex
 */
public enum PieceColor {

    WHITE("White"),
    BLACK("Black"),
    NONE("None");

    private final String description;

    private PieceColor(String aDescription) {
        description = aDescription;
    }

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