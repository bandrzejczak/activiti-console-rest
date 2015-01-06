package pl.edu.pw.ii.bpmConsole.test;

import org.assertj.core.api.Condition;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MatchesPredicate<T> extends Condition<List<T>> {

    private final Predicate<T>[] predicates;

    private MatchesPredicate(Predicate<T>[] predicate) {
        this.predicates = predicate;
    }

    public static <T> MatchesPredicate anyMatching(Predicate<T>... predicates) {
        return new MatchesPredicate<>(predicates);
    }

    @Override
    public boolean matches(List<T> value) {
        return Arrays.stream(predicates).allMatch(p -> value.stream().anyMatch(p));
    }
}
