package Controllers;

import Models.Question;
import Models.Quiz;
import Services.QuizService;
import Utilities.ConsoleUI;
import Utilities.QuestionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class QuizCreate {
    private Scanner scanner;
    private final QuizService quizService;

    public QuizCreate(Scanner scanner, QuizService quizService) {
        this.scanner = scanner;
        this.quizService = quizService;
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

        try{
            quizService.saveQuizAsync(quiz).get();
            ConsoleUI.printSuccess("Quiz '" + title + "' created with " + questions.size() + " questions");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private Question<String> createQuestion() {
        return QuestionBuilder.createQuestion(scanner);
    }
}
