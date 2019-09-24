package com.novoda.noplayer.test.utils;

import org.junit.rules.ExternalResource;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.novoda.noplayer.test.utils.ReflectionFinalMutationUtils.*;

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

    private final Map<Field, Object> fieldsMutated = new HashMap<>();

    @Override
    protected void after() {
        super.after();
        for (Map.Entry<Field, Object> entry : fieldsMutated.entrySet()) {
            try {
                Field field = entry.getKey();
                Object value = entry.getValue();
                setFinalStatic(field, value);
            } catch (Exception e) {
                System.err.println("Could not restore field: " + e.getMessage());
            }
        }
    }

    public <T> void mutateStatic(Class<T> type, String name, Object newValue) {
        try {
            Field field = type.getField(name);
            if (!fieldsMutated.containsKey(field)) {
                fieldsMutated.put(field, getFinalStatic(field));
            }
            setFinalStatic(field, newValue);
        } catch (Exception e) {
            System.err.println("Could not mutate field: " + e.getMessage());
        }
    }

    private static void setFinalStatic(Field field, Object newValue) throws Exception {
        setFinalField(null, field, newValue);
    }

    private static Object getFinalStatic(Field field) throws Exception {
        return getFinalField(null, field);
    }

}
