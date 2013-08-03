/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess;

public enum CastleLocation {

    WHITE_KING_SIDE(PieceColor.WHITE, CastleSide.KING_SIDE, new Move(new Square(4, 0), new Square(6, 0)), new Move(
            new Square(7, 0), new Square(5, 0))),

    WHITE_QUEEN_SIDE(PieceColor.WHITE, CastleSide.QUEEN_SIDE, new Move(new Square(4, 0), new Square(2, 0)), new Move(
            new Square(0, 0), new Square(3, 0))),

    BLACK_KING_SIDE(PieceColor.BLACK, CastleSide.KING_SIDE, new Move(new Square(4, 7), new Square(6, 7)), new Move(
            new Square(7, 7), new Square(5, 7))),

    BLACK_QUEEN_SIDE(PieceColor.BLACK, CastleSide.QUEEN_SIDE, new Move(new Square(4, 7), new Square(2, 7)), new Move(
            new Square(0, 7), new Square(3, 7)));

    private final PieceColor color;
    private final CastleSide side;
    private final Move castleKingMove;
    private final Move castleRookMove;

    private CastleLocation(PieceColor aColor, CastleSide aSide, Move aCastleKingMove, Move aCastleRookMove) {
        color = aColor;
        side = aSide;
        castleKingMove = aCastleKingMove;
        castleRookMove = aCastleRookMove;
    }

    public PieceColor getColor() {
        return color;
    }

    public CastleSide getSide() {
        return side;
    }

    public Move getKingMove() {
        return castleKingMove;
    }

    public Move getRookMove() {
        return castleRookMove;
    }

    public static CastleLocation getCastlingType(Move aCastleKingMove) {
        for (final CastleLocation cL : CastleLocation.values()) {
            if (cL.castleKingMove.equals(aCastleKingMove)) {
                return cL;
            }
        }
        return null;
    }
}
