package ui.circuitDrawer.drawers;

import base.Database;
import connections.Node;
import elements.*;
import ui.circuitDrawer.CircuitDrawer;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {
    public static int width = CircuitDrawer.width;
    public static int height = CircuitDrawer.height - 50;
    public static int extraWidth = 100;
    public static int extraHeight = 50;
    public static int horizontalSpace = 100;
    public static int verticalSpace = 100;


    public int zeroCounter = 0;
    public void paint(Graphics g){
        reset();
        drawAllNodes(g);
        drawAllElements(g);
    }


    private void drawAllNodes(Graphics g) {
        for(Node node : Database.getInstance().getNodeList()){
            if (!node.getNodeName().equals("0")) {
                Drawer.drawFilledDot(g, getNodesDimension(Integer.parseInt(node.getNodeName())));
            }
        }
        Drawer.drawLine(g,getAbsoluteDimension(0,0),getAbsoluteDimension(width - 2 * extraWidth,0));
        drawEarth(g);
    }
    public Dimension getNodesDimension(int node){

        int j = (node - 1)/6 + 1;
        int i = (node - 1)%6;
        Dimension dimension = getAbsoluteDimension(i * horizontalSpace,j * verticalSpace);
        return dimension;
    }
    public Dimension[] findNodesDimension(int nodeP, int nodeN){
        Dimension[] dimensions = new Dimension[2];
        Dimension nodePDim = null;
        Dimension nodeNDim = null;
        if(nodeP == 0){
            nodePDim = zeroNodesDimension(nodeN);
            nodeNDim = getNodesDimension(nodeN);
        }
        else if(nodeN == 0){
            nodePDim = getNodesDimension(nodeP);
            nodeNDim = zeroNodesDimension(nodeP);
        }
        else{
            nodePDim = getNodesDimension(nodeP);
            nodeNDim = getNodesDimension(nodeN);
        }
        dimensions[0] = nodePDim;
        dimensions[1] = nodeNDim;
        return dimensions;
    }
    private Dimension zeroNodesDimension(int node) {
        int x = ((node - 1)%6) * horizontalSpace;
        int y = 0;
        Dimension dimension = getAbsoluteDimension(x,y);
        return dimension;
    }
    private Dimension getAbsoluteDimension(int x, int y){
        return new Dimension( x +  extraWidth, height - y - extraHeight);

    }
    private void reset(){
        width = CircuitDrawer.width;
        height = CircuitDrawer.height - 50;
        zeroCounter = 0;
    }
    private void drawAllElements(Graphics g){
        for(Element element : Database.getInstance().getElementList()){
            int nodeP = element.getNodeP();
            int nodeN = element.getNodeN();
            Dimension nodePDim = findNodesDimension(nodeP,nodeN)[0];
            Dimension nodeNDim = findNodesDimension(nodeP,nodeN)[1];
            drawElement(g,element,nodePDim,nodeNDim);

        }
    }
    private void drawElement(Graphics g,Element element, Dimension dimension1, Dimension dimension2){
        //dimension1 is positive
        //dimension2 is negative
        double ratio = this.calculateLinesRatio(dimension1, dimension2);
        Dimension pNodeDimension = new Dimension((int)(dimension1.width + (dimension2.width - dimension1.width)*ratio), (int)(dimension1.height + ((dimension2.height - dimension1.height) * ratio)));
        Dimension nNodeDimension = new Dimension((int)(dimension2.width + (dimension1.width - dimension2.width)*ratio), (int)(dimension2.height + ((dimension1.height - dimension2.height) * ratio)));
        Drawer.drawLine(g,dimension1,pNodeDimension);
        Drawer.drawLine(g,dimension2,nNodeDimension);
        Drawer.drawElement(element,g,pNodeDimension,nNodeDimension);
    }
    private double calculateLinesRatio(Dimension dimension1,Dimension dimension2){
        double length = Math.sqrt(Math.pow(dimension1.width - dimension2.width,2) + Math.pow(dimension1.height - dimension2.height,2));
        double ratio = ((length - Element.ELEMENT_LENGTH)/2)/length;
        return ratio;
    }
    private void drawEarth(Graphics g){
        Dimension zero = getAbsoluteDimension(0,0);
        Dimension one = getAbsoluteDimension(0, -5);
        Dimension oneLeft = getAbsoluteDimension(-10, -5);
        Dimension oneRight = getAbsoluteDimension(+10, -5);
        Dimension twoLeft = getAbsoluteDimension( -6 , -8);
        Dimension twoRight = getAbsoluteDimension(+6 , -8);
        Dimension threeLeft = getAbsoluteDimension(-3,-11);
        Dimension threeRight = getAbsoluteDimension(+3, -11);
        Drawer.drawLine(g,zero,one);
        Drawer.drawLine(g,oneLeft,oneRight);
        Drawer.drawLine(g,twoLeft,twoRight);
        Drawer.drawLine(g,threeLeft,threeRight);

    }
}
