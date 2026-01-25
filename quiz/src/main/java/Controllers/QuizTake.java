package Controllers;

import Models.Answer;
import Models.Question;
import Models.Quiz;
import Services.SaveAndReadJSON;
import Utilities.ConsoleUI;
import Utilities.ThreadManager;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class QuizTake {
    private Scanner scanner;

    public QuizTake(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        Quiz<String> quiz = selectQuizCategory();
        if (quiz == null) return;

        takeQuiz(quiz);
    }

    private Quiz<String> selectQuizCategory() {
        ConsoleUI.printInfo("Loading quizzes...");

        Future<List<Quiz<String>>> future = ThreadManager.getExecutor().submit(() ->
                SaveAndReadJSON.readQuizJSON("quiz_data.json")
        );
        try {
            List<Quiz<String>> quizzes = future.get(5, TimeUnit.SECONDS);
            if (quizzes == null || quizzes.isEmpty()) {
                ConsoleUI.printError("No quizzes found");
                return null;
            }
            ConsoleUI.printHeader("SELECT QUIZ CATEGORY");
            for (int i = 0; i < quizzes.size(); i++) {
                System.out.println((i + 1) + ". " + quizzes.get(i).getTitle() +
                        " (" + quizzes.get(i).getQuestionCount() + " questions)");
            }

            int choice = ConsoleUI.readInt("\nSelect quiz (1-" + quizzes.size() + "): ");
            if (choice < 1 || choice > quizzes.size()) {
                ConsoleUI.printError("Invalid choice");
                return null;
            }
            return quizzes.get(choice - 1);
        } catch (Exception e) {
            ConsoleUI.printError("Error loading quizzes: " + e.getMessage());
            return null;
        }
    }

    private void takeQuiz(Quiz<String> quiz) {
        ConsoleUI.printHeader(quiz.getTitle());
        ConsoleUI.printInfo("Questions: " + quiz.getQuestionCount());

        int questionNum = 1;
        for (Question<String> question : quiz.getQuestions()) {
            askQuestion(question, questionNum, quiz);
            questionNum++;
        }

        showResults(quiz);
    }

    private void askQuestion(Question<String> question, int num, Quiz<String> quiz) {
        System.out.println("\n--- Question " + num + " ---");
        question.displayQuestion();

        String userAnswer = ConsoleUI.readInput("\nYour Answer (A/B/C/D): ");
        userAnswer = userAnswer.trim().toUpperCase();

        while (!userAnswer.matches("[A-D]")) {
            ConsoleUI.printError("Invalid! Enter A, B, C, or D.");
            userAnswer = ConsoleUI.readInput("Your Answer: ").trim().toUpperCase();
        }

        Answer<String> answer = new Answer<>(question.getId(), userAnswer);
        quiz.addUserAnswer(answer);

        if (question.checkAnswer(answer)) {
            ConsoleUI.printSuccess("Correct");
        } else {
            ConsoleUI.printError("Wrong! Answer: " + question.getCorrectAnswer());
        }
    }

    private void showResults(Quiz<String> quiz) {
        ConsoleUI.printHeader("RESULTS");

        int score = quiz.calculateScore();
        int maxScore = quiz.getMaxPossibleScore();
        double percentage = (maxScore > 0) ? (score * 100.0 / maxScore) : 0;

        System.out.println("Score: " + score + "/" + maxScore);
        System.out.printf("Percentage: %.1f%%\n", percentage);

        if (percentage >= 80) {
            ConsoleUI.printSuccess("Excellent");
        } else if (percentage >= 60) {
            ConsoleUI.printInfo("Good job");
        } else {
            ConsoleUI.printInfo("Keep practicing");
        }
    }
}