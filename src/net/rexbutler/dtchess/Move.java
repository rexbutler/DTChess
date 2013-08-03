package net.rexbutler.dtchess;

public class Move {
    public Move(Square StartSquare, Square EndSquare) {
        this(StartSquare, EndSquare, PieceType.NONE);
    }

    public Move(Square startSquare, Square endSquare, PieceType promotionPieceType) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.promotionPieceType = promotionPieceType;
    }

    public Move(int x1, int y1, int x2, int y2) {
        this(new Square(x1, y1), new Square(x2, y2));
    }

    public Move(int x1, int y1, int x2, int y2, PieceType promotionPieceType) {
        this(new Square(x1, y1), new Square(x2, y2), promotionPieceType);
    }
    
    public Square getStartSquare() {
        return startSquare;
    }

    public Square getEndSquare() {
        return endSquare;
    }

    public PieceType getPromotionPieceType() {
        return promotionPieceType;
    }

    public int deltaX() {
        return endSquare.getX() - startSquare.getX();
    }

    public int deltaY() {
        return endSquare.getY() - startSquare.getY();
    }

    @Override
    public boolean equals(Object otherObject) {
        Move other;

        if (this == otherObject) {
            return true;
        }
        if (this == null) {
            return false;
        }
        if (!(otherObject instanceof Move)) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }

        other = (Move) otherObject;

        return (other.startSquare.equals(startSquare)) && (other.endSquare.equals(endSquare))
                && (other.promotionPieceType.equals(promotionPieceType));
    }

    @Override
    public int hashCode() {
        return (startSquare.hashCode() << 14) + (endSquare.hashCode() << 7) + promotionPieceType.ordinal();
    }

    private final Square startSquare;
    private final Square endSquare;
    private final PieceType promotionPieceType; // In most cases, this will be
                                                // PieceType.NONE
}
