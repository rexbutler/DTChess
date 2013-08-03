/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess.tests;

import java.util.HashSet;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.NotationIn;
import net.rexbutler.dtchess.NotationOut;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.Square;

import org.junit.*;
import static org.junit.Assert.assertTrue;

public class NaiveTests {

    public static void main(String[] args) {
        drawTest();
    }

    public static void drawTest() {
        Position position;
        final String fenPosition = "4k3/4P3/4K3/8/8/8/8/8 b - - 0 1";
        
        position = NotationIn.positionFromFEN(fenPosition);
        System.out.println(position.isKingToMoveInCheck());
        System.out.println(position.isDraw());
        System.out.println(position.isCheckmate());
        
//        Move E8F8 = NotationIn.moveFromNotation("e8f8");
//        System.out.println(position.isLegalMove(E8F8, true));
//        
//        for(Move move : position.allLegalMoves(true)) {
//            System.out.println(NotationOut.notationOfMove(move));
//        }
    }
    
    public static void checkmateTest() {
        Position p = new Position(true);
        Move move;
        String[] moves = {"f2f4", "e7e6", "g2g4", "d8h4"};
        
        for(int i = 0; i < moves.length; i++) {
            move = NotationIn.moveFromNotation(moves[i]);
            System.out.println(i + " " + moves[i]);
            assertTrue(p.isLegalMove(move, true));
            p.applyMove(move);
            System.out.println(NotationOut.longDescription(p));        
        }
        
        for(Move mv : p.allLegalMoves(true)) {
            System.out.println(NotationOut.notationOf(p, mv));
        }
        
        System.out.println(p.isCheckmate());
    }
    
    public static void test1() {
        Position position;
        final String fenPosition = "r3k2r/8/8/8/1b6/8/8/R3K2R w KQkq - 0 1";

        Square e1 = new Square(4, 0);
        position = NotationIn.positionFromFEN(fenPosition);

        for(Move move: position.possibleMovesWhichEndAt(e1)) {
            System.out.println(move.getStartSquare());
        }
    }

}
