package de.borekking.mcPolls.poll;

import de.borekking.mcPolls.poll.answer.Answer;
import de.borekking.mcPolls.poll.answer.AnswersHolder;
import de.borekking.mcPolls.poll.answer.StaticAnswer;
import de.borekking.mcPolls.poll.types.AbstractPoll;
import de.borekking.mcPolls.poll.types.ActivePoll;
import de.borekking.mcPolls.poll.types.EndedPoll;

import de.borekking.mcPolls.util.JavaUtils;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PollSerializer {

    /*
     * Utility class for loading and saving Polls
     *
     */

    // </Serializing Polls>
    public static ActivePoll deserializeActivePoll(Map<?, ?> map) {
        // Get Answers
        Map<?, ?> answerMap = getInnerMap(map, "answers");
        List<Answer> answers = deserializeAnswerMap(answerMap).stream().map(Answer::new).collect(Collectors.toList());

        // Get Votes
        Map<?, ?> votesMap = getInnerMap(map, "votes");
        Map<UUID, Integer> votes = PollSerializer.deserializeVotes(votesMap);

        return new ActivePoll((String) map.get("name"), (String) map.get("question"), answers, votes, JavaUtils.getLongSave(map.get("start")), JavaUtils.getLongSave(map.get("length")));
    }

    public static EndedPoll deserializeEndedPoll(Map<?, ?> map) {
        // Get Answers
        Map<?, ?> answerMap = getInnerMap(map, "answers");
        List<StaticAnswer> answers = deserializeAnswerMap(answerMap);

        return new EndedPoll((String) map.get("name"), (String) map.get("question"), answers, JavaUtils.getLongSave(map.get("start")), JavaUtils.getLongSave(map.get("length")));
    }
    // </Deserializing Polls>

    // <Serializing Polls>
    public static <T extends StaticAnswer> Map<String, Object> serializeAbstractPoll(AbstractPoll<T> poll) {
        // Init HashMap
        Map<String, Object> map = new HashMap<>();

        // Set Main fields
        map.put("start", poll.getStart());
        map.put("length", poll.getLength());
        map.put("question", poll.getQuestion());
        map.put("name", poll.getName());

        // Set Answers
        map.put("answers", serializeAnswerMap(poll));

        // If ActiveVote set votes
        if (poll instanceof ActivePoll) map.put("votes", serializeVotes(((ActivePoll) poll).getVotes()));

        return map;
    }
    // </Serializing Polls>

    // <Answers>
    private static <T extends AnswersHolder<?>> Map<String, Object> serializeAnswerMap(T holder) {
        Map<String, Object> answersMap = new HashMap<>();

        // Fill answers
        for (int i = 0; i < holder.size(); i++) {
            StaticAnswer answer = holder.getAnswers().get(i);
            Map<String, Object> innerMap = new HashMap<>();

            innerMap.put("answer", answer.getAnswer());
            innerMap.put("amount", answer.getAmount());

            answersMap.put(String.valueOf(i), innerMap);
        }

        return answersMap;
    }

    private static List<StaticAnswer> deserializeAnswerMap(Map<?, ?> map) {
        List<StaticAnswer> answersList = new ArrayList<>();

        // Fill answers
        for (Object key : map.keySet()) {
            Map<?, ?> innerMap = getInnerMap(map, key);

            String answer = (String) innerMap.get("answer");
            int amount = (Integer) innerMap.get("amount");

            answersList.add(new StaticAnswer(answer, amount));
        }

        return answersList;
    }
    // </Answers>

    // <Votes>
    private static Map<UUID, Integer> deserializeVotes(Map<?, ?> votesMap) {
        //                              Map to HashMap              key to UUID;          key to amount (int) from provides votesMap
        return votesMap.keySet().stream().collect(Collectors.toMap(k -> UUID.fromString((String) k), k -> (Integer) votesMap.get(k)));
    }

    private static Map<?, ?> serializeVotes(Map<UUID, Integer> votesMap) {
        //                         Map to HashMap                 key to key;    key to amount (int)
        return votesMap.keySet().stream().collect(Collectors.toMap(UUID::toString, votesMap::get));
    }


    // "Util"
    private static Map<?, ?> getInnerMap(Map<?, ?> root, Object key) {
        return ((MemorySection) root.get(key)).getValues(false);
    }
}
