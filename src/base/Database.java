package base;

import extra.Errors;
import extra.NumberConverter;
import ui.circuitDrawer.CircuitDrawer;
import ui.mainPage.MainScreen;
import connections.Node;
import connections.Union;
import elements.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    //Lists

    List<Node> nodeList = new ArrayList<>();
    List<Union> unionList = new ArrayList<>();
    List<Element> elementList = new ArrayList<>();
    Map<String,Node> nodeMap = new HashMap<>();
    LinkedList<Node> visitList = new LinkedList<>();
    List<String> inputList = new ArrayList<>();

    public Node earthNode;

    //Src and Files
    public String savingSrc;
    public FileWriter writer;
    private String codeSrc;
    private String logSrc;
    private String resultSrc;
    private String resultFolderName;
    private int logTabs = 0;
    private int resultTabs = 0;
    private MainScreen mainScreen;
    public CircuitDrawer circuitDrawer;
    private boolean fileLoaded = false;
    private boolean fileRun = false;
    private boolean circuitSolved = false;

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




    //Run
    public void run(Scanner scanner) throws IOException {
        mainScreen = new MainScreen();
        //input(scanner);
    }
    public void updateInputFile() {
        String inputText = mainScreen.getEditorTextList();
        try {
            Writer inputWriter = new FileWriter(codeSrc);
            inputWriter.write(inputText);
            inputWriter.close();
        } catch (IOException e) {
            Errors.fileNotFound();
        }
        this.updateInputList();
    }

    private void updateInputList() {
        Path path = Paths.get(codeSrc);
        try {
            inputList = Files.readAllLines(path);
            fileLoaded = true;
        }
        catch (IOException e) {
            Errors.fileNotFound();
        }
    }

    public void runCode() throws IOException {
        //saving src
        Database.getInstance().fileRun = true;
        Database.getInstance().setSavingSrc(codeSrc);
        Database.getInstance().makeSavingDir();
        //inputs
        log("Inputs...");
        increaseLogTab();
        for (String input : inputList){
            if(!Errors.getTranFlag()){
                Errors.line++;
                Database.readLine(input);
            }
            else{
                if(!input.equals("\n")) {
                    System.err.println("Input which made the error is : " + input);
                    Errors.errorOne();
                }
            }
        }
        log("diFlag : "+Errors.getDiFlag());
        log("dvFlag : "+Errors.getDvFlag());
        log("dtFlag : "+Errors.getDtFlag());
        log("tranFlag : "+Errors.getTranFlag());

        if(!(Errors.getDiFlag() && Errors.getDtFlag() && Errors.getDvFlag() && Errors.getTranFlag())){
            Errors.errorOne();
        }
        decreaseLogTabs();
        Database.getInstance().__initialize__();
        log("Solving...");
        Database.solver();
        log("Creating Final Results...");
        Database.getInstance().finalResults();
    }
    public void setSavingSrc(String string ){
        String[] strings = string.split("/");
        savingSrc = string.substring(0,string.length() - (strings[strings.length - 1].length() + 1)) + "/" + resultFolderName;
        logSrc = savingSrc + "/log.txt";
        resultSrc = savingSrc + "/result.txt";
    }
    public void makeSavingDir() throws IOException {
        File file = new File(savingSrc + "/Elements");
        file.mkdirs();
        file = new File(savingSrc + "/Nodes");
        file.mkdirs();
        makingFile(logSrc);
        makingFile(resultSrc);

    }
    public void makingFile(String address) throws IOException {
        writer = new FileWriter(address);
        writer.write("");
        writer.close();
    }

    //Initialization


    public void __initialize__() {
        database.log("\n\nInitialization...");
        log("    Adding Elements To Nodes...");
        addElementsToNodes();
        log("    Creating Node Graph...");
        createNodeGraph();
        log("    Creating Union Graph...");
        createUnionGraph();
        log("\n\nPre-Calculation Results...");
        log("    Nodes:");
        logNodesElements();
        log("\n\n    Elements:");
        logElementsNodes();
        log("\n\n    Unions:");
        logUnionNodes();
    }
        //Adding elements to nodes
    public static void addElementsToNodes(){
        for(Node node : database.nodeList){
            for (Element element : database.elementList){
                if (element.nodeP.equals(node) || element.nodeN.equals(node)){
                    node.addElement(element);
                }
            }
        }
    }
        //creators
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
    public static void createEarthNode(){
        for(Node node : database.nodeList){
            if (node.getNodeName().equals("0")){
                database.earthNode = node;
                database.log("    Earth Node Found!");
                return;
            }
        }
        Errors.earthNodeNotFound();
    }
        // Creating union graph
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
    private static void addUnion(Union union){
        if (!database.unionList.contains(union)){
            database.unionList.add(union);
        }
    }
        //Command line processor
    public static void readLine(String input) throws IOException {
        database.log("input : " + input);
        if (input.startsWith("R")){
            database.log("    Element is a Resistor");
            createResistor(input);
        }
        else if(input.startsWith("I")){
            database.log("    Element is a Current Source");
            createCurrentSource(input);
        }
        else if(input.startsWith("C")){
            database.log("    Element is a Capacitor");
            createCapacitor(input);
        }
        else if(input.startsWith("L")){
            database.log("    Element is an Inductor");
            createInductor(input);
        }
        else if(input.startsWith("V")){
            database.log("    Element is a Voltage Source");
            createVoltageSource(input);
        }
        else if(input.startsWith("F")){
            database.log("    Element is a CCCS");
            createCCCS(input);
        }
        else if(input.startsWith("G")){
            database.log("    Element is a VCCS");
            createVCCS(input);
        }
        else if(input.startsWith("H")){
            database.log("    Element is a CCVS");
            createCCVS(input);
        }
        else if(input.startsWith("E")){
            database.log("    Element is a VCVS");
            createVCVS(input);
        }
        else if(input.matches("((\\s*)(\\*)(.*))")){
            //This input is Comment
            database.increaseLogTab();
            database.log("This line is comment");
            database.decreaseLogTabs();
        }
        else if(input.startsWith("dV")){
            database.log("    Setting dV..");
            setDeltaV(input);
        }
        else if(input.startsWith("dI")){
            database.log("    Setting dI..");
            setDeltaI(input);
        }
        else if(input.startsWith("dT")){
            database.log("    Setting dT..");
            setDeltaT(input);
        }
        else if(input.startsWith(".tran")){
            database.log("    Setting Simulation Time...");
            setTran(input);
        }
        else{
            Errors.wrongInputError();
        }
    }

    //Solve

    public static void solver() throws IOException {
        database.solveCircuit();
    }
    public void solveCircuit() throws IOException {
        increaseLogTab();
        while (t<=t0){
            //log("Currently Solving Time : " + t + " Seconds");
            t = t + deltaT;
            increaseLogTab();
            resetVSCurrents();
            for(int i=0;i<1000;i++) {
                //log("Currently Solving Step : " + (i+1) );
                increaseLogTab();
                for (Union union : database.unionList) {
                    //log("Updating Union : " + union.getUnionName());
                    //System.out.println("Union counter : " + counter );
                    union.updateVoltage(deltaV, deltaI);
                    //System.out.println("Union " + union.getUnionName() + " Update:");
                    //printInfo();
                }
                decreaseLogTabs();
                //database.printNodesElements();
                updateNodesVoltagesInUnions();
                updateElementsInSameTime();
            }
            decreaseLogTabs();
            updateElementsInNextTime();
            Errors.checkErrorTwo();
            findVSCurrents();
            updateInfoFiles();
            //printInfo();
            //System.out.println("\n\n");
        }
        decreaseLogTabs();
    }

    private boolean isKclEstablished() {
        for(Union union : unionList){
            if (!union.isKclEstablished()){
                return false;
            }
        }
        return true;
    }

    //Voltage Source Currents
    private void findVSCurrents() {
        for(Union union : unionList){
            union.findVSCurrents();
        }
    }
    private void resetVSCurrents() {
        for(Union union : unionList){
            union.resetVSCurrents();
        }
    }
        //Update
    private void updateNodesVoltagesInUnions(){
        for (Union union : this.unionList){
            //log("Union name :" + union.getUnionName());
            union.updateNodesVoltage();
        }
    }
    private void updateElementsInSameTime(){
        for(Element element : elementList){
            element.update();
        }
    }
    private void updateElementsInNextTime(){
        for(Element element : elementList){
            element.update();
            element.nextStep();
        }
    }

    //Results


    public void printNodesResults() {
        result("Nodes:");
        increaseResultTabs();
        for(Node node : nodeList){
            result(node.getNodeName() + " : " + node.getV());
        }
        decreaseResultTabs();
    }
    public void printElementsResults(){
        result("Elements:");
        increaseResultTabs();
        for(Element element : elementList){
            result(element.getName() + " : " + element.nodeP.getNodeName() + " to " + element.nodeN.getNodeName());
            increaseResultTabs();
            if(!Element.isVoltageSource(element)){
                result("Voltage : "+ element.getV());
                result("Current : "+ element.getI());
            }
            else{
                result("Voltage : "+ element.getV());
                result("Current : "+ element.getI());
            }
            decreaseResultTabs();
        }
    }
    public void finalResults(){
        printNodesResults();
        result("__________");
        printElementsResults();
        addFinalGraphicalResults();
        circuitSolved = true;
    }

    public void addFinalGraphicalResults(){
        try {
            mainScreen.logResults(Files.readString(Paths.get(resultSrc)));
        } catch (IOException e) {
            Errors.fileNotFound();
        }
    }
    public void result(String string){
        try {
            writer = new FileWriter(resultSrc, true);
            writer.append(resultTabs() + string + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String resultTabs() {
        String string = "";
        for (int i=0;i<resultTabs ; i++){
            string = string + "    ";
        }
        return string;
    }
    public void increaseResultTabs(){
        resultTabs = resultTabs + 1;
    }
    public void decreaseResultTabs(){
        resultTabs = resultTabs - 1;
    }

    //Log

    public void log(String string) {
        try {
            writer = new FileWriter(logSrc,true);
            String text = logTabs() + string +"\n";
            writer.append( logTabs()+ string + "\n");
            System.out.println(logTabs()+ string + "\n");
            writer.close();
        } catch (IOException e) {
            Errors.fileNotFound();
        }
    }

    public void logUnionNodes(){
        for(Union union : unionList){
            union.printUnionNodes();
        }
    }
    public void logNodesElements(){
        for(Node node : database.nodeList){
            log("        Elements of Node " + node.getNodeName() );
            node.printElementList();
        }
    }
    public void logElementsNodes(){
        for(Element element : database.elementList){
            log("        Nodes of Element " + element.getName() + " : ");
            element.printNodes();
        }
    }
    public void increaseLogTab(){
        logTabs = logTabs + 1;
    }
    public void decreaseLogTabs(){
        logTabs = logTabs - 1;
    }
    public void resetLogTabs() {logTabs = 0;}
    public String logTabs(){
        String string = "";
        for (int i=0 ; i < logTabs ; i++){
            string = string + "    ";
        }
        return string;
    }

    //File Updater

    public void updateInfoFiles() throws IOException {
        updateElementsInfoFiles();
        updateNodeInfoFiles();
    }
    private void updateNodeInfoFiles() throws IOException{
        for(Node node : nodeList){
            node.updateInfoFile();
        }
    }
    private void updateElementsInfoFiles() throws IOException {
        for(Element element : elementList){
            element.updateInfoFile();
        }
    }


    //Finder

    public Element findElement(String elementName){
        for (Element element : this.elementList){
            if (elementName.equals(element.getName())){
                return element;
            }
        }
        return null;
    }
    public Node findNode(String nodeName) throws IOException {
        Node node = null;
        Errors.nodeNameError(nodeName);
        if (database.nodeMap.containsKey(nodeName)){
            node = database.nodeMap.get(nodeName);
        }
        else {
            node = new Node(nodeName);
            database.nodeMap.put(nodeName,node);
            database.nodeList.add(node);
        }
        return node;
    }


    //Creating Elements and Inputs

    private static void setDeltaT(String input) {
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 2){
            Errors.wrongInputError();
            return;
        }
        else if(!inputs[0].equals("dT")){
            Errors.wrongInputError();
            return;
        }
        database.deltaT = NumberConverter.convert(inputs[1]);
        Errors.setDtFlag();
        database.log(Double.toString(database.deltaT));

    }
    private static void setDeltaV(String input) {
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 2){
            Errors.wrongInputError();
            return;
        }
        else if(!inputs[0].equals("dV")){
            Errors.wrongInputError();
            return;
        }
        database.deltaV = NumberConverter.convert(inputs[1]);
        Errors.setDvFlag();
        database.log(Double.toString(database.deltaV));
    }
    private static void setDeltaI(String input) {
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 2){
            Errors.wrongInputError();
            return;
        }
        else if(!inputs[0].equals("dI")){
            Errors.wrongInputError();
            return;
        }
        database.deltaI = NumberConverter.convert(inputs[1]);
        Errors.setDiFlag();
        database.log(Double.toString(database.deltaI));
    }
    private static void setTran(String input) {
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 2){
            Errors.wrongInputError();
            return;
        }
        else if(!inputs[0].equals(".tran")){
            Errors.wrongInputError();
            return;
        }
        database.t0 = NumberConverter.convert(inputs[1]);
        Errors.setTranFlag();
        database.log(Double.toString(database.t0));

    }
    private static void createResistor(String input) throws IOException {
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 4){
            Errors.wrongInputError();
            return;
        }
        String elementName = inputs[0];
        double resistant = NumberConverter.convert(inputs[3]);
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        Node nodeP;
        Node nodeN;
        nodeN = database.findNode(nodeNName);
        nodeP = database.findNode(nodePName);
        Element element = new Resistor(elementName,resistant);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);
        element.makeNewInfoFile();

    }
    private static void createCurrentSource(String input) throws IOException {
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 7){
            Errors.wrongInputError();
            return;
        }

        String elementName = inputs[0];
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        Node nodeP;
        Node nodeN;
        nodeN = database.findNode(nodeNName);
        nodeP = database.findNode(nodePName);
        double iOff = NumberConverter.convert(inputs[3]);
        double iSin = NumberConverter.convert(inputs[4]);
        double freq = NumberConverter.convert(inputs[5]);
        double phi = NumberConverter.convert(inputs[6]);
        Element element = new CurrentSource(elementName,iOff,iSin,freq,phi);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);
        element.makeNewInfoFile();
    }
    private static void createVoltageSource(String input) throws IOException {
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 7){
            Errors.wrongInputError();
            return;
        }

        String elementName = inputs[0];
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        Node nodeP;
        Node nodeN;
        nodeN = database.findNode(nodeNName);
        nodeP = database.findNode(nodePName);
        double vOff = NumberConverter.convert(inputs[3]);
        double vSin = NumberConverter.convert(inputs[4]);
        double freq = NumberConverter.convert(inputs[5]);
        double phi = NumberConverter.convert(inputs[6]);
        Element element = new VoltageSource(elementName,vOff,vSin,freq,phi);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);
        element.makeNewInfoFile();
    }
    private static void createCapacitor(String input) throws IOException {
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 4){
            Errors.wrongInputError();
            return;
        }
        String elementName = inputs[0];
        double capacity = NumberConverter.convert(inputs[3]);
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        Node nodeP;
        Node nodeN;
        nodeN = database.findNode(nodeNName);
        nodeP = database.findNode(nodePName);
        Element element = new Capacitor(elementName,capacity);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);
        element.makeNewInfoFile();
    }
    private static void createInductor(String input) throws IOException {
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 4){
            Errors.wrongInputError();
            return;
        }
        String elementName = inputs[0];
        double inductance = NumberConverter.convert(inputs[3]);
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        Node nodeP;
        Node nodeN;
        nodeN = database.findNode(nodeNName);
        nodeP = database.findNode(nodePName);
        Element element = new Inductor(elementName,inductance);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);
        element.makeNewInfoFile();
    }
    private static void createCCCS(String input) throws IOException {
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 5){
            Errors.wrongInputError();
            return;
        }
        String elementName = inputs[0];
        String controllerName = inputs[3];
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        double ratio = NumberConverter.convert(inputs[4]);
        Node nodeP;
        Node nodeN;
        //Updating the node map Starts
        nodeN = database.findNode(nodeNName);
        nodeP = database.findNode(nodePName);
        //Updating the node map Ends
        Element element = new CCCS(elementName,controllerName,ratio);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);
        element.makeNewInfoFile();
    }
    private static void createVCCS(String input) throws IOException{
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 6){
            Errors.wrongInputError();
            return;
        }
        String elementName = inputs[0];
        String positiveControllerName = inputs[3];
        String negativeControllerName = inputs[4];
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        double ratio = NumberConverter.convert(inputs[5]);
        Node nodeP;
        Node nodeN;
        //Updating the node map Starts
        nodeN = database.findNode(nodeNName);
        nodeP = database.findNode(nodePName);
        //Updating the node map Ends
        Element element = new VCCS(elementName,positiveControllerName,negativeControllerName,ratio);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);
        element.makeNewInfoFile();
    }
    private static void createCCVS(String input) throws IOException{
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 5){
            Errors.wrongInputError();
            return;
        }
        String elementName = inputs[0];
        String controllerName = inputs[3];
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        double ratio = NumberConverter.convert(inputs[4]);
        Node nodeP;
        Node nodeN;
        //Updating the node map Starts
        nodeN = database.findNode(nodeNName);
        nodeP = database.findNode(nodePName);
        //Updating the node map Ends
        Element element = new CCVS(elementName,controllerName,ratio);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);
        element.makeNewInfoFile();
    }
    private static void createVCVS(String input) throws IOException{
        String[] inputs = input.split("\\s+");
        if(inputs.length!= 6){
            Errors.wrongInputError();
            return;
        }
        String elementName = inputs[0];
        String positiveControllerName = inputs[3];
        String negativeControllerName = inputs[4];
        String nodePName = inputs[1];
        String nodeNName = inputs[2];
        double ratio = NumberConverter.convert(inputs[5]);
        Node nodeP;
        Node nodeN;
        //Updating the node map Starts
        nodeN = database.findNode(nodeNName);
        nodeP = database.findNode(nodePName);
        //Updating the node map Ends
        Element element = new VCVS(elementName,positiveControllerName,negativeControllerName,ratio);
        element.setNodes(nodeP,nodeN);
        database.elementList.add(element);
        element.makeNewInfoFile();
    }


    //Extras

    public static void printNodes(){
        for(Node node : database.nodeList){
            System.out.println(node.getNodeName());
        }
    }

    //Errors

    public boolean isErrorTwo() {
        for(Union union : unionList){
            if(union.isErrorTwo()){
                return true;
            }
        }
        return false;
    }

    public boolean isErrorThree() {
        for(Union union : unionList){
            if(union.isErrorThree()){
                return true;
            }
        }
        return false;
    }



    //Getters

    public MainScreen getMainScreen(){
        return mainScreen;
    }
    public String getCodeSrc(){
        return codeSrc;
    }
    public boolean isFileLoaded(){
        return fileLoaded;
    }
    public boolean isFileRun() {
        return fileRun;
    }
    public boolean isCircuitSolved(){
        return circuitSolved;
    }
    public boolean isNodeFound(String nodeName){
        for (Node node : nodeList){
            if(node.getNodeName().equals(nodeName)){
                return true;
            }
        }
        return false;
    }
    public List<Node> getNodeList(){
        return nodeList;
    }

    //Setters

    public void setCodeSrc(String codePath){
        codeSrc = codePath;
        Path path = Paths.get(codeSrc);
        try {
            inputList = Files.readAllLines(path);
            fileLoaded = true;
        }
        catch (IOException e) {
            Errors.fileNotFound();
        }
    }
    public boolean setResultsFolder(String text) {
        if(!text.equals("")){
            resultFolderName = text;
            return true;
        }
        return false;
    }



    //Graphics

    public void fillEditor() {
        mainScreen.fillEditorPane(inputList);
    }

    public double findNodeVoltage(String pNode, double calcTime) {
        Node node;
        try {
            node = findNode(pNode);
        } catch (IOException e) {
            return 0;
        }
        String address = node.getAddress();
        List<String> lines;
        //log("Node Address is : " + address);
        try {
            lines = Files.readAllLines(Paths.get(address));
        } catch (IOException e) {
            Errors.fileNotFound();
            return 0;
        }
        double time;
        double voltage;
        //log("Node lines read");
        for (String line : lines){
            String[] strings = line.split("\\s+");
            time = Double.parseDouble(strings[0]);
            //log("time Found");
            voltage = Double.parseDouble(strings[1]);
            //log("voltage Found");
            if(Math.abs(time-calcTime)<this.deltaT){
                return voltage;
            }
        }
        Errors.WrongTimeInputForResults();
        return 0;

    }

    public List<Element> getElementList() {
        return elementList;
    }
}

