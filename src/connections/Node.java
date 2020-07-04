package connections;

import elements.Element;

import java.util.ArrayList;
import java.util.List;

public class Node {
    //public static Node EMPTY_NODE = new Node("EMPTY_NODE_x00-1");


    public Node(String nodeName){
        this.nodeName = nodeName;
        this.union = new Union(this);
    }

    public Union union ;
    private boolean added = false;
    private String nodeName;
    private double v ;
    private double vPre = 0;
    public List<Node> connectedNodes = new ArrayList<>();
    private List<Element> elementList = new ArrayList<>();


    //Updater
    public void increaseV(double deltaV){
        v = vPre + deltaV;
    }
    public void resetV(){
        v = vPre;
    }
    public void updateV(){
        vPre = v;
    }
    public double getV(){
        return v;
    }
    public double enteringCurrent(){
        double current = 0;
        for(Element element : elementList){
            if((element.getElementType() != Element.VCVS) && (element.getElementType() != Element.CCVS) && (element.getElementType() != Element.VOLTAGE_SOURCE)){
                element.update();
                //System.out.println("Entering current from element " + element.getName() + " = " + element.getCurrentEnteringNode(this));
                current = current + element.getCurrentEnteringNode(this);
            }
        }
        //System.out.println("Entering current to " + nodeName + " = "+current);
        return current;
    }


    //Initial
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
    public void makeAdded(){
        added = true;
    }
    public boolean isAdded(){
        return added;
    }


    //Extras
    public String getNodeName() {
        return nodeName;
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


    //Printers
    public void printElementList() {
        for(Element element : elementList){
            System.out.println("     " + element.getName());
        }


    }
}
