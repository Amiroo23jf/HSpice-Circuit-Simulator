package base;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please Enter Your Saving Src : ");
        String input = scanner.nextLine();
        Database.getInstance().setSavingSrc(input);
        Database.getInstance().makeSavingDir();
        System.out.print("Please Enter Your Target Time : ");
        input = scanner.nextLine();
        double t0 = Double.parseDouble(input);
        System.out.println("Please Enter Your Inputs : ");
        input = scanner.nextLine();
        while(!input.startsWith("END")){
            Database.createElement(input);
            input = scanner.nextLine();
        }
        Database.getInstance().initialize();
        Database.solver(0.001,0.00001,0.01 , t0);
        Database.getInstance().finalResults();
    }
}
