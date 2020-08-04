package connections;

import base.Database;
import elements.Element;

import java.util.*;

public class Union {

    public Union(Node baseNode){
        this.baseNode = baseNode;
        Database.getInstance().log("    Union " + baseNode.getNodeName() + " Created");
        this.unionName = baseNode.getNodeName();
        this.nodeList.add(baseNode);
    }
    private String unionName;
    private List<Node> nodeList = new ArrayList<>();
    private boolean visited = false;
    private double totalCurrent1 ;
    private double totalCurrent2 ;
    private Node baseNode ;
    private Map<Element, Boolean> VSCurrentMap = new HashMap<>();

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
        //System.out.println("Total1 : "+totalCurrent1);
        //System.out.println("Total2 : " + totalCurrent2);
        //System.out.println(dI);
        double dV = totalCurrent1/deltaI * deltaV;
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
        Database.getInstance().log("        Union " + unionName );
        Database.getInstance().log("            Nodes:");
        for(Node node : nodeList){
            Database.getInstance().log("                " + node.getNodeName());
        }
    }

    public void updateNodesVoltage() {
        resetNodesUpdated();
        while(!areAllNodesUpdated()){
            for(Node node : nodeList){
                //Database.getInstance().log("Node is : "+node.getNodeName());
                if(node.isUpdated()){
                    for(Node connectedNode : node.connectedNodes){
                        if(!connectedNode.isUpdated() && connectedNode.isFromSameUnion(node)){
                            //Database.getInstance().log("    ConnectedNode is : "+connectedNode.getNodeName());
                            Element element = node.getVoltageSourceWithNode(connectedNode);
                            //Database.getInstance().log("        Element is : " + element.getName());
                            if(element.getName().equals("EMPTY")){
                                continue;
                            }
                            connectedNode.setV(node.getV() + element.increaseNodeVoltage(connectedNode));
                            connectedNode.updateV();
                            connectedNode.makeUpdated(true);
                        }
                    }
                }
            }
        }
    }

    //Creating Graph

    private boolean areAllNodesUpdated() {
        for(Node node : nodeList){
            if(!node.isUpdated()){
                return false;
            }
        }
        return true;
    }

    private void resetNodesUpdated() {
        for(Node node : nodeList){
            node.makeUpdated(false);
            node.makeAddedToUpdateQueue(false);
        }
        baseNode.makeUpdated(true);

    }

    //Voltage Source Current

    public void resetVSCurrents() {
        for(Node node : nodeList){
            node.resetVSCurrent();
        }
    }

    public void findVSCurrents() {
        while(!isAllVSCurrentsFound()){
            for(Node node : nodeList){
                if(node.numberOfNotFoundVSCurrents() == 1){
                    node.findVSCurrent();
                }
            }
        }
    }

    private boolean isAllVSCurrentsFound() {
        for(Node node : nodeList){
            if(!node.isVSCurrentsFound()){
                return false;
            }
        }
        return true;
    }


    //Error Checkers

    public boolean isErrorTwo() {
        if(!areAllEnteringElementsCS()){
            return false;
        }
        return !isKclEstablished();
    }

    public boolean isErrorThree() {
        if(!hasCycle()){
            return false;
        }
        return !isKvlEstablished();
    }

    private boolean isKvlEstablished() {
        //needs to be changed
        return true;
    }

    private boolean hasCycle() {
        //needs to be changed
        return false;
    }

    public boolean isKclEstablished() {
        double current = 0;
        for(Node node : nodeList){
            current = current + node.enteringCurrent();
        }
        return Math.abs(current) < Database.getDeltaI();
    }

    private boolean areAllEnteringElementsCS() {
        for(Node node : nodeList){
            if(!node.areAllEnteringElementsCS()){
                return false;
            }
        }
        return true;
    }

}
