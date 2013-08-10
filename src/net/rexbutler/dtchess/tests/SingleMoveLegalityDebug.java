package net.rexbutler.dtchess.tests;

import static org.junit.Assert.assertTrue;
import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.PositionState;
import net.rexbutler.dtchess.notation.NotationIn;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SingleMoveLegalityDebug {

    private static String FEN;
    private static Position position;
    private static String moveNotation;
    private static boolean strict;
    private static Move move;
    private static boolean legal;

    @BeforeClass
    public static void oneTimeSetUp() {
        FEN = "K1k5/8/8/8/1pP1Pp2/8/8/8 b - e3 0 1";
        position = NotationIn.positionFromFEN(FEN);
        strict = true;
        
        moveNotation = "f4e3";
        legal = false;
        
        move = NotationIn.moveFromNotation(moveNotation);
    }

    @AfterClass
    public static void oneTimeTearDown() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSingleMoveLegality() {
        assertTrue(position.isLegalMove(move, strict) == legal);
    }
}