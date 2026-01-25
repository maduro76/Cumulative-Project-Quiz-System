package Controllers;

import Models.Question;
import Models.Quiz;
import Services.*;
import Services.SaveAndReadJSON;
import Utilities.ConsoleUI;
import Utilities.QuestionBuilder;

import java.util.List;
import java.util.Scanner;

public class QuizEdit {
    private Scanner scanner;

    public QuizEdit(Scanner scanner) {
        this.scanner = scanner;
    }

    public void editQuiz() {
        ConsoleUI.printHeader("EDIT QUIZ");

        // Load all quizzes
        List<Quiz<String>> quizzes = SaveAndReadJSON.readQuizJSON("quiz_data.json");

        if (quizzes == null || quizzes.isEmpty()) {
            ConsoleUI.printError("No quizzes found");
            return;
        }

        // Display available quizzes
        ConsoleUI.printInfo("Available Quizzes:");
        for (int i = 0; i < quizzes.size(); i++) {
            System.out.println((i + 1) + ". " + quizzes.get(i).getTitle() +
                    " (" + quizzes.get(i).getQuestionCount() + " questions)");
        }

        // Select quiz
        int choice = ConsoleUI.readInt("\nSelect quiz to edit (1-" + quizzes.size() + "): ");

        if (choice < 1 || choice > quizzes.size()) {
            ConsoleUI.printError("Invalid selection");
            return;
        }

        Quiz<String> quiz = quizzes.get(choice - 1);
        int quizIndex = choice - 1;

        ConsoleUI.printSuccess("Loaded: " + quiz.getTitle());
        ConsoleUI.printInfo("Questions: " + quiz.getQuestionCount());

        // Edit menu loop
        while (true) {
            showEditMenu();
            int menuChoice = ConsoleUI.readInt("\nChoice: ");

            switch (menuChoice) {
                case 1:
                    viewQuestions(quiz);
                    break;
                case 2:
                    addQuestion(quiz);
                    break;
                case 3:
                    editQuestion(quiz);
                    break;
                case 4:
                    deleteQuestion(quiz);
                    break;
                case 5:
                    editTitle(quiz);
                    break;
                case 6:
                    quizzes.set(quizIndex, quiz);
                    SaveAndReadJSON.saveQuiz(quizzes, "quiz_data.json");
                    ConsoleUI.printSuccess("Quiz saved");
                    return;
                case 7:
                    ConsoleUI.printInfo("Changes discarded");
                    return;
                default:
                    ConsoleUI.printError("Invalid choice");
            }
        }
    }

    private void showEditMenu() {
        ConsoleUI.printSeparator();
        System.out.println("\n EDIT MENU");
        System.out.println("1. View All Questions");
        System.out.println("2. Add Question");
        System.out.println("3. Edit Question");
        System.out.println("4. Delete Question");
        System.out.println("5. Edit Quiz Title");
        System.out.println("6. Save & Exit");
        System.out.println("7. Exit Without Saving");
    }

