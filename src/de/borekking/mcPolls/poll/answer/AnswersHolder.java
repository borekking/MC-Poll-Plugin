package de.borekking.mcPolls.poll.answer;

import java.util.Iterator;
import java.util.List;

public interface AnswersHolder<T extends StaticAnswer> extends Iterable<T> {

    List<T> getAnswers();

    default int size() {
        return this.getAnswers().size();
    }

    default String[] getAnswersArray() {
        return this.getAnswers().stream().map(StaticAnswer::getAnswer).toArray(String[]::new);
    }

    default T getAnswer(int i) {
        return this.getAnswers().get(i);
    }

    @Override
    default Iterator<T> iterator() {
        return this.getAnswers().iterator();
    }
}
