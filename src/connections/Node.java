package connections;

import elements.Element;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int nodeNumber;
    private Union union;
    private boolean added = false;
    private String nodeName;
    private double v;
    private double vPre;
    private List<Node> connectedNodes = new ArrayList<>();
    private List<Element> elementList = new ArrayList<>();

    public double getV(){
        return v;
    }
    public void addConnectedNode(Node connectedNode){
        if (!connectedNodes.contains(connectedNode)){
            connectedNodes.add(connectedNode);
        }
    }
    public boolean isConnectedToNode(Node node){
        for (Element element : elementList){
            if(element.nodeN.equals(node) || element.nodeP.equals(node)){
                return true;
            }
        }
        return false;
    }
    public void addElement(Element element){
        if(!elementList.contains(element)){
            elementList.add(element);
        }
    }
    public boolean equals(Node node){
        if(node.getNodeName().equals(this.getNodeName())){
            return true;
        }
        return false;
    }

    public String getNodeName() {
        return nodeName;
    }

}
