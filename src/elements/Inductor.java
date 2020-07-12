package elements;


import base.Database;
import connections.Node;

public class Inductor extends Element{
    private double inductance;
    public Inductor(String elementName,double inductance){
        this.elementName = elementName;
        this.inductance = inductance;
    }
    @Override
    public void update() {
        this.v = this.nodeP.getV() - this.nodeN.getV();
        this.i = this.iPre + this.v * Database.getDeltaT()/this.inductance;
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
        return Element.INDUCTOR;
    }

    @Override
    public double increaseNodeVoltage(Node connectedNode) {
        return 0;
    }
}
