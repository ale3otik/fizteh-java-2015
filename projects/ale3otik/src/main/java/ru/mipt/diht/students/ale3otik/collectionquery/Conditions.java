package ru.mipt.diht.students.ale3otik.collectionquery;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Where clause conditions.
 */
public class Conditions<T> {

    /**
     * Matches string result of expression against regexp pattern.
     *
     * @param expression expression result to match
     * @param regexp     pattern to match to
     * @param <T>        source object type
     * @return
     */
    public static <T> Predicate<T> rlike(Function<T, String> expression, String regexp) {
        return elem -> expression.apply(elem).matches(regexp);
    }

    /**
     * Matches string result of expression against SQL like pattern.
     *
     * @param expression expression result to match
     * @param pattern    pattern to match to
     * @param <T>        source object type
     * @return
     */
    public static <T> Predicate<T> like(Function<T, String> expression, String pattern) {
        return e-> expression.apply(e).equals(pattern);
    }

}
