package de.borekking.mcPolls.poll;

import de.borekking.mcPolls.Main;
import de.borekking.mcPolls.poll.types.ActivePoll;
import de.borekking.mcPolls.poll.types.EndedPoll;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PollManager {

    // Map of current Polls and their ending timestamps
    private final List<ActivePoll> activePolls;

    // Ended Polls
    private final List<EndedPoll> endedPolls;

    // Procedure for ending Polls
    private final Consumer<ActivePoll> endingPollProcedure;

    public PollManager(Consumer<ActivePoll> endingPollProcedure) {
        this.activePolls = new ArrayList<>();
        this.endedPolls = new ArrayList<>();

        this.endingPollProcedure = endingPollProcedure;

        this.startTask();
    }

    // Methods to start new Polls
    public void startPoll(ActivePoll poll) {
        this.activePolls.add(poll);
    }

    // Can also be used to stop a poll
    public void cancelPoll(ActivePoll poll) {
        this.activePolls.remove(poll);
    }

    public ActivePoll getActivePoll(String name) {
        return this.activePolls.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean containsActivePoll(String name) {
        return this.activePolls.stream().anyMatch(p -> p.getName().equals(name));
    }

    public EndedPoll getEndedPoll(String name) {
        return this.endedPolls.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean containsEndedPoll(String name) {
        return this.endedPolls.stream().anyMatch(p -> p.getName().equals(name));
    }

    public void addEndedPoll(EndedPoll poll) {
        this.endedPolls.add(poll);
    }

    // Start checking for ending polls
    private void startTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                PollManager.this.checkEndingPolls();
            }
        }.runTaskTimer(Main.getPlugin(), 20L, 20L); // Running every second (20 ticks)
    }

    // Check for ending polls
    private void checkEndingPolls() {
        List<ActivePoll> ended = new ArrayList<>();

        // Go through all Polls
        for (ActivePoll poll : this.activePolls) {
            // If Poll already ended add Poll to EndedPolls
            if (poll.hasEnded()) ended.add(poll);
        }

        // Stop the ended polls (no more answer-changing, etc.)
        ended.forEach(this::stopPoll);
    }

    private void stopPoll(ActivePoll poll) {
        // Cal the endingPollProcedure
        this.endingPollProcedure.accept(poll);

        // Remove the poll from the active Polls
        this.cancelPoll(poll);

        // Save Poll as EndedPoll and put it in belonging List
        this.addEndedPoll(new EndedPoll(poll));
    }

    public List<ActivePoll> getActivePolls() {
        return activePolls;
    }

    public List<EndedPoll> getEndedPolls() {
        return endedPolls;
    }
}
