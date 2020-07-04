package elements;

import connections.Node;

public abstract class Element {
    protected double vPre;
    protected double v;
    protected double iPre;
    protected double i;
    public Node nodeP;
    public Node nodeN;
    protected String elementName;

    public abstract void update();
    public abstract double getCurrentEnteringNode(Node node);
}
