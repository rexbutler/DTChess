/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess;

/**
 * Represents the vector of a move, or in other words the difference in x and y coordinates before
 * and after a given move.
 * 
 * @author Rex Butler
 */
public class MoveVector {
    public final static int ABS_VECTOR_LIMIT = Chess.BOARD_SIZE - 1;

    public MoveVector(Move move) {
        dx = move.getEndSquare().getX() - move.getStartSquare().getX();
        dy = move.getEndSquare().getY() - move.getStartSquare().getY();
    }

    public MoveVector(int aDeltaX, int aDeltaY) {
        dx = aDeltaX;
        dy = aDeltaY;
    }

    public MoveVector reverse() {
        return new MoveVector(-dx, -dy);
    }

    public int getDeltaX() {
        return dx;
    }

    public int getDeltaY() {
        return dy;
    }

    public boolean inRange() {
        final boolean possibleX = (Math.abs(dx) <= ABS_VECTOR_LIMIT);
        final boolean possibleY = (Math.abs(dy) <= ABS_VECTOR_LIMIT);

        return possibleX && possibleY;
    }

    @Override
    public boolean equals(Object otherObject) {
        MoveVector other;

        if (this == otherObject) {
            return true;
        }
        if (this == null) {
            return false;
        }
        if (!(otherObject instanceof MoveVector)) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }

        other = (MoveVector) otherObject;

        return (other.dx == dx) && (other.dy == dy);
    }

    @Override
    public int hashCode() {
        return (dy * Chess.BOARD_SIZE) + dx; // Unique
    }

    @Override
    public String toString() {
        return "(" + dx + "," + dy + ")"; // Unique
    }

    private final int dx;
    private final int dy;
}
