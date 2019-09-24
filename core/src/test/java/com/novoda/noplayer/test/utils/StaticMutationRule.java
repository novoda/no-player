package com.novoda.noplayer.test.utils;

import org.junit.rules.ExternalResource;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.novoda.noplayer.test.utils.ReflectionFinalMutationUtils.findField;
import static com.novoda.noplayer.test.utils.ReflectionFinalMutationUtils.getFinalField;
import static com.novoda.noplayer.test.utils.ReflectionFinalMutationUtils.setFinalField;

/**
 * Allows mutating constants which are restored after the test is finished.
 * This makes sure that we can leave the static scope as it was before the test started.
 * <p>
 * To use it create a public static field in a test like:
 *
 * <code>
 *
 * @Rule public final StaticMutationRule mutations = new StaticMutationRule();
 * </code>
 */
@SuppressWarnings({"UseOfSystemOutOrSystemErr", "JavaDoc"})
public class StaticMutationRule extends ExternalResource {

    private final List<Mutation> mutations;

    public StaticMutationRule(Mutation... mutations) {
        this.mutations = Arrays.asList(mutations);
    }

    @Override
    protected void before() {
        for (Mutation mutation : mutations) {
            mutation.mutate();
        }
    }

    @Override
    protected void after() {
        for (Mutation mutation : mutations) {
            mutation.restore();
        }
    }

    private static void setFinalStatic(Field field, Object newValue) throws Exception {
        setFinalField(null, field, newValue);
    }

    private static Object getFinalStatic(Field field) throws Exception {
        return getFinalField(null, field);
    }

    public static class Mutation<T> {

        final Class<T> type;
        final String name;
        final Object newValue;
        Field field;
        Object oldValue;

        public static <T> Mutation<T> mutation(Class<T> type, String name, Object newValue) {
            return new Mutation<>(type, name, newValue);
        }

        private Mutation(Class<T> type, String name, Object newValue) {
            this.type = type;
            this.name = name;
            this.newValue = newValue;
        }

        private void mutate() {
            try {
                oldValue = getFinalStatic(field());
                setFinalStatic(field, newValue);
            } catch (Exception e) {
                System.err.println("Could not mutate field: " + e.getMessage());
            }
        }

        private void restore() {
            try {
                setFinalStatic(field(), oldValue);
            } catch (Exception e) {
                System.err.println("Could not restore field: " + e.getMessage());
            }
        }

        private Field field() throws Exception {
            if (field == null) {
                field = findField(type, name);
            }
            return field;
        }

    }

}
