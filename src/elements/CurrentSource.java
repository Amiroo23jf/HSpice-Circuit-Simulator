package elements;

import base.Database;
import connections.Node;

public class CurrentSource extends Element {
    private double iOffset;
    private double iSin;
    private double freq;
    private double phi;


    public CurrentSource(String elementName,double iOffset, double iSin, double freq , double phi){
        this.elementName = elementName;
        this.iOffset = iOffset;
        this.iSin = iSin;
        this.freq = freq;
        this.phi = phi;
    }
    @Override
    public void update() {
        this.v = this.nodeP.getV() - this.nodeN.getV();
        this.i = this.iOffset + (this.iSin * Math.sin((2 * Math.PI * freq * Database.t()) + (2 * Math.PI * freq * Database.getDeltaT()) + phi));
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
        return Element.CURRENT_SOURCE;
    }

    @Override
    public double increaseNodeVoltage(Node connectedNode) {
        return 0;
    }
}
