public class Quiz{
    private String quizId;
    private String title;
    private java.util.List<Question> questions;
    private java.util.Map<String, Answer> userAnswers;
    private int totalScore;
    public Quiz(String quizId, String title){
        this.quizId = quizId;
        this.title = title;
        this.questions = new java.util.ArrayList<>();
        this.userAnswers = new java.util.HashMap<>();
        this.totalScore = 0;
    }
    public void addQuestion(Question question){
        questions.add(question);
    }
    public boolean removeQuestion(String questionId){return questions.removeIf(q -> q.getId().equals(questionId));}
    public Question getQuestion(int index){
        if (index >= 0 && index < questions.size()){
            return questions.get(index);
        } return null;
    }
    public java.util.List<Question> getAllQuestions(){ return new java.util.ArrayList<>(questions); }
    public void userAnswers(Answer answer){userAnswers.put(answer.getQuestionId(), answer);}
    public int calculateScore(){
        int score = 0;
        for (Question question : questions){
            Answer answer = userAnswers.get(question.getId());
            if (answer != null && question.checkAnswer(answer)){
                score += question.getScore();
            }
        }
        return score;
    }
    public int getTotalScore(){ return totalScore; }
    public int getQuestionCount(){ return questions.size(); }
    public String getQuizId(){ return quizId; }
    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }
    @Override
    public String toString(){
        return String.format("Quiz: %s (ID: %s, %d questions, %d points total)", title, quizId, questions.size(), calculateScore());}

}