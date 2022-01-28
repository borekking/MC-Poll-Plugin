package de.borekking.mcPolls.data;

import de.borekking.mcPolls.poll.PollManager;

public class DataManager {

    private final PollFileManager pollFileManager;

    private final PollManager pollManager;

    public DataManager(PollManager pollManager) {
        this.pollManager = pollManager;

        this.pollFileManager = new PollFileManager();
    }

    // Load Active Polls and Ended Polls from a file
    public void load() {
        this.pollFileManager.loadActivePolls().forEach(this.pollManager::startPoll);
        this.pollFileManager.loadEndedPolls().forEach(this.pollManager::addEndedPoll);
    }

    // Save Active Polls and Ended Polls into a file
    public void save() {
        // Clear PollFileManager to prevent doubling Polls after changing from active to ended
        this.pollFileManager.clear();

        this.pollFileManager.saveActivePolls(this.pollManager.getActivePolls());
        this.pollFileManager.saveEndedPolls(this.pollManager.getEndedPolls());
        this.pollFileManager.save();
    }
}
