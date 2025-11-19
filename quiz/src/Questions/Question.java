public class Question{
    private String id;
    private String question;
    private String correctAns;
    private int score;
    private String category;
    public Question(String id, String question, String correctAns, int score, String category){
        this.id = id;
        this.question = question;
        this.correctAns = correctAns;
        this.score = score;
        this.category = category;
    }
    public String getId(){ return id; }
    public String getQuestion(){ return question; }
    public String getCorrectAns(){ return correctAns; }
    public int getScore(){ return score; }
    public void setScore(int score){ this.score = score; }  
    public String getCategory(){ return category; }
    public void setCategory(String category){ this.category = category; }
    public void displayQuestion(){
        System.out.println("Q: " + question);
    }
    public boolean checkAnswer(Answer answer){
        if (answer == null || answer.getAnswerText() == null){
            return false;
        }
        return answer.getAnswerText().trim().equalsIgnoreCase(correctAns.trim());
    }
    @Override
    public String toString(){
        return String.format("[%s] %s (%d points) - Category: %s", id, question, score, category);
    }
}