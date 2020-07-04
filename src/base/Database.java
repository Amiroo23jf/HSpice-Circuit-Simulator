package base;

import connections.Node;
import connections.Union;
import elements.Element;

import java.util.*;

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
    List<Element> elementList = new ArrayList<>();
    Map<String,Node> nodeMap = new HashMap<>();
    Queue<Node> visitList = new LinkedList<>();
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
    public static void addElementsToNodes(){
        for(Node node : database.nodeList){
            for (Element element : database.elementList){
                if (element.nodeP.equals(node) || element.nodeN.equals(node)){
                    node.addElement(element);
                }
            }
        }
    }
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
        database.visitList.add(database.earthNode);
        database.earthNode.makeAdded();
        for(Node node : database.visitList){
            Database.findUnionsForNode(node);
        }
    }

    private static void findUnionsForNode(Node node) {
        if (isAllNodesAdded()){
            return;
        }
        for(Node connectedNode : node.connectedNodes){
            if(!connectedNode.isAdded()){
                database.visitList.add(connectedNode);
                connectedNode.makeAdded();
            }
            if(node.elementBetweenIsVoltageSource(connectedNode)){
                connectedNode.union = node.union;
                node.union.addNode(connectedNode);
            }
        }
        addUnion(node.union);

    }
    public static boolean isAllNodesAdded(){
        for(Node node : database.nodeList){
            if (!node.isAdded()){
                return false;
            }
        }
        return true;
    }
    public static void createEarthNode(){
        for(Node node : database.nodeList){
            if (node.getNodeName().equals("0")){
                database.earthNode = node;
            }
        }
    }
    private static void addUnion(Union union){
        if (!database.unionList.contains(union)){
            database.unionList.add(union);
        }
    }

}
