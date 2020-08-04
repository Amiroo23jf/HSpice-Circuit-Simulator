package extra;

import base.Database;

import javax.swing.*;

public class Errors {
    private static boolean tranFlag = false;
    private static boolean dvFlag = false;
    private static boolean dtFlag = false;
    private static boolean diFlag = false;
    public static int line = 0;


    //Errors

    public static void wrongInputError(){
        logError("Error : Wrong Input in line " + line);
        System.exit(0);
    }
    public static void nodeNameError(String name){
        //checks if the string is numeric
        if(!NumberConverter.isNumeric(name)){
            logError("Error : Node Name is Not Numeric in line : "+ line);
            System.exit(0);
        }
    }
    public static void errorOne(){
        logError("Error -1");
        System.exit(0);
        //returns error -1
    }
    public static void errorTwo(){
        logError("Error -2");
        System.exit(0);
    }
    public static void errorThree() {
        logError("Error -3");
        System.exit(0);
    }
    public static void fileNotFound() {
        System.err.println("Error : Source File Not Found");
        System.exit(0);
    }
    public static void earthNodeNotFound() {
        //Error -4 , Earth Node Not Found
        logError("Error -4 : Earth Node Not Found");
        System.exit(0);
    }
    public static void fileLoadedBefore() {
        graphicalError("Error : File Has Been Loaded Before!","File Loaded Error");
        logError("Error : File Loaded Before");
    }
    public static void fileNotLoaded() {
        graphicalError("Please Load A File First", "File Not Loaded Yet");
        //logError("Error : File Not Loaded Yet");
    }
    public static void enterSavingFolder() {
        logError("Error : Saving Folder is Not Valid");
        graphicalError("Please Enter The Saving Folder ","Saving Folder Not Entered");
    }
    public static void nodeNotFound() {
        logError("Error : Node Not Found");
        graphicalError("Node Not Found","Node Not Found");
    }
    public static void WrongTimeInputForResults() {
        logError("Error : Incorrect Time Input");
        graphicalError("Please Enter A Correct Time ","Incorrect Input");
    }
    public static void circuitNotSolved() {
        graphicalError("Please Solve The Circuit First","Circuit Not Solved");
    }

    //Setter

    public static void setTranFlag(){
        Errors.tranFlag = true;
    }
    public static void setDvFlag(){
        Errors.dvFlag = true;
    }
    public static void setDiFlag() {
        Errors.diFlag = true;
    }
    public static void setDtFlag() {
        Errors.dtFlag = true;
    }

    //Getters

    public static boolean getTranFlag(){
        return tranFlag;
    }
    public static boolean getDvFlag(){
        return dvFlag;
    }
    public static boolean getDiFlag(){
        return diFlag;
    }
    public static boolean getDtFlag(){
        return dtFlag;
    }

    //Logger

    private static void logError(String error){
        System.err.println(error);
        Database.getInstance().resetLogTabs();
        Database.getInstance().log(error);
    }
    private static void graphicalError(String error, String title){
        JOptionPane.showMessageDialog(Database.getInstance().getMainScreen(),error,title,JOptionPane.ERROR_MESSAGE);
    }

    //Checkers

    public static void checkErrorTwo(){
        if(Database.getInstance().isErrorTwo()){
            Errors.errorTwo();
        }
    }
    public static void checkErrorThree(){
        if(Database.getInstance().isErrorThree()){
            Errors.errorThree();
        }
    }


}
