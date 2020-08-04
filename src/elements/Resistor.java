package elements;

import connections.Node;

import java.awt.*;

public class Resistor extends Element {
    //We Create a Sample used For Debug
    public static Resistor SAMPLE = new Resistor("R1",10);
    public static double R_LENGTH = Element.ELEMENT_LENGTH/8;
    private double resistant;

    public Resistor(String elementName,double resistant){
        this.elementName = elementName;
        this.resistant = resistant;
    }
    @Override
    public void update() {
        this.v = this.nodeP.getV() - this.nodeN.getV();
        this.i = this.v / this.resistant;
    }

    @Override
    public double getCurrentEnteringNode(Node node) {
        if (node.equals(nodeP)){
            return -i;
        }
        else {
            return +i;
        }
    }

    @Override
    public int getElementType() {
        return Element.RESISTOR;
    }

    @Override
    public double increaseNodeVoltage(Node connectedNode) {
        return 0;
    }

    @Override
    public void drawElement(Graphics g, Dimension dimension1, Dimension dimension2) {
        Dimension thisDim;
        double angle = findAngle(dimension1,dimension2);
        double PI = Math.PI;
        thisDim = dimension1;
        for(int i=0;i<4;i++) {
            thisDim = new Dimension((int)Math.round(dimension1.width + i*Element.ELEMENT_LENGTH/4 * Math.cos(angle)),(int)Math.round(dimension1.height + i*Element.ELEMENT_LENGTH/4 * Math.sin(angle)));
            thisDim = drawLine(g, thisDim, R_LENGTH, angle + PI / 3);
            thisDim = drawLine(g, thisDim, R_LENGTH, angle - PI / 3);
            thisDim = drawLine(g, thisDim, R_LENGTH, angle - PI / 3);
            thisDim = drawLine(g, thisDim, R_LENGTH, angle + PI / 3);
        }
        drawName(g,dimension1,dimension2);
        //thisDim = drawLine(g,thisDim,R_LENGTH,angle + PI/3);
        //thisDim = drawLine(g,thisDim,R_LENGTH,angle - PI/3);

    }

}
