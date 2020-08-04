package elements;

import base.Database;
import connections.Node;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
    public final static int ELEMENT_LENGTH = 50;

    protected double vPre = 0;
    protected double v;
    protected double iPre = 0;
    protected double i;
    public Node nodeP;
    public Node nodeN;
    protected String elementName;
    private String fileAddress;
    public boolean isCurrentCalculated = false;

    public void nextStep(){
        iPre = i;
        vPre = v;
    }

    //info

    public void makeNewInfoFile() throws IOException {
        fileAddress = Database.getInstance().savingSrc + "/Elements/" + elementName + ".txt";
        Database.getInstance().log("    Element Src Address is : " + fileAddress);
        FileWriter writer = new FileWriter(fileAddress);
        // time   voltage   current
        writer.write("");
        writer.close();

    }
    public void updateInfoFile() throws IOException {
        FileWriter writer = new FileWriter(fileAddress,true);
        writer.append(Database.t() + "   " + this.getV() + "   " + this.getI() + "   " + this.getPower() + "\n");
        writer.close();
    }

    public void printNodes(){
        Database.getInstance().log("            Positive Node : " + nodeP.getNodeName());
        Database.getInstance().log("            Negative Node : " + nodeN.getNodeName());
    }

    // getters and setter

    public String getName(){
        return elementName;
    }
    public Node getOtherNode(Node node){
        if(node.equals(nodeP)){
            return nodeN;
        }
        return nodeP;
    }
    public double getV(){
        return v;
    }
    public double getI() {
        return i;
    }
    public double getPower() {
        if(Element.isCurrentSource(this)){
            return -v*i;
        }
        return v*i;
    }
    public void setNodes(Node nodeP, Node nodeN){
        this.nodeN = nodeN;
        this.nodeP = nodeP;
    }
    public int getNodeP(){
        return Integer.parseInt(nodeP.getNodeName());
    }
    public int getNodeN(){
        return Integer.parseInt(nodeN.getNodeName());
    }


    // booleans
    public boolean equals(Element element){
        return element.elementName.equals(this.elementName);
    }
    public static boolean isVoltageSource(Element element){
        int elementType = element.getElementType();
        return elementType == Element.VCVS || elementType == Element.CCVS || elementType == Element.VOLTAGE_SOURCE;
    }
    public static boolean isCurrentSource(Element element){
        int elementType = element.getElementType();
        return elementType == Element.VCCS || elementType == Element.CCCS || elementType == Element.CURRENT_SOURCE;
    }
    //abstracts
    public abstract void update();
    public abstract double getCurrentEnteringNode(Node node);
    public abstract int getElementType();
    public abstract double increaseNodeVoltage(Node connectedNode);

    //Reset

    public void resetCurrent(){
        this.i = 0;
        this.iPre = 0;
    }
    public void setCurrent(Node node , double enteringCurrent) {
        if (node.equals(nodeP)){
            this.i = enteringCurrent;
        }
        else{
            this.i = -enteringCurrent;
        }

    }

    //Graphics

    public abstract void drawElement(Graphics g, Dimension dimension1, Dimension dimension2);
    public Dimension drawLine(Graphics g, Dimension dimension, double length, double angle){
        int x = (int)(dimension.width + length*Math.cos(angle));
        int y = (int)(dimension.height + length*Math.sin(angle));
        Dimension fDim = new Dimension(x,y);
        g.drawLine(dimension.width,dimension.height,x,y);
        return fDim;
    }
    public void drawCircle(Graphics cg, int xCenter, int yCenter, int r) {
        cg.drawOval(xCenter-r, yCenter-r, 2*r, 2*r);
    }
    public double findAngle(Dimension dimension1,Dimension dimension2){
        double x = dimension2.width - dimension1.width;
        double y = dimension2.height - dimension1.height;
        return Math.atan2(y,x);
    }
    public void drawName(Graphics g,Dimension dimension1,Dimension dimension2){
        g.drawString(elementName,dimension1.width - 30,dimension2.height - 30);
    }
    public Dimension nextDim(Dimension dimension,double length, double angle){
        int x = (int)(dimension.width + length*Math.cos(angle));
        int y = (int)(dimension.height + length*Math.sin(angle));
        Dimension fDim = new Dimension(x,y);
        return fDim;
    }
    public void drawPolygon(Graphics g, Dimension dimension1, Dimension dimension2) {
        int length = (int) (Element.ELEMENT_LENGTH * 2 / Math.sqrt(3));
        int angle = (int)findAngle(dimension1,dimension2);
        Dimension nextDim = drawLine(g,dimension1,length,angle + Math.PI/6);
        drawLine(g,nextDim,length,angle - Math.PI/6);
        nextDim = drawLine(g,dimension1,length,angle - Math.PI/6);
        drawLine(g,nextDim,length,angle + Math.PI/6);
    }
}
