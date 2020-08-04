package elements;

import base.Database;
import connections.Node;

import java.awt.*;

public class Capacitor extends Element{
    public static Capacitor SAMPLE = new Capacitor("C1",0);

    private double capacity;

    public Capacitor(String elementName, double capacity){
        this.elementName = elementName;
        this.capacity = capacity;
    }
    @Override
    public void update() {
        this.v = this.nodeP.getV() - this.nodeN.getV();
        this.i = this.capacity * (v - vPre)/ Database.getDeltaT();
    }

    @Override
    public double getCurrentEnteringNode(Node node) {
        if(node.equals(nodeP)){
            return -i;
        }
        else{
            return +i;
        }
    }

    @Override
    public int getElementType() {
        return Element.CAPACITOR;
    }

    @Override
    public double increaseNodeVoltage(Node connectedNode) {
        return 0;
    }

    @Override
    public void drawElement(Graphics g, Dimension dimension1, Dimension dimension2) {
        double angle = findAngle(dimension1,dimension2);
        Dimension first = drawLine(g,dimension1,Element.ELEMENT_LENGTH/3,angle);
        Dimension second = drawLine(g,dimension2,Element.ELEMENT_LENGTH/3,angle + Math.PI);
        drawLine(g,first,Element.ELEMENT_LENGTH/3,angle + Math.PI/2);
        drawLine(g,first,Element.ELEMENT_LENGTH/3,angle - Math.PI/2);
        drawLine(g,second,Element.ELEMENT_LENGTH/3,angle + Math.PI/2);
        drawLine(g,second,Element.ELEMENT_LENGTH/3,angle - Math.PI/2);
        drawName(g,dimension1,dimension2);


    }
}
