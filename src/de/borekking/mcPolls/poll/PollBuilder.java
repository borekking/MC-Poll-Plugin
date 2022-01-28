package de.borekking.mcPolls.poll;

import de.borekking.mcPolls.poll.types.ActivePoll;
import de.borekking.mcPolls.util.JavaUtils;
import de.borekking.mcPolls.util.TimeUnit;

import java.util.ArrayList;
import java.util.List;

public class PollBuilder {

    /*
     * Builder for creating new Active Poll.
     *
     */

    private String name, question;

    private final List<String> answers;

    // Poll's length in milliseconds, default 1 Hour
    private long pollLength = TimeUnit.HOUR.getMillis();

    public PollBuilder() {
        this.answers = new ArrayList<>();
    }

    public PollBuilder name(String n) {
        this.name = n;
        return this;
    }

    public PollBuilder question(String q) {
        this.question = q;
        return this;
    }

    public PollBuilder addAnswer(String a) {
        this.answers.add(a);
        return this;
    }

    public PollBuilder removeAnswer(String a) {
        this.answers.remove(a);
        return this;
    }

    public PollBuilder pollLength(long pollLength) {
        this.pollLength = pollLength;
        return this;
    }

    public ActivePoll build() {
        return new ActivePoll(this.name, this.question, JavaUtils.toArray(this.answers), this.pollLength);
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getName() {
        return name;
    }

    public String getQuestion() {
        return question;
    }

    public long getPollLength() {
        return pollLength;
    }

    // Return if the Poll is valid to be created
    // Its valid if: Name is set, name ist one word (no spaces), name is not empty, question is not null, question is not empty and there is at least one answers.
    public boolean isValid() {
        return this.name != null && !this.name.contains(" ") && !this.name.trim().isEmpty() && this.question != null && !this.question.trim().isEmpty() && this.answers.size() >= 1;
    }
}


