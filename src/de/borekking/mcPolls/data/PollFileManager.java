package de.borekking.mcPolls.data;

import de.borekking.mcPolls.poll.types.AbstractPoll;
import de.borekking.mcPolls.poll.types.ActivePoll;
import de.borekking.mcPolls.poll.types.EndedPoll;
import de.borekking.mcPolls.poll.PollSerializer;
import de.borekking.mcPolls.poll.answer.StaticAnswer;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PollFileManager extends FileHolder {

    public static final String ACTIVE_POLLS_SECTION = "activePolls", ENDED_POLLS_SECTION = "endedPolls";

    public PollFileManager() {
        super("data/polls.yml");
    }

    // Loading
    public List<ActivePoll> loadActivePolls() {
        return this.loadPolls(ACTIVE_POLLS_SECTION, PollSerializer::deserializeActivePoll);
    }

    public List<EndedPoll> loadEndedPolls() {
        return this.loadPolls(ENDED_POLLS_SECTION, PollSerializer::deserializeEndedPoll);
    }

    private <T extends AbstractPoll<? extends StaticAnswer>> List<T> loadPolls(String configSection, Function<Map<?, ?>, T> pollProvider) {
        if (!this.config.contains(configSection)) return new ArrayList<>();

        ConfigurationSection section = this.config.getConfigurationSection(configSection);
        Map<String, Object> values = section.getValues(false);

        List<T> list = new ArrayList<>();

        for (String key : values.keySet()) {
            Map<?, ?> innerMap = ((ConfigurationSection) values.get(key)).getValues(false);
            T poll = pollProvider.apply(innerMap);
            list.add(poll);
        }

        return list;
    }

    // Saving
    public void saveActivePolls(List<ActivePoll> polls) {
        this.savePolls(ACTIVE_POLLS_SECTION, polls, PollSerializer::serializeAbstractPoll);
    }

    public void saveEndedPolls(List<EndedPoll> polls) {
        this.savePolls(ENDED_POLLS_SECTION, polls, PollSerializer::serializeAbstractPoll);
    }

    private <T extends AbstractPoll<? extends StaticAnswer>> void savePolls(String configSection, List<T> polls, Function<T, Map<String, Object>> deserializeProvider) {
        int i = 0;
        for (T poll : polls) {
            Map<String, Object> map = deserializeProvider.apply(poll);
            this.config.createSection(configSection + "." + i++, map);
        }
    }
}
