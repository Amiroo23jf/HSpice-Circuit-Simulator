package base;

import connections.Node;
import connections.Union;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Database database = new Database();
    public static Database getInstance(){
        return database;
    }
    private Double deltaV;
    private Double deltaI;
    private Double deltaT;
    List<Node> nodeList = new ArrayList<>();
    List<Union> unionList = new ArrayList<>();
    Node earthNode;

    //Constants
    public static double getDeltaT(){
        return database.deltaT;
    }

    public static double getDeltaV(){
        return database.deltaV;
    }

    public static double getDeltaI(){
        return database.deltaI;
    }

    //Initial
    public static void createNodeGraph(){
        for(Node node : database.nodeList){
            for(Node connectedNode : database.nodeList){
                if(node!=connectedNode){
                    if(node.isConnectedToNode(connectedNode)){
                        node.addConnectedNode(connectedNode);
                    }
                }
            }
        }
    }
    public static void createUnionGraph(){

    }
    public static void findEarthNode(){
        for(Node node : database.nodeList){
            if (node.getNodeName().equals("0")){
                database.earthNode = node;
            }
        }
    }

}
