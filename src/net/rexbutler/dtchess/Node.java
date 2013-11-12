package net.rexbutler.dtchess;

public class Node {
    public static float NOT_EVALUATED = Float.NEGATIVE_INFINITY;
    
    public Node(Position nodePosition, float evaluation) {
        this.nodePosition = nodePosition;
        this.evaluation = evaluation;
    }

    public Node(Position nodePosition) {
        this(nodePosition, NOT_EVALUATED);
    }
    
    Position nodePosition;
    float evaluation;
}
