package net.rexbutler.dtchess;

/**
 * Represents the result of a game, including an unfinished values for games in progress.
 */
public enum GameResult {

    // @formatter:off
    WIN_FOR_WHITE("1-0", PieceColor.WHITE),
    DRAW("1/2-1/2", PieceColor.NONE),
    WIN_FOR_BLACK("0-1", PieceColor.BLACK),
    UNFINISHED("", PieceColor.NONE);
    // @formatter:off
    
    private final String description;
    private final PieceColor colorWhoWins;

    private GameResult(String aDescription, PieceColor whoWins) {
        description = aDescription;
        colorWhoWins = whoWins;
    }

    public String getDescription() {
        return description;
    }

    public PieceColor getColorWhoWins() {
        return colorWhoWins;
    }
}