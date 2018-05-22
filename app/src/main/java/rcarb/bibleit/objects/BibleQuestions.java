package rcarb.bibleit.objects;

public class BibleQuestions {


    private String mQuestion;

    public BibleQuestions() {}

    public BibleQuestions(String question){
        mQuestion = question;
    }

    public void setQuestion(String question){
        mQuestion = question;
    }

    public String getQuestion(){
        return mQuestion;
    }

}
