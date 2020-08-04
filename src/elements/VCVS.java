package elements;

import base.Database;
import connections.Node;

import java.awt.*;
import java.io.IOException;

public class VCVS extends Element{
    private Node positiveController;
    private Node negativeController;
    private double controllerVoltage;
    private double ratio;

    public VCVS(String elementName, String positiveControllerName, String negativeControllerName, double ratio) throws IOException {
        this.positiveController = Database.getInstance().findNode(positiveControllerName);
        this.negativeController = Database.getInstance().findNode(negativeControllerName);
        this.ratio = ratio;
        this.elementName = elementName;
    }

    @Override
    public void update() {
        this.controllerVoltage = positiveController.getV() - negativeController.getV();
        this.v = this.controllerVoltage * this.ratio;
    }

    @Override
    public double getCurrentEnteringNode(Node node){
        if (node.equals(nodeP)){
            return -i;
        }
        else {
            return +i;
        }
    }

    @Override
    public int getElementType() {
        return Element.VCVS;
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
