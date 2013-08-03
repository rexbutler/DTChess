package net.rexbutler.dtchess;

import java.util.HashSet;

public class DTChess {

    public static void main(String[] args) {
        Position position = NotationIn.positionFromFEN(args[0]);
        
        HashSet<Move> legalMoves = position.allLegalMoves(true);
        boolean isCheckmate = position.isCheckmate();
        boolean isDraw = position.isDraw();
        //boolean isConsistent = false;
        
        System.out.println(NotationOut.longDescription(position));
        System.out.println("Checkmate: " + isCheckmate);
        System.out.println("Draw: " + isDraw);
        //System.out.println("Consistent: " + isConsistent);
        
        System.out.println("Number of Legal Moves: " + legalMoves.size());
        System.out.print("Legal moves: ");
        for(Move move: legalMoves) {
            System.out.print(NotationOut.notationOfMove(move) + " ");
        }
        System.out.println("");
    }

}
