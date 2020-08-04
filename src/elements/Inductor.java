package elements;


import base.Database;
import connections.Node;

import java.awt.*;

public class Inductor extends Element{
    public static Inductor SAMPLE = new Inductor("L1",0);
    public static double RADIUS = ELEMENT_LENGTH/5;
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

    @Override
    public void drawElement(Graphics g, Dimension dimension1, Dimension dimension2) {
        int x = 0;
        int y = 0;
        double angle = findAngle(dimension1,dimension2);
        for(int i=0;i<4;i++){
            x = (int)(dimension1.width + (1+i) * RADIUS * Math.cos(angle));
            y = (int)(dimension1.height + (1+i) * RADIUS * Math.sin(angle));
            drawCircle(g,x,y,(int)RADIUS);
        }
        drawName(g,dimension1,dimension2);
    }
}
