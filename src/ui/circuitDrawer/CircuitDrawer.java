package ui.circuitDrawer;

import base.Database;
import connections.Node;
import ui.circuitDrawer.drawers.DrawPanel;
import ui.mainPage.MainScreen;

import javax.swing.*;
import java.awt.*;

public class CircuitDrawer extends JFrame {
    public static int width = 800;
    public static int height = 750;

    public int maxX = 0 ;
    public int maxY = 0;

    DrawPanel drawPanel;
    public CircuitDrawer(){
        this.setTitle("Circuit");
        this.findBounds();
        this.setBounds(MainScreen.SCREEN_WIDTH/2 - width/2,(MainScreen.SCREEN_HEIGHT - height)/2,width,height);
        this.setMinimumSize(new Dimension(MainScreen.SCREEN_WIDTH/2 - width/2,(MainScreen.SCREEN_HEIGHT - height)/2));
        this.setMaximumSize(new Dimension(MainScreen.SCREEN_WIDTH/2 - width/2,(MainScreen.SCREEN_HEIGHT - height)/2));
        drawPanel = new DrawPanel();
        this.add(drawPanel);
        //drawElement(Resistor.SAMPLE,new Dimension(10,10),new Dimension(200,200));
        this.setVisible(true);
    }

    private void findBounds() {
        for(Node node : Database.getInstance().getNodeList()){
            int num = Integer.parseInt(node.getNodeName());
            if ((num - 1)%6 > maxX){
                maxX = (num - 1)%6;
            }
            if ((num - 1)/6 + 1 > maxY){
                maxY = (num - 1)/6 + 1;
            }
        }
        width = 2 * DrawPanel.extraWidth + DrawPanel.horizontalSpace * maxX;
        height = 2 * DrawPanel.extraHeight + DrawPanel.verticalSpace * maxY + 50;

    }

}
