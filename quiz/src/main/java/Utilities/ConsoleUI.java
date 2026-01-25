package Utilities;
import java.util.Scanner;

public class ConsoleUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void printHeader(String text){
        System.out.println("\n" + "=".repeat(60));
        System.out.println(" " + text);
        System.out.println("=".repeat(60));
    }

    public static void printSuccess(String message){
        System.out.println("[SUCCESS] " + message);
    }

    public static void printError(String message) {
        System.out.println("✗ " + message);
    }

    public static void printInfo(String message) {
        System.out.println("ℹ " + message);
    }

    public static void printSeparator() {
        System.out.println("-".repeat(60));
    }

    public static String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                printError("Please enter a valid number");
            }
        }
    }

    public static void waitForEnter() {
        System.out.print("\nPress ENTER to continue...");
        scanner.nextLine();
    }
}
