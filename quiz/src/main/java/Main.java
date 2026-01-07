import Answers.Answer;
import FileHandling.SaveAndReadJSON;
import Questions.IQuestion;
import Questions.Question;
import Quizes.Quiz;

import java.util.*;

public class Main {
    private static final String FILENAME = "quiz_data.json";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        List<Quiz<String>> quizzes = SaveAndReadJSON.readQuizJSON(FILENAME);

        boolean programRunning = true;
        while (programRunning) {
            System.out.println("\n=== Quiz System - Menu ===");
            System.out.println("1. Choose quiz to take or edit");
            System.out.println("2. Create NEW quiz (New Category)");
            System.out.println("3. Save and Exit");
            System.out.print("Your choice: ");

            String mainChoice = scanner.nextLine().trim();

            switch (mainChoice) {
                case "1":
                    if (quizzes.isEmpty()) {
                        System.out.println(">> No quizzes available! Use option 2 to create the first one.");
                    } else {
                        Quiz<String> selectedQuiz = selectQuiz(quizzes);
                        if (selectedQuiz != null) {
                            manageQuiz(selectedQuiz);
                        }
                    }
                    break;
                case "2":
                    createNewQuiz(quizzes);
                    break;
                case "3":
                    System.out.println("Saving data...");
                    SaveAndReadJSON.saveQuiz(quizzes, FILENAME);
                    System.out.println("Goodbye!");
                    programRunning = false;
                    break;
                default:
                    System.out.println("Unknown option. Choose 1, 2, or 3.");
            }
        }
    }
    private static void createNewQuiz(List<Quiz<String>> quizzes) {
        System.out.println("\n--- CREATE NEW QUIZ ---");
        System.out.print("Enter quiz name/category (e.g., History, Movies, Sports): ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter unique ID (e.g., HIST-1): ");
        String id = scanner.nextLine().trim();
        boolean exists = quizzes.stream().anyMatch(q -> q.getQuizId().equalsIgnoreCase(id));
        if (exists) {
            System.out.println(">> Error: A quiz with this ID already exists!");
            return;
        }
        Quiz<String> newQuiz = new Quiz<>(id, title, new ArrayList<>());
        quizzes.add(newQuiz);
        System.out.println(">> Success! Created new quiz: " + title);
    }
    private static Quiz<String> selectQuiz(List<Quiz<String>> quizzes) {
        System.out.println("\n--- AVAILABLE QUIZZES ---");
        for (int i = 0; i < quizzes.size(); i++) {
            System.out.println((i + 1) + ". " + quizzes.get(i).getTitle() + " (ID: " + quizzes.get(i).getQuizId() + ")");
        }
        System.out.print("Choose quiz number: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < quizzes.size()) {
                return quizzes.get(index);
            }
        } catch (NumberFormatException e) {
        }
        System.out.println(">> Invalid choice.");
        return null;
    }
    private static void manageQuiz(Quiz<String> quiz) {
        boolean insideQuiz = true;
        while (insideQuiz) {
            System.out.println("\n--- QUIZ: " + quiz.getTitle().toUpperCase() + " ---");
            System.out.println("1. Solve this quiz");
            System.out.println("2. Add a question to this quiz");
            System.out.println("3. Show list of questions");
            System.out.println("4. Return to Main Menu");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    solveQuiz(quiz);
                    break;
                case "2":
                    addQuestionInteractively(quiz);
                    break;
                case "3":
                    showAllQuestions(quiz);
                    break;
                case "4":
                    insideQuiz = false;
                    break;
                default:
                    System.out.println("Unknown option.");
            }
        }
    }
    private static void solveQuiz(Quiz<String> quiz) {
        if (quiz.getQuestionCount() == 0) {
            System.out.println(">> This quiz is empty! Please add questions first (Option 2).");
            return;
        }

        System.out.println("\n--- STARTING QUIZ: " + quiz.getTitle() + " ---");
        for (IQuestion<String> q : quiz.getAllQuestions()) {
            q.displayQuestion();

            System.out.print("Your answer (A/B): ");
            String input = scanner.nextLine().trim();

            Answer<String> ans = new Answer<>(q.getId(), input);
            quiz.userAnswers(ans);

            if (q.checkAnswer(ans)) {
                System.out.println("Correct! (+" + q.getScore() + " pts)");
            } else {
                System.out.println("Wrong. Correct answer: " + q.getCorrectAns());
            }
        }
        System.out.println("YOUR FINAL SCORE: " + quiz.calculateScore() + " pts");
    }

    private static void addQuestionInteractively(Quiz<String> quiz) {
        System.out.println("\n--- ADDING QUESTION TO: " + quiz.getTitle() + " ---");
        System.out.print("Question ID (e.g., Q1): ");
        String id = scanner.nextLine();

        System.out.print("Question text: ");
        String text = scanner.nextLine();

        System.out.print("Option A: ");
        String optA = scanner.nextLine();

        System.out.print("Option B: ");
        String optB = scanner.nextLine();

        Map<Character, String> options = new HashMap<>();
        options.put('A', optA);
        options.put('B', optB);

        System.out.print("Correct answer (type A or B): ");
        String correct = scanner.nextLine().trim();

        System.out.print("Points for the question: ");
        int points = 0;
        try {
            points = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Number error, setting to 0.");
        }
        String category = quiz.getTitle();
        Question<String> newQ = new Question<>(id, text, correct, points, category, options);
        quiz.addQuestion(newQ);
        System.out.println(">> Question added successfully!");
    }

    private static void showAllQuestions(Quiz<String> quiz) {
        System.out.println("\n--- List of questions in " + quiz.getTitle() + " database ---");
        if (quiz.getQuestionCount() == 0) {
            System.out.println("(No questions)");
        } else {
            for (int i = 0; i < quiz.getQuestionCount(); i++) {
                System.out.println(i + 1 + ". " + quiz.getQuestion(i));
            }
        }
    }
}