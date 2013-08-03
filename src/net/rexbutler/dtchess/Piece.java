/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess;

/**
 * 
 * @author rex
 */
public class Piece {
    public static final Piece NONE = new Piece(PieceColor.NONE, PieceType.NONE);
    public static final Piece W_PAWN = new Piece(PieceColor.WHITE, PieceType.PAWN);
    public static final Piece W_KNIGHT = new Piece(PieceColor.WHITE, PieceType.KNIGHT);
    public static final Piece W_BISHOP = new Piece(PieceColor.WHITE, PieceType.BISHOP);
    public static final Piece W_ROOK = new Piece(PieceColor.WHITE, PieceType.ROOK);
    public static final Piece W_QUEEN = new Piece(PieceColor.WHITE, PieceType.QUEEN);
    public static final Piece W_KING = new Piece(PieceColor.WHITE, PieceType.KING);
    public static final Piece B_PAWN = new Piece(PieceColor.BLACK, PieceType.PAWN);
    public static final Piece B_KNIGHT = new Piece(PieceColor.BLACK, PieceType.KNIGHT);
    public static final Piece B_BISHOP = new Piece(PieceColor.BLACK, PieceType.BISHOP);
    public static final Piece B_ROOK = new Piece(PieceColor.BLACK, PieceType.ROOK);
    public static final Piece B_QUEEN = new Piece(PieceColor.BLACK, PieceType.QUEEN);
    public static final Piece B_KING = new Piece(PieceColor.BLACK, PieceType.KING);

    private final PieceColor color;
    private final PieceType type;

    public Piece(PieceColor aColor, PieceType aType) {
        type = aType;
        color = aColor;
    }
    
    public Piece(Piece piece) {
        type = piece.type;
        color = piece.color;
    }

    public PieceType getType() {
        return type;
    }

    public PieceColor getColor() {
        return color;
    }

    @Override
    public boolean equals(Object otherObject) {
        Piece other;

        if (this == otherObject) {
            return true;
        }
        if (this == null) {
            return false;
        }
        if (!(otherObject instanceof Piece)) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }

        other = (Piece) otherObject;

        return (other.color.equals(color)) && (other.type.equals(type));
    }

    @Override
    public int hashCode() {
        return (color.hashCode() << 5) + type.hashCode();
    }
}
