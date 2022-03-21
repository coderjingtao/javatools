package main.core.lang;

@FunctionalInterface
public interface Matcher<T> {

    /**
     * instance t is matched or not
     * @param t object / instance
     * @return matched or not
     */
    boolean match(T t);
}
