package ui.circuitDrawer.drawers;

import elements.Element;
import ui.circuitDrawer.CircuitDrawer;

import javax.swing.*;
import java.awt.*;

public class Drawer extends JPanel {
    public static final Color COLOR = Color.black;
    public static final int FILLED_DOT_RADIUS = 3;


    public static void drawElement(Element element,Graphics g,Dimension dimension1, Dimension dimension2) {
        element.drawElement(g, dimension1,dimension2);
    }
    public static void drawLine(Graphics g, Dimension dimension1,Dimension dimension2){
        g.drawLine(dimension1.width,dimension1.height,dimension2.width,dimension2.height);
    }
    public static void drawFilledDot(Graphics g, Dimension dimension, int radius){
        int x = dimension.width - radius;
        int y = dimension.height - radius;
        g.fillOval(x,y,2 * radius, 2 * radius);
    }
    public static void drawFilledDot(Graphics g, Dimension dimension){
        int x = dimension.width - FILLED_DOT_RADIUS;
        int y = dimension.height - FILLED_DOT_RADIUS;
        g.fillOval(x,y,2*FILLED_DOT_RADIUS,2*FILLED_DOT_RADIUS);
    }

}
