package net.rexbutler.dtchess;

import java.util.ArrayList;


/**
 * Represents a complete chess game.
 * 
 * @author Rex Butler
 */
public class Game {
    private Position initialPosition;
    private ArrayList<Move> gameMoves;
    private Position currentPosition;
    private GameResult gameResult = GameResult.UNFINISHED;

    public Game() {
    }
    
    public boolean reset() {
        return true;
    }

    public boolean clear() {
        return true;
    }

    public boolean isConsistent() {
        return true;
    }

    public Position getStartingPosition() {
        return initialPosition;
    }

    public void setStartingPosition(Position initialPosition) {
        this.initialPosition = initialPosition;
    }

    public Position getEndingPosition() {
        return currentPosition;
    }

    public void setEndingPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public ArrayList<Move> getGameMoves() {
        return gameMoves;
    }

    public void setGameMoves(ArrayList<Move> gameMoves) {
        this.gameMoves = gameMoves;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }
    
    public boolean canClaimRepetitionDraw() {
        return false;  // TODO
    }
}
