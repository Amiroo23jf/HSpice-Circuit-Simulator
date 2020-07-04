package connections;

import base.Database;

import java.util.ArrayList;
import java.util.List;

public class Union {

    public Union(Node baseNode){
        this.baseNode = baseNode;
        System.out.println("Union " + baseNode.getNodeName() + " Created");
        this.unionName = baseNode.getNodeName();
        this.nodeList.add(baseNode);
    }
    private String unionName;
    private List<Node> nodeList = new ArrayList<>();
    private boolean visited = false;
    private double totalCurrent1 ;
    private double totalCurrent2 ;
    private Node baseNode ;

    //Updater
    public void addNode(Node node){
        if(!nodeList.contains(node)){
            nodeList.add(node);
        }
    }
    private void findTotalCurrents(double deltaV){
        totalCurrent1 = 0;
        for (Node node : nodeList){
            totalCurrent1 = totalCurrent1 + node.enteringCurrent();
        }
        totalCurrent2 = 0;
        for (Node node : nodeList){
            node.increaseV(deltaV);
        }
        for (Node node : nodeList){
            totalCurrent2 = totalCurrent2 + node.enteringCurrent();
        }
        for (Node node : nodeList){
            node.resetV();
        }
    }
    public void updateVoltage(double deltaV , double deltaI){
        if(this.nodeList.contains(Database.getInstance().earthNode)){
            return;
        }
        findTotalCurrents(deltaV);
        double dI = totalCurrent1 - totalCurrent2;
        double dV = totalCurrent2/dI * deltaV;
        if(dI < 0){
            dV = -dV;
        }
        //System.out.println("Entering current difference is : " + dI);
        //System.out.println("Entering current 1 is : " + totalCurrent1);
        //System.out.println("Entering Current 2 is : " + totalCurrent2);
        for(Node node : nodeList){
            node.increaseV(dV);
            node.updateV();
        }
    }

    //Extras


    public String getUnionName(){
        return unionName;
    }
    public boolean equals(Union union){
        return union.unionName.equals(this.unionName);
    }


    //Prints


    public void printUnionNodes() {
        System.out.println("Union " + unionName );
        System.out.println(" Nodes:");
        for(Node node : nodeList){
            System.out.println("       " + node.getNodeName());
        }
        System.out.println("_____________");
    }
}
