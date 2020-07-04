package connections;

import java.util.ArrayList;
import java.util.List;

public class Union {

    public Union(Node baseNode){
        this.unionName = baseNode.getNodeName();
        this.baseNode = baseNode;
        this.nodeList.add(baseNode);
    }
    private String unionName;
    private List<Node> nodeList = new ArrayList<>();
    private boolean visited = false;
    private double totalCurrent1 ;
    private double totalCurrent2 ;
    private Node baseNode ;

    public boolean isVisited(){
        return visited;
    }
    public void makeVisited(){
        this.visited = true;
    }


    public void addNode(Node node){
        if(!nodeList.contains(node)){
            nodeList.add(node);
        }
    }

    public boolean equals(Union union){
        return union.unionName.equals(this.unionName);
    }
}
