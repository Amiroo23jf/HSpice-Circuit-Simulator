package elements;

import connections.Node;

public abstract class Element {
    public final static int RESISTOR = 11;
    public final static int CAPACITOR = 12;
    public final static int INDUCTOR = 13;
    public final static int CURRENT_SOURCE = 1;
    public final static int VOLTAGE_SOURCE = 2;
    public final static int CCCS = 21;
    public final static int VCCS = 22;
    public final static int CCVS = 23;
    public final static int VCVS = 24;

    protected double vPre;
    protected double v;
    protected double iPre;
    protected double i;
    public Node nodeP;
    public Node nodeN;
    protected String elementName;

    public abstract void update();
    public abstract double getCurrentEnteringNode(Node node);
    public abstract int getElementType();

}
