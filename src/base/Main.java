package base;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        double t0 = Double.parseDouble(input);
        input = scanner.nextLine();
        while(!input.startsWith("END")){
            Database.createElement(input);
            input = scanner.nextLine();
        }
        Database.solver(0.001,0.01,0.000000001 , t0);
        Database.getInstance().printInfo();
    }
}
