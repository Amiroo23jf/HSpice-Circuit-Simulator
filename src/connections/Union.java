package connections;

import java.util.ArrayList;
import java.util.List;

public class Union {
    private List<Node> nodeList = new ArrayList<>();
    private boolean visited = false;
    private double totalCurrent1 ;
    private double totalCurrent2 ;
    private Node baseNode;


    public void makeVisited(){
        this.visited = true;
    }
}
