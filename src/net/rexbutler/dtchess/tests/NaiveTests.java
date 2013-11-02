/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess.tests;

import java.util.HashSet;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.Square;
import net.rexbutler.dtchess.movelogic.SimpleChessLogic;
import net.rexbutler.dtchess.notation.NotationIn;
import net.rexbutler.dtchess.notation.NotationOut;

import org.junit.*;
import static org.junit.Assert.assertTrue;


/**
 * A few ad hoc and temporary tests of DTChess library functionality.
 * 
 * @author Rex Butler
 */
public class NaiveTests {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        Position position;
        final String fenPosition = "4k3/4P3/4K3/8/8/8/8/8 b - - 0 1";
        
        position = NotationIn.positionFromFEN(fenPosition);
        System.out.println(position.isKingToMoveInCheck());
        System.out.println(position.isStatemate());
        System.out.println(position.isCheckmate());
    }
    
    public static void test2() {
        Position position;
        final String fenPosition = "r3k2r/8/8/8/1b6/8/8/R3K2R w KQkq - 0 1";

        Square e1 = new Square(4, 0);
        position = NotationIn.positionFromFEN(fenPosition);

        for(Move move: position.possibleMovesWhichEndAt(e1)) {
            System.out.println(move.getStartSquare());
        }
    }
}
