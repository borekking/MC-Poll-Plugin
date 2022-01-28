package de.borekking.mcPolls.poll.answer;

import java.util.List;

public class StaticAnswer {

    private final String answer;

    protected int amount;

    // Methode to get Array of Answers from List of Answer
    public static String[] getAnswerArray(List<Answer> answers) {
        return answers.stream().map(Answer::getAnswer).toArray(String[]::new);
    }

    public StaticAnswer(String answer, int amount) {
        this.answer = answer;
        this.amount = amount;
    }

    public StaticAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public int getAmount() {
        return amount;
    }
}
