package com.novoda.noplayer.test.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Used to work with final fields in order to test against system APIs without adding unecessary
 * colaborators to production code.
 */
@SuppressWarnings({"WeakerAccess", "JavaReflectionMemberAccess", "unused"})
public class RelectionFinalMutationUtils {

    private RelectionFinalMutationUtils() {
        // Utils class
    }

    public static void setFinalField(Object subject, Field field, Object newValue) throws Exception {
        setAccessible(field).set(subject, newValue);
    }

    public static void setFinalField(Object subject, String name, Object newValue) throws Exception {
        setFinalField(subject, findField(subject.getClass(), name), newValue);
    }

    public static Object getFinalField(Object subject, Field field) throws Exception {
        return setAccessible(field).get(subject);
    }

    public static Object getFinalField(Object subject, String name) throws Exception {
        return getFinalField(subject, findField(subject.getClass(), name));
    }

    static Field setAccessible(Field field) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        return field;
    }

    static Field findField(Class<?> type, String name) throws Exception {
        Field field;
        try {
            field = type.getField(name);
        } catch (NoSuchFieldException e) {
            field = type.getDeclaredField(name);
        }
        return field;
    }

}
