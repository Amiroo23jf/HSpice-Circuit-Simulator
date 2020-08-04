package connections;

import base.Database;
import elements.Element;
import elements.VoltageSource;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Node {
    //public static Node EMPTY_NODE = new Node("EMPTY_NODE_x00-1");


    public Node(String nodeName) throws IOException {
        this.nodeName = nodeName;
        this.union = new Union(this);
        makeInfoFile();
    }

    public Union union;
    private boolean added = false;
    private boolean updated = false;
    private boolean addedToUpdateQueue = false;
    private String nodeName;
    private double v;
    private double vPre = 0;
    public List<Node> connectedNodes = new ArrayList<>();
    private List<Element> elementList = new ArrayList<>();
    String fileAddress;


    //Updater
    public void increaseV(double deltaV) {
        v = vPre + deltaV;
    }

    public void setV(double v) {
        this.v = v;
    }

    public void resetV() {
        v = vPre;
    }

    public void updateV() {
        vPre = v;
    }

    public double getV() {
        return v;
    }

    public double enteringCurrent() {
        double current = 0;
        for (Element element : elementList) {
            if ((element.getElementType() != Element.VCVS) && (element.getElementType() != Element.CCVS) && (element.getElementType() != Element.VOLTAGE_SOURCE)) {
                //element.update();
                //System.out.println("Entering current from element " + element.getName() + " = " + element.getCurrentEnteringNode(this));
                current = current + element.getCurrentEnteringNode(this);
            }
        }
        //System.out.println("Entering current to " + nodeName + " = "+current);
        return current;
    }


    //Initial
    public void addConnectedNode(Node connectedNode) {
        if (!connectedNodes.contains(connectedNode)) {
            connectedNodes.add(connectedNode);
        }
    }

    public boolean isConnectedToNode(Node node) {
        for (Element element : elementList) {
            if (element.nodeN.equals(node) || element.nodeP.equals(node)) {
                return true;
            }
        }
        return false;
    }

    public void addElement(Element element) {
        if (!elementList.contains(element)) {
            elementList.add(element);
        }
    }

    public boolean equals(Node node) {
        return node.getNodeName().equals(this.getNodeName());
    }

    public void makeAdded() {
        added = true;
    }

    public boolean isAdded() {
        return added;
    }

    public void makeUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void makeAddedToUpdateQueue(boolean checker) {
        this.addedToUpdateQueue = checker;
    }

    public boolean isAddedToUpdateQueue() {
        return addedToUpdateQueue;
    }

    //Extras
    public String getNodeName() {
        return nodeName;
    }

    public Element getVoltageSourceWithNode(Node node) {
        Element chosenElement = new VoltageSource("EMPTY", 0, 0, 0, 0);
        for (Element element : elementList) {
            if (element.getOtherNode(this).equals(node)) {
                if (element.getElementType() == Element.VOLTAGE_SOURCE || element.getElementType() == Element.CCVS || element.getElementType() == Element.VCVS) {
                    return element;
                }
            }
        }
        return chosenElement;
    }

    public boolean elementBetweenIsVoltageSource(Node connectedNode) {
        for (Element element : this.elementList) {
            if (element.nodeP.equals(connectedNode) || element.nodeN.equals(connectedNode)) {
                int elementType = element.getElementType();
                if (elementType == Element.VOLTAGE_SOURCE || elementType == Element.VCVS || elementType == Element.CCVS) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFromSameUnion(Node node) {
        return (this.union.equals(node.union));

    }

    //Files
    public void makeInfoFile() throws IOException{
        fileAddress = Database.getInstance().savingSrc + "/Nodes/" + nodeName + ".txt";
        Database.getInstance().log("    Node " + nodeName + " Created");
        Database.getInstance().log("        Node Src Address is : " + fileAddress);
        FileWriter writer = new FileWriter(fileAddress);
        // time   voltage   Current
        writer.write("");
        writer.close();
    }
    public void updateInfoFile() throws IOException {
        FileWriter writer = new FileWriter(fileAddress, true);
        writer.append(Database.t() + "   " + this.getV() + "   " + this.enteringCurrent() + "\n");
        writer.close();
    }
    public String getAddress() {
        return fileAddress;
    }

    //Printers
    public void printElementList() {
        for (Element element : elementList) {
            Database.getInstance().log("            " + element.getName());
        }


    }

    //Calculating Voltage Sources' Currents

    public void resetVSCurrent() {
        for (Element element : elementList){
            if (element.getElementType() == Element.VCVS || element.getElementType() == Element.CCVS || element.getElementType() == Element.VOLTAGE_SOURCE){
                element.resetCurrent();
                element.isCurrentCalculated = false;
            }
        }
    }

    public boolean isVSCurrentsFound() {
        for (Element element : elementList){
            if(Element.isVoltageSource(element)){
                if(!element.isCurrentCalculated){
                    return false;
                }
            }
        }
        return true;
    }

    public int numberOfNotFoundVSCurrents(){
        int i = 0;
        for(Element element : elementList){
            if(Element.isVoltageSource(element)){
                if(!element.isCurrentCalculated){
                    i++;
                }
            }
        }
        return i;
    }

    public void findVSCurrent() {
        double enteringCurrent = this.enteringCurrent();
        for(Element element : elementList){
            if (!element.isCurrentCalculated && Element.isVoltageSource(element)){
                element.setCurrent(this,enteringCurrent);
                element.isCurrentCalculated = true;
                return;
            }
        }
    }

    //Error Checkers

    public boolean areAllEnteringElementsCS() {
        for(Element element : elementList){
            if(!Element.isVoltageSource(element) && !Element.isCurrentSource(element)){
                return false;
            }
        }
        return true;
    }
}


