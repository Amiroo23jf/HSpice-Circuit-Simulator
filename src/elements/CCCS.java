package elements;

import base.Database;
import connections.Node;

import java.awt.*;

public class CCCS extends Element {
    private Element controller;
    private double controllerCurrent;
    private double ratio;

    public CCCS(String elementName, String controllerName, double ratio){
        this.controller = Database.getInstance().findElement(controllerName);
        this.ratio = ratio;
        this.elementName = elementName;
    }
    @Override
    public void update() {
        this.v = this.nodeP.getV() - this.nodeN.getV();
        controllerCurrent = controller.getI();
        this.i = controllerCurrent * ratio;
    }

    @Override
    public double getCurrentEnteringNode(Node node) {
        if(node.equals(nodeP)){
            return +i;
        }
        else{
            return -i;
        }
    }

    @Override
    public int getElementType() {
        return Element.CCCS;
    }

    @Override
    public double increaseNodeVoltage(Node connectedNode) {
        return 0;
    }

    @Override
    public void drawElement(Graphics g, Dimension dimension1, Dimension dimension2) {
        drawName(g,dimension1,dimension2);
        double angle = findAngle(dimension1,dimension2);
        Dimension dim = nextDim(dimension1,ELEMENT_LENGTH/2,angle);
        drawPolygon(g,dimension1,dimension2);
        g.drawLine(dimension1.width,dimension1.height,dimension2.width,dimension2.height);
        drawLine(g,dim,Element.ELEMENT_LENGTH/3,angle + Math.PI/4);
        drawLine(g,dim,Element.ELEMENT_LENGTH/3,angle - Math.PI/4);
    }
}
