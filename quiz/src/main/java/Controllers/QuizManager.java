package Controllers;

import Data.DataManager;
import Data.JsonQuizManager;
import Models.Quiz;
import Services.QuizService;
import Utilities.*;

import java.util.Scanner;

public class QuizManager {
    private Scanner scanner;
    private QuizService quizService;
    private QuizTake quizTaker;
    private QuizCreate quizCreator;
    private QuizEdit quizEditor;
    private QuizDelete quizDeleter;

    public QuizManager() {
        this.scanner = new Scanner(System.in);

        DataManager<Quiz<String>> dataManager = new JsonQuizManager();
        this.quizService = new QuizService(dataManager);

        this.quizTaker = new QuizTake(scanner, quizService);
        this.quizCreator = new QuizCreate(scanner, quizService);
        this.quizEditor = new QuizEdit(scanner, quizService);
        this.quizDeleter = new QuizDelete(scanner, quizService);
    }

    public void run() {
        ConsoleUI.printHeader("QUIZ SYSTEM - CONSOLE EDITION");

        while (true) {
            showMainMenu();
            int choice = ConsoleUI.readInt("\nEnter your choice: ");

            switch (choice) {
                case 1:
                    handleTakeQuiz();
                    break;
                case 2:
                    handleCreateQuiz();
                    break;
                case 3:
                    handleEditQuiz();
                    break;
                case 4:
                    handleDeleteQuiz();
                    break;
                case 5:
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
        System.out.println("4. Delete Quiz");
        System.out.println("5. Exit");
    }

    private void handleTakeQuiz() {
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

    private void handleDeleteQuiz() {
        quizDeleter.deleteQuiz();
        ConsoleUI.waitForEnter();
    }

    private void handleExit() {
        ConsoleUI.printInfo("Exiting quiz system...");
        quizService.shutdown();
        scanner.close();
        System.out.println("\nThank you for using Quiz System");
    }
}