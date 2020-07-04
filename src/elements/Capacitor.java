package elements;

import base.Database;
import connections.Node;

public class Capacitor extends Element{
    private double capacity;

    public Capacitor(String elementName, double capacity){
        this.elementName = elementName;
        this.capacity = capacity;
    }
    @Override
    public void update() {
        this.v = this.nodeP.getV() - this.nodeN.getV();
        this.i = this.capacity * (v - vPre)/ Database.getDeltaT();
    }

    @Override
    public double getCurrentEnteringNode(Node node) {
        if(node.equals(nodeP)){
            return -i;
        }
        else{
            return +i;
        }
    }

    @Override
    public int getElementType() {
        return Element.CAPACITOR;
    }
}
