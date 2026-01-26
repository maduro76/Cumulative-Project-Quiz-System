package Controllers;

import Models.Quiz;
import Services.QuizService;
import Utilities.ConsoleUI;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class QuizDelete {
    private Scanner scanner;
    private QuizService quizService;

    public QuizDelete(Scanner scanner, QuizService quizService) {
        this.scanner = scanner;
        this.quizService = quizService;
    }

    public void deleteQuiz() {
        ConsoleUI.printHeader("DELETE QUIZ");

        List<Quiz<String>> quizzes = quizService.getAllQuizzes();

        if (quizzes == null || quizzes.isEmpty()) {
            ConsoleUI.printError("No quizzes found");
            return;
        }

        ConsoleUI.printInfo("Available Quizzes:");
        for (int i = 0; i < quizzes.size(); i++) {
            System.out.println((i + 1) + ". " + quizzes.get(i).getTitle() +
                    " (" + quizzes.get(i).getQuestionCount() + " questions)");
        }

        int choice = ConsoleUI.readInt("\nSelect quiz to delete (1-" + quizzes.size() + ", 0 to cancel): ");

        if (choice == 0) {
            ConsoleUI.printInfo("Delete cancelled");
            return;
        }

        if (choice < 1 || choice > quizzes.size()) {
            ConsoleUI.printError("Invalid selection");
            return;
        }

        Quiz<String> quizToDelete = quizzes.get(choice - 1);

        ConsoleUI.printInfo("You are about to delete: " + quizToDelete.getTitle());
        String confirm = ConsoleUI.readInput("Are you sure? Type 'YES' to confirm: ");

        if (!confirm.equalsIgnoreCase("YES")) {
            ConsoleUI.printInfo("Delete cancelled");
            return;
        }
        try {
            quizService.deleteQuizAsync(quizToDelete.getQuizId()).get();
            ConsoleUI.printSuccess("Quiz '" + quizToDelete.getTitle() + "' deleted successfully");
        } catch (InterruptedException | ExecutionException e) {
            ConsoleUI.printError("Failed to delete quiz: " + e.getMessage());
        }
    }
}