import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private String text;
    private List<String> options;
    private String correctAnswer;

    public Question(String text, List<String> options, String correctAnswer) {
        if (options == null || options.size() != 4) {
            throw new IllegalArgumentException("Question must have 4 choices");
        }
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }


    public boolean isCorrectAnswer(String answer) {
        return correctAnswer.equalsIgnoreCase(answer);
    }


}
