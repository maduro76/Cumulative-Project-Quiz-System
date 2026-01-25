package Controllers;

import Utilities.*;

import java.util.Scanner;

public class QuizManager {
    private Scanner scanner;
    private QuizTake quizTaker;
    private QuizCreate quizCreator;
    private QuizEdit quizEditor;

    public QuizManager() {
        this.scanner = new Scanner(System.in);
        this.quizTaker = new QuizTake(scanner);
        this.quizCreator = new QuizCreate(scanner);
        this.quizEditor = new QuizEdit(scanner);
    }

    public void run() {
        ConsoleUI.printHeader("QUIZ SYSTEM - CONSOLE EDITION");

        while (true) {
            showMainMenu();
            int choice = ConsoleUI.readInt("\nEnter your choice: ");

            switch (choice) {
                case 1:
                    handleTakeQuiz(true);
                    break;
                case 2:
                    handleCreateQuiz();
                    break;
                case 3:
                    handleEditQuiz();
                    break;
                case 4:
                    handleExit();
                    return;
                default:
                    ConsoleUI.printError("Invalid choice. Please try again");
            }
        }
    }

    private void showMainMenu() {
        ConsoleUI.printSeparator();
        System.out.println("\nMAIN MENU");
        System.out.println("1. Take Quiz");
        System.out.println("2. Create Quiz");
        System.out.println("3. Edit Existing Quiz");
        System.out.println("4. Exit");
    }

    private void handleTakeQuiz(boolean withTimer) {
        quizTaker.start();
    }

    private void handleCreateQuiz() {
        quizCreator.createQuiz();
        ConsoleUI.waitForEnter();
    }

    private void handleEditQuiz() {
        quizEditor.editQuiz();
        ConsoleUI.waitForEnter();
    }

    private void handleExit() {
        ConsoleUI.printInfo("Exiting quiz system...");
        ThreadManager.shutdown();
        scanner.close();
        System.out.println("\nThank you for using Quiz System");
    }
}