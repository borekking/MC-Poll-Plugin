package de.borekking.mcPolls.poll.types;

import de.borekking.mcPolls.poll.answer.StaticAnswer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EndedPoll extends AbstractPoll<StaticAnswer> {

    /*
     * Class to store a Poll's results after it ended.
     *
     */

    // The user's answers
    private final List<StaticAnswer> answers;

    public EndedPoll(String name, String question, List<StaticAnswer> answers, long start, long length) {
        super(name, question, start, length);

        this.answers = answers;
    }

    public EndedPoll(ActivePoll poll) {
        super(poll.getName(), poll.getQuestion(), poll.getStart(), poll.getLength());

        this.answers = this.getAnswers(poll);
    }

    public StaticAnswer getWinner() {
        // Sort answers by amount and return last one. (The Greatest amount of votes)
        return this.answers.stream().sorted(Comparator.comparingInt(StaticAnswer::getAmount)).collect(Collectors.toList()).get(this.size() - 1);
    }

    private List<StaticAnswer> getAnswers(ActivePoll poll) {
        List<StaticAnswer> answers = new ArrayList<>();
        poll.forEach(answers::add);
        return answers;
    }

    public List<StaticAnswer> getAnswers() {
        return answers;
    }
}
