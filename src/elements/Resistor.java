package elements;

import connections.Node;

public class Resistor extends Element {
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

}
