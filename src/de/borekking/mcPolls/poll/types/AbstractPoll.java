package de.borekking.mcPolls.poll.types;

import de.borekking.mcPolls.poll.answer.AnswersHolder;
import de.borekking.mcPolls.poll.answer.StaticAnswer;

public abstract class AbstractPoll<T extends StaticAnswer> implements AnswersHolder<T> {

    // The Polls End as nanoseconds from 1970
    private final long start, length;

    // The Poll's question
    private final String question;

    // The Polls Name
    private final String name;

    public AbstractPoll(String name, String question, long start, long length) {
        // Prevent Poll names with spaces or without content
        if (name.contains(" ") || name.length() == 0) throw new RuntimeException("Illegal Name for Poll: " + name);

        this.name = name;
        this.question = question;

        // Set Start and length
        this.start = start;
        this.length = length;
    }

    // Constructor without the start time, setting it to the current time
    public AbstractPoll(String name, String question, long length) {
        this(name, question, System.currentTimeMillis(), length);
    }

    // Getters (immutable)
    public String getQuestion() {
        return question;
    }

    public String getName() {
        return name;
    }

    // Times in millis
    public long getStart() {
        return start;
    }

    public long getLength() {
        return length;
    }

    public long getEnd() {
        return this.start + this.length;
    }

    public long getTimeLeft() {
        return this.getEnd() - System.currentTimeMillis(); // Return end-time minus current time.
    }

    public boolean hasEnded() {
        return this.getTimeLeft() <= 0;
    }
}
