package base;

import connections.Node;
import connections.Union;
import elements.Capacitor;
import elements.CurrentSource;
import elements.Element;
import elements.Resistor;

import java.util.*;

public class Database {
    private static Database database = new Database();
    public static Database getInstance(){
        return database;
    }
    private Double deltaV;
    private Double deltaI;
    private Double deltaT;
    private Double t = 0.0;
    private Double t0;
    List<Node> nodeList = new ArrayList<>();
    List<Union> unionList = new ArrayList<>();
    List<Element> elementList = new ArrayList<>();
    Map<String,Node> nodeMap = new HashMap<>();
    LinkedList<Node> visitList = new LinkedList<>();
    public Node earthNode;

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
    public static double t(){
        return database.t;
    }


    //Initial


    public static void createElement(String input){
        if (input.startsWith("R")){
            System.out.println("Element is a Resistor");
            createResistor(input);
        }
        else if(input.startsWith("I")){
            System.out.println(("Element is a Current Source"));
            createCurrentSource(input);
        }
        else if(input.startsWith("C")){
            System.out.println("Element is a Capacitor");
            createCapacitor(input);
        }
    }
    public static void solver(double deltaT,double deltaV, double deltaI, double t0){
        database.deltaT = deltaT;
        database.deltaV = deltaV;
        database.deltaI = deltaI;
        database.t0 = t0;
        addElementsToNodes();
        createNodeGraph();
        //printNodes();
        database.printNodesElements();
        database.printElementsNodes();
        createUnionGraph();
        database.printUnionNodes();
        database.solveCircuit();
    }
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
        createEarthNode();
        database.visitList.add(database.earthNode);
        database.earthNode.makeAdded();
        int i=0;
        while(i<database.visitList.size()){
            Node node = database.visitList.get(i);
            //System.out.println("we are in " + node.getNodeName());
            Database.findUnionsForNode(node);
            i++;
        }
    }
    public void solveCircuit(){
        while (t<=t0){
            t = t + deltaT;
            int counter = 0;
            for (Union union : database.unionList){
                //System.out.println("Union counter : " + counter );
                union.updateVoltage(deltaV,deltaI);
                //System.out.println("Union " + union.getUnionName() + " Update:");
                //printInfo();
                counter++;
            }
            //database.printNodesElements();
            updateElements();
            //printInfo();
            //System.out.println("\n\n");
        }
    }


    //Extras


    private void updateElements(){
        for(Element element : elementList){
            element.update();
            element.nextStep();
        }
    }
    private static void findUnionsForNode(Node node) {
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
    public static void createEarthNode(){
        for(Node node : database.nodeList){
            if (node.getNodeName().equals("0")){
                database.earthNode = node;
                System.out.println("Earth Node Found!");
            }
        }
    }
    private static void addUnion(Union union){
        if (!database.unionList.contains(union)){
            database.unionList.add(union);
        }
    }


    //Info


    public void printInfo(){
        for(Node node : nodeList){
            System.out.println(node.getNodeName() + "   " + node.getV());
        }
    }
    public void printUnionNodes(){
        for(Union union : unionList){
            union.printUnionNodes();
        }
    }
    public void printNodesElements(){
        for(Node node : database.nodeList){
            System.out.println("Elements of Node " + node.getNodeName() );
            node.printElementList();
        }
    }
    public void printElementsNodes(){
        for(Element element : database.elementList){
            System.out.println("Nodes of Element " + element.getName() + " : ");
            element.printNodes();
        }
    }
    public static void printNodes(){
        for(Node node : database.nodeList){
            System.out.println(node.getNodeName());
        }
    }


    //Creating Elements


    private static void createResistor(String input){
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 4){
            //ERROR
            return;
        }
        String elementName = inputs[0];
        double resistant = Double.parseDouble(inputs[3]);
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        Node nodeP;
        Node nodeN;
        if (database.nodeMap.containsKey(nodeNName)){
            nodeN = database.nodeMap.get(nodeNName);
        }
        else {
            nodeN = new Node(nodeNName);
            database.nodeMap.put(nodeNName,nodeN);
            database.nodeList.add(nodeN);
        }
        if(database.nodeMap.containsKey(nodePName)){
            nodeP = database.nodeMap.get(nodePName);
        }
        else{
            nodeP = new Node(nodePName);
            database.nodeMap.put(nodePName,nodeP);
            database.nodeList.add(nodeP);
        }
        Element element = new Resistor(elementName,resistant);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);

    }
    private static void createCurrentSource(String input) {
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 7){
            //ERROR
            return;
        }

        String elementName = inputs[0];
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        Node nodeP;
        Node nodeN;
        if (database.nodeMap.containsKey(nodeNName)){
            nodeN = database.nodeMap.get(nodeNName);
        }
        else {
            nodeN = new Node(nodeNName);
            database.nodeMap.put(nodeNName,nodeN);
            database.nodeList.add(nodeN);
        }
        if(database.nodeMap.containsKey(nodePName)){
            nodeP = database.nodeMap.get(nodePName);
        }
        else{
            nodeP = new Node(nodePName);
            database.nodeMap.put(nodePName,nodeP);
            database.nodeList.add(nodeP);
        }
        double iOff = Double.parseDouble(inputs[3]);
        double iSin = Double.parseDouble(inputs[4]);
        double freq = Double.parseDouble(inputs[5]);
        double phi = Double.parseDouble(inputs[6]);
        Element element = new CurrentSource(elementName,iOff,iSin,freq,phi);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);

    }
    private static void createCapacitor(String input){
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 4){
            //ERROR
            return;
        }
        String elementName = inputs[0];
        double capacity = Double.parseDouble(inputs[3]);
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        Node nodeP;
        Node nodeN;
        if (database.nodeMap.containsKey(nodeNName)){
            nodeN = database.nodeMap.get(nodeNName);
        }
        else {
            nodeN = new Node(nodeNName);
            database.nodeMap.put(nodeNName,nodeN);
            database.nodeList.add(nodeN);
        }
        if(database.nodeMap.containsKey(nodePName)){
            nodeP = database.nodeMap.get(nodePName);
        }
        else{
            nodeP = new Node(nodePName);
            database.nodeMap.put(nodePName,nodeP);
            database.nodeList.add(nodeP);
        }
        Element element = new Capacitor(elementName,capacity);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);
    }

}
