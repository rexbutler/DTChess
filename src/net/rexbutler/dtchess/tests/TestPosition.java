package net.rexbutler.dtchess.tests;

/**
 * A data class representing one compound test of move validation. Each test consisting of a test
 * id, one chess position (as a FEN string), and the expected legal and illegal moves given that position.
 * 
 * @author Rex Butler
 */
public class TestPosition {
    public String getFEN() {
        return FEN;
    }

    public void setFEN(String fEN) {
        FEN = fEN;
    }

    public String getLegalMoves() {
        return legalMoves;
    }

    public void setLegalMoves(String legalMoves) {
        this.legalMoves = legalMoves;
    }

    public String getIllegalMoves() {
        return illegalMoves;
    }

    public void setIllegalMoves(String illegalMoves) {
        this.illegalMoves = illegalMoves;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String FEN;
    String legalMoves;
    String illegalMoves;
    String id;
}
