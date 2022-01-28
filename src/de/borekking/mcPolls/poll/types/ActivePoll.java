package de.borekking.mcPolls.poll.types;

import de.borekking.mcPolls.poll.answer.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActivePoll extends AbstractPoll<Answer> {

    // The user's answers
    private final List<Answer> answers;

    // The players' answers from Player to Integer (index)
    private final Map<UUID, Integer> votes;

    public ActivePoll(String name, String question, String[] answersArray, long start, long length) {
        super(name, question, start, length);
        this.answers = this.createEmptyAnswers(answersArray);
        this.votes = new HashMap<>();
    }

    public ActivePoll(String name, String question, String[] answersArray, long length) {
        super(name, question, length);
        this.answers = this.createEmptyAnswers(answersArray);
        this.votes = new HashMap<>();
    }

    public ActivePoll(String name, String question, List<Answer> answers, Map<UUID, Integer> chooses, long start, long length) {
        super(name, question, start, length);
        this.answers = answers;
        this.votes = chooses;
    }

    // Methode to increase an answer (poll)
    public void updateAnswer(UUID player, int index) {
        // Return if not a valid answer (index)
        if (!this.validAnswer(index)) return;

        // Get Players latest choice
        int latestChoose = this.getPlayerChoose(player);

        // Return if the player already chose this index
        if (latestChoose == index) return;

        // Decrease latest choice if not <0 (no latest choice)
        if (latestChoose >= 0) this.decreaseAnswer(latestChoose);

        // Increase new choice (index)
        this.increaseAnswer(index);

        // (Re-)set chooses entry
        this.votes.put(player, index);
    }

    // Get current amount of an answer
    public int getAmount(int index) {
        return this.getAnswer(index).getAmount();
    }

    // Returns a players choice or a < 0 value (if the player did not choose yet)
    public int getPlayerChoose(UUID player) {
        return this.votes.getOrDefault(player, -1);
    }

    // Increase an answer
    private void increaseAnswer(int index) {
        this.getAnswer(index).increase();
    }

    // Decrease an answer
    private void decreaseAnswer(int index) {
        this.getAnswer(index).decrease();
    }

    // Returns if an answer (int) is a valid answer (0 <= answer < answersArray.length)
    private boolean validAnswer(int answer) {
        return answer >= 0 && answer < this.size();
    }

    private List<Answer> createEmptyAnswers(String[] answersArray) {
        List<Answer> answers = new ArrayList<>();
        for (String a : answersArray) answers.add(new Answer(a));
        return answers;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public Map<UUID, Integer> getVotes() {
        return votes;
    }
}
