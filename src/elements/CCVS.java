package elements;

import base.Database;
import connections.Node;

import java.awt.*;

public class CCVS extends Element {
    private Element controller;
    private double controllerCurrent;
    private double ratio;

    public CCVS(String elementName, String controllerName, double ratio) {
        this.controller = Database.getInstance().findElement(controllerName);
        this.ratio = ratio;
        this.elementName = elementName;
    }

    @Override
    public void update() {
        this.controllerCurrent = controller.getI();
        this.v = this.controllerCurrent * ratio;

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
        return Element.CCVS;
    }

    @Override
    public double increaseNodeVoltage(Node connectedNode) {
        if(connectedNode.equals(nodeP)){
            return this.v;
        }
        return -this.v;
    }

    @Override
    public void drawElement(Graphics g, Dimension dimension1, Dimension dimension2) {
        drawName(g,dimension1,dimension2);
        double angle = findAngle(dimension1,dimension2);
        drawPolygon(g,dimension1,dimension2);
        Dimension dimPlus = nextDim(dimension1,Element.ELEMENT_LENGTH/4,angle);
        Dimension dimMinus = nextDim(dimension2, Element.ELEMENT_LENGTH/4*3,angle);
        g.drawString("+",dimPlus.width,dimMinus.height);
        g.drawString("-",dimMinus.width,dimMinus.height);
    }
}
