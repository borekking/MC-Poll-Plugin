package de.borekking.mcPolls.poll.answer;

import java.util.List;

public class Answer extends StaticAnswer {

    // Methode to get Array of Answers from List of Answer
    public static String[] getAnswerArray(List<Answer> answers) {
        return answers.stream().map(Answer::getAnswer).toArray(String[]::new);
    }

    Answer(String answer, int amount) {
        super(answer, amount);
    }

    public Answer(StaticAnswer answer) {
        this(answer.getAnswer(), answer.getAmount());
    }

    public Answer(String answer) {
        super(answer);
    }

    public void decrease() {
        if (this.amount == 0) return;
        this.amount--;
    }

    public void increase() {
        this.amount++;
    }
}
