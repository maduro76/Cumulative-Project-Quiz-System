package Controllers;

import Models.Question;
import Models.Quiz;
import Services.SaveAndReadJSON;
import Utilities.ConsoleUI;
import Utilities.QuestionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;


public class QuizCreate {
    private Scanner scanner;

    public QuizCreate(Scanner scanner) {
        this.scanner = scanner;
    }

    public void createQuiz() {
        ConsoleUI.printHeader("CREATE NEW QUIZ");

        String title = ConsoleUI.readInput("Quiz Title: ");
        if (title.isEmpty()) {
            ConsoleUI.printError("Title cannot be empty");
            return;
        }

        List<Question<String>> questions = new ArrayList<>();

        while (true) {
            ConsoleUI.printSeparator();
            System.out.println("\nQuestion " + (questions.size() + 1));

            Question<String> q = createQuestion();
            if (q != null) {
                questions.add(q);
                ConsoleUI.printSuccess("Question added");
            }

            String more = ConsoleUI.readInput("\nAdd another question? (y/n): ");
            if (!more.equalsIgnoreCase("y")) {
                break;
            }
        }

        if (questions.isEmpty()) {
            ConsoleUI.printError("No questions added! Quiz not created");
            return;
        }

        String quizId = "QUIZ-" + UUID.randomUUID().toString().substring(0, 6);
        Quiz<String> quiz = new Quiz<>(quizId, title, new ArrayList<>(questions));
        List<Quiz<String>> existingQuizzes = SaveAndReadJSON.readQuizJSON("quiz_data.json");
        if (existingQuizzes == null) {
            existingQuizzes = new ArrayList<>();
        }
        existingQuizzes.add(quiz);

        SaveAndReadJSON.saveQuiz(existingQuizzes, "quiz_data.json");
        ConsoleUI.printSuccess("Quiz '" + title + "' created with " + questions.size() + " questions");
    }

    private Question<String> createQuestion() {
        return QuestionBuilder.createQuestion(scanner);
    }
}
