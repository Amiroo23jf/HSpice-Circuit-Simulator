package base;

import java.io.IOException;
import java.util.Scanner;

public class CircuitSimulator {
    public static void main(String[] args) throws IOException {
        Scanner consoleScanner = new Scanner(System.in);
        Database.getInstance().run(consoleScanner);
    }
}
