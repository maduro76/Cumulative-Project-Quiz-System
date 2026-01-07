package Quizes;

import Questions.Question;
import Questions.IQuestion;
import Answers.Answer;
import Answers.IAnswer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quiz<T> implements IQuiz<T> {
    private String quizId;
    private String title;
    private List<IQuestion<T>> questions;
    private Map<String, Answer<T>> userAnswers;
    private int totalScore;

    public Quiz() {
        this.questions = new ArrayList<>();
        this.userAnswers = new HashMap<>();
    }
    public Quiz(String quizId, String title, List<IQuestion<T>> questions) {
        this.quizId = quizId;
        this.title = title;
        this.questions = questions;
        this.userAnswers = new HashMap<>();
        this.totalScore = 0;
    }
    public void userAnswers(IAnswer<T> answer) {
        if (answer instanceof Answer) {
            userAnswers.put(answer.getQuestionId(), (Answer<T>) answer);
        }
    }
    public int calculateScore() {
        int tempScore = 0;
        for (IQuestion<T> q : questions) {
            IAnswer<T> ans = userAnswers.get(q.getId());
            if (ans != null && q.checkAnswer(ans)) {
                tempScore += q.getScore();
            }
        }
        this.totalScore = tempScore;
        return tempScore;
    }
    public List<IQuestion<T>> getQuestions() { return questions; } //entire collection
    public int getTotalScore() { return totalScore; }
    public void addQuestion(IQuestion<T> q) {questions.add((Question<T>) q);}
    public boolean removeQuestion(int id) { return false; }
    public IQuestion<T> getQuestion(int index) { return questions.get(index); } //single item
    public List<IQuestion<T>> getAllQuestions() { return new ArrayList<>(questions); }
    public int getQuestionCount() { return questions.size(); }
    public String getQuizId() { return quizId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}