/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rexbutler.dtchess.tests;

import java.util.HashSet;

import net.rexbutler.dtchess.Chess;
import net.rexbutler.dtchess.Square;

public class DebugTools {
    public static boolean[][] squareListToArray(HashSet<Square> squares) {
        final boolean[][] grid = new boolean[Chess.BOARD_SIZE][Chess.BOARD_SIZE];

        for (final Square square : squares) {
            grid[square.getX()][square.getY()] = true;
        }
        return grid;
    }

    public static String gridString(boolean[][] grid) {
        String gridStr = "";

        for (int j = Chess.BOARD_SIZE - 1; j >= 0; j--) {
            for (int i = 0; i < Chess.BOARD_SIZE; i++) {
                if (grid[i][j]) {
                    gridStr += "*";
                } else {
                    gridStr += "-";
                }
            }
            gridStr += "\n";
        }
        return gridStr;
    }
}
