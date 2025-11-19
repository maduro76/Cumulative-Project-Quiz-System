public class Answer{
    private String questionId;
    private String answerText;
    public Answer(String questionId, String answerText){
        this.questionId = questionId;
        this.answerText = answerText;
    }
    public String getQuestionId(){ return questionId; }
    public String getAnswerText(){ return answerText; }
    public void setAnswerText(String answerText){ this.answerText = answerText; }
    @Override
    public String toString(){
        return String.format("Answer for Q%s: %s", questionId, answerText);
    }
}