package connections;

import elements.Element;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public static Node EMPTY_NODE = new Node("EMPTY_NODE_x00-1");


    Node(String nodeName){
        this.nodeName = nodeName;
    }
    public Union union = new Union(this);
    private boolean added = false;
    private String nodeName;
    private double v;
    private double vPre;
    public List<Node> connectedNodes = new ArrayList<>();
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
        return node.getNodeName().equals(this.getNodeName());
    }

    public String getNodeName() {
        return nodeName;
    }
    public void makeAdded(){
        added = true;
    }
    public boolean isAdded(){
        return added;
    }

    public void setUnion(Union union){
        this.union = union;
    }
    public boolean elementBetweenIsVoltageSource(Node connectedNode){
        for(Element element : this.elementList){
            if(element.nodeP.equals(connectedNode) || element.nodeN.equals(connectedNode)){
                int elementType = element.getElementType();
                if (elementType == Element.VOLTAGE_SOURCE || elementType == Element.VCVS || elementType == Element.CCVS){
                    return true;
                }
            }
        }
        return false;
    }


}
