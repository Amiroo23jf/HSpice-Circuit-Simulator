package elements;

import base.Database;
import connections.Node;

public class VoltageSource extends Element {
    private double vOffset;
    private double vSin;
    private double freq;
    private double phi;

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
}
