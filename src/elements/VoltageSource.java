package elements;

import base.Database;
import connections.Node;

public class VoltageSource extends Element {
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
        return 0;
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
}
