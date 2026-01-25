package Utilities;

import Models.Question;
import java.util.Scanner;
import java.util.UUID;

public class QuestionBuilder {
    public static Question<String> createQuestion(Scanner scanner) {
        try {
            System.out.print("Question: ");
            String questionText = scanner.nextLine().trim();
            if (questionText.isEmpty()) {
                ConsoleUI.printError("Question cannot be empty");
                return null;
            }

            System.out.print("A: ");
            String answerA = scanner.nextLine().trim();

            System.out.print("B: ");
            String answerB = scanner.nextLine().trim();

            System.out.print("C: ");
            String answerC = scanner.nextLine().trim();

            System.out.print("D: ");
            String answerD = scanner.nextLine().trim();

            String correctAns;
            while (true) {
                System.out.print("Correct answer (A/B/C/D): ");
                correctAns = scanner.nextLine().trim().toUpperCase();
                if (correctAns.matches("[A-D]")) break;
                ConsoleUI.printError("Please enter A, B, C, or D");
            }

            int points;
            while (true) {
                try {
                    System.out.print("Points (1-100): ");
                    points = Integer.parseInt(scanner.nextLine().trim());
                    if (points >= 1 && points <= 100) break;
                    ConsoleUI.printError("Points must be between 1 and 100");
                } catch (NumberFormatException e) {
                    ConsoleUI.printError("Invalid number");
                }
            }

            System.out.print("Category [General]: ");
            String category = scanner.nextLine().trim();
            if (category.isEmpty()) category = "General";

            String questionId = "Q" + UUID.randomUUID().toString().substring(0, 6);

            return new Question<>(
                    questionId, questionText, correctAns, points, category,
                    answerA, answerB, answerC, answerD
            );

        } catch (Exception e) {
            ConsoleUI.printError("Error creating question: " + e.getMessage());
            return null;
        }
    }
}