package net.rexbutler.dtchess.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.rexbutler.dtchess.Move;
import net.rexbutler.dtchess.Position;
import net.rexbutler.dtchess.notation.NotationIn;
import net.rexbutler.dtchess.notation.NotationOut;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import au.com.bytecode.opencsv.CSVReader;

@RunWith(Parameterized.class)
public class MoveLogicSuite {

    private final String FEN;
    private final String legalMoves;
    private final String illegalMoves;
    private final String id;

    public MoveLogicSuite(TestPosition testPosition) {
        FEN = testPosition.getFEN();
        legalMoves = testPosition.getLegalMoves();
        illegalMoves = testPosition.getIllegalMoves();
        id = testPosition.getId();
    }

    // Creates the test data
    @Parameters
    public static Collection<TestPosition[]> data() throws IOException {
        final String dataFileName = "./DTChessTests.tsv";
        final File testPositionsFile = new File(dataFileName);
        FileReader testPositionsReader;
        CSVReader csvReader;
        final Collection<TestPosition[]> toReturn = new ArrayList<TestPosition[]>();

        boolean skipFirstLineFlag = true;

        testPositionsReader = new FileReader(testPositionsFile);
        csvReader = new CSVReader(testPositionsReader, '\t');
        final List<String[]> testPositions = csvReader.readAll();

        for (final String[] testPositionString : testPositions) {
            if (skipFirstLineFlag) {
                skipFirstLineFlag = false;
                continue;
            }
            final TestPosition[] testPositionHolder = new TestPosition[1];
            final TestPosition testPosition = new TestPosition();

            testPosition.setFEN(testPositionString[0]);
            testPosition.setLegalMoves(testPositionString[1]);
            testPosition.setIllegalMoves(testPositionString[2]);
            testPosition.setId(testPositionString[3]);

            testPositionHolder[0] = testPosition;

            toReturn.add(testPositionHolder);
        }

        csvReader.close();
        return toReturn;
    }

    private String[] splitMoves(String moves) {
        if (moves.trim().equals("")) {
            return new String[0];
        } else {
            return moves.split(",");
        }
    }

    @Test
    public void testIsLegalMove() {
        Position position; // = NotationIn.positionFromFEN(FEN);
        final String[] legalMoveNotations = splitMoves(legalMoves);
        final String[] illegalMoveNotations = splitMoves(illegalMoves);

        final Move[] legalMoves = new Move[legalMoveNotations.length];
        final Move[] illegalMoves = new Move[illegalMoveNotations.length];

        for (int i = 0; i < legalMoveNotations.length; i++) {
            try {
                legalMoves[i] = NotationIn.moveFromNotation(legalMoveNotations[i]);
            } catch (final IllegalArgumentException e) {
                assertTrue("Malformed move string: " + legalMoveNotations[i], false);
            }
        }

        for (int i = 0; i < illegalMoveNotations.length; i++) {
            try {
                illegalMoves[i] = NotationIn.moveFromNotation(illegalMoveNotations[i]);
            } catch (final IllegalArgumentException e) {
                assertTrue("Malformed move string: " + illegalMoveNotations[i], false);
            }
        }
        
        for (int i = 0; i < legalMoves.length; i++) {
            position = NotationIn.positionFromFEN(FEN);
            
            String assertNote = "\n[" + id + "]\n[" + FEN + "]\nThe move [" + legalMoveNotations[i] + "] should be legal.";
            assertTrue(assertNote, position.isLegalMove(legalMoves[i], true));
        }

        for (int i = 0; i < illegalMoves.length; i++) {
            position = NotationIn.positionFromFEN(FEN);
            
            String assertNote = "\n[" + id + "]\n[" + FEN + "]\nThe move [" + illegalMoveNotations[i] + "] should -not- be legal.";
            assertFalse(assertNote, position.isLegalMove(illegalMoves[i], true));
        }
    }
}
