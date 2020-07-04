package elements;

import connections.Node;

public abstract class Element {
    public final static int RESISTOR = 11;
    public final static int CAPACITOR = 12;
    public final static int INDUCTOR = 13;
    public final static int CURRENT_SOURCE = 1;
    public final static int VOLTAGE_SOURCE = 2;
    public final static int CCCS = 21;
    public final static int VCCS = 22;
    public final static int CCVS = 23;
    public final static int VCVS = 24;

    protected double vPre = 0;
    protected double v;
    protected double iPre = 0;
    protected double i;
    public Node nodeP;
    public Node nodeN;
    protected String elementName;

    public void nextStep(){
        iPre = i;
        vPre = v;
    }
    public String getName(){
        return elementName;
    }
    public void printNodes(){
        System.out.println("    Node Positive : " + nodeP.getNodeName());
        System.out.println("    Node Negative : " + nodeN.getNodeName());
    }
    public void setNodes(Node nodeP, Node nodeN){
        this.nodeN = nodeN;
        this.nodeP = nodeP;
    }
    public boolean equals(Element element){
        return element.elementName.equals(this.elementName);
    }
    public abstract void update();
    public abstract double getCurrentEnteringNode(Node node);
    public abstract int getElementType();

}
