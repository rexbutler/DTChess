/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess;

/**
 * Represents a square on the chessboard as an x and y coordinate.
 * 
 * In chess, if the the board is placed so the white pieces are on the near side of the board, the
 * corner square to the left is denoted "a1". This corresponds to (x,y) = (0,0) with x in
 * [0,1,...,7] mapping to the columns ["a","b",...,"h"] and y in [0,1,...,7] mapping to the rows
 * ["1","2",...,"8"]. For example, (2,4) corresponds to the square "b5". This is called the algebraic
 * notation of a square.
 * 
 * @author Rex Butler
 */
public class Square {

    public Square(int xx, int yy) {
        x = xx;
        y = yy;
    }

    public Square(Square square) {
        x = square.x;
        y = square.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isXOnBoard() {
        final boolean xIn = ((0 <= x) && (x < Chess.BOARD_SIZE));
        return xIn;
    }

    public boolean isYOnBoard() {
        final boolean yIn = ((0 <= y) && (y < Chess.BOARD_SIZE));
        return yIn;
    }

    public boolean isOnBoard() {
        return isXOnBoard() && isYOnBoard();
    }

    public Square addVector(MoveVector toAdd) {
        return new Square(x + toAdd.getDeltaX(), y + toAdd.getDeltaY());
    }

    @Override
    public boolean equals(Object otherObject) {
        Square other;

        if (this == otherObject) {
            return true;
        }
        if (this == null) {
            return false;
        }
        if (!(otherObject instanceof Square)) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }

        other = (Square) otherObject;

        return (other.x == x) && (other.y == y);
    }

    @Override
    public int hashCode() {
        return (y * Chess.BOARD_SIZE) + x;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    private final int x;
    private final int y;
}
