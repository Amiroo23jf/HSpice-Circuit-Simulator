package elements;

import base.Database;
import connections.Node;

import java.awt.*;

public class VoltageSource extends Element {
    public static final VoltageSource SAMPLE = new VoltageSource("V1",0,0,0,0);

    private double vOffset;
    private double vSin;
    private double freq;
    private double phi;

    public VoltageSource(String elementName, double vOff, double vSin, double freq, double phi) {
        this.elementName = elementName;
        this.vOffset = vOff;
        this.vSin = vSin;
        this.freq = freq;
        this.phi = phi;
    }

    @Override
    public void update() {
        this.v = this.vOffset + this.vSin * Math.sin(2 * Math.PI * freq * (Database.t() + Database.getDeltaT()) + phi);

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
        return Element.VOLTAGE_SOURCE;
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
        Dimension dim = nextDim(dimension1,ELEMENT_LENGTH/2,angle);
        drawCircle(g,dim.width,dim.height,Element.ELEMENT_LENGTH/2);
        Dimension dimPlus = nextDim(dimension1,Element.ELEMENT_LENGTH/4,angle);
        Dimension dimMinus = nextDim(dimension1, Element.ELEMENT_LENGTH/4*3,angle);
        g.drawString("+",dimPlus.width,dimMinus.height);
        g.drawString("-",dimMinus.width,dimMinus.height);


    }


}