    private void viewQuestions(Quiz<String> quiz) {
        ConsoleUI.printHeader("QUESTIONS IN QUIZ");

        if (quiz.getQuestionCount() == 0) {
            ConsoleUI.printInfo("No questions in this quiz");
            return;
        }

        List<Question<String>> questions = quiz.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Question<String> q = questions.get(i);
            System.out.println("\n" + (i + 1) + ". " + q.getQuestion());
            System.out.println("   Correct: " + q.getCorrectAnswer() +
                    " | Score: " + q.getScore() +
                    " | Category: " + q.getCategory());

            if (q.getAnswerOptions() != null) {
                q.getAnswerOptions().forEach((key, value) ->
                        System.out.println("   " + key + ". " + value)
                );
            }
        }
    }

    private void addQuestion(Quiz<String> quiz) {
        ConsoleUI.printHeader("ADD NEW QUESTION");

        Question<String> newQuestion = createQuestion();
        if (newQuestion != null) {
            quiz.addQuestion(newQuestion);
            ConsoleUI.printSuccess("Question added, total: " + quiz.getQuestionCount());
        }
    }

    private void editQuestion(Quiz<String> quiz) {
        if (quiz.getQuestionCount() == 0) {
            ConsoleUI.printError("No questions to edit");
            return;
        }

        viewQuestions(quiz);

        int index = ConsoleUI.readInt("\nQuestion number to edit (1-" +
                quiz.getQuestionCount() + "): ") - 1;

        if (index < 0 || index >= quiz.getQuestionCount()) {
            ConsoleUI.printError("Invalid question number");
            return;
        }

        Question<String> oldQuestion = quiz.getQuestions().get(index);

        System.out.println("\nEditing: " + oldQuestion.getQuestion());
        System.out.println("(Leave blank to keep current value)\n");

        System.out.print("Question [" + oldQuestion.getQuestion() + "]: ");
        String newText = scanner.nextLine().trim();
        String questionText = newText.isEmpty() ? oldQuestion.getQuestion() : newText;
        String answerA = editOption("A", oldQuestion);
        String answerB = editOption("B", oldQuestion);
        String answerC = editOption("C", oldQuestion);
        String answerD = editOption("D", oldQuestion);

        System.out.print("Correct answer [" + oldQuestion.getCorrectAnswer() + "]: ");
        String newCorrect = scanner.nextLine().trim().toUpperCase();
        String correctAns = newCorrect.isEmpty() ?
                String.valueOf(oldQuestion.getCorrectAnswer()) : newCorrect;

        System.out.print("Points [" + oldQuestion.getScore() + "]: ");
        String pointsStr = scanner.nextLine().trim();
        int points = pointsStr.isEmpty() ? oldQuestion.getScore() :
                Integer.parseInt(pointsStr);

        System.out.print("Category [" + oldQuestion.getCategory() + "]: ");
        String newCat = scanner.nextLine().trim();
        String category = newCat.isEmpty() ? oldQuestion.getCategory() : newCat;

        Question<String> updatedQuestion = new Question<>(
                oldQuestion.getId(),
                questionText,
                correctAns,
                points,
                category,
                answerA,
                answerB,
                answerC,
                answerD
        );

        quiz.getQuestions().set(index, updatedQuestion);
        ConsoleUI.printSuccess("Question updated");
    }

    private String editOption(String letter, Question<String> question) {
        String current = question.getAnswerOptions().get(letter.charAt(0));
        System.out.print("Option " + letter + " [" + current + "]: ");
        String newValue = scanner.nextLine().trim();
        return newValue.isEmpty() ? current : newValue;
    }

    private void deleteQuestion(Quiz<String> quiz) {
        if (quiz.getQuestionCount() == 0) {
            ConsoleUI.printError("No questions to delete");
            return;
        }

        viewQuestions(quiz);

        int index = ConsoleUI.readInt("\nQuestion number to delete (1-" +
                quiz.getQuestionCount() + "): ") - 1;

        if (index < 0 || index >= quiz.getQuestionCount()) {
            ConsoleUI.printError("Invalid question number");
            return;
        }

        Question<String> deleted = quiz.getQuestions().remove(index);
        ConsoleUI.printSuccess("Deleted: " + deleted.getQuestion());
        ConsoleUI.printInfo("Remaining questions: " + quiz.getQuestionCount());
    }

    private void editTitle(Quiz<String> quiz) {
        System.out.println("Current title: " + quiz.getTitle());
        String newTitle = ConsoleUI.readInput("New title: ");

        if (!newTitle.isEmpty()) {
            quiz.setTitle(newTitle);
            ConsoleUI.printSuccess("Title updated to: " + newTitle);
        } else {
            ConsoleUI.printInfo("Title unchanged");
        }
    }

    private void saveAndExit(List<Quiz<String>> allQuizzes, String filename) {
        SaveAndReadJSON.saveQuiz(allQuizzes, filename);
        ConsoleUI.printSuccess("Quiz saved successfully");
    }
    private Question<String> createQuestion() {
        return QuestionBuilder.createQuestion(scanner);
    }
}