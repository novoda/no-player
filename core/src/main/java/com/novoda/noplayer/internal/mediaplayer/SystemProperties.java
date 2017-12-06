package com.novoda.noplayer.internal.mediaplayer;

import android.annotation.SuppressLint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class SystemProperties {

    private static final String SYSTEM_PROPERTIES_CLASS = "android.os.SystemProperties";
    private static final String SYSTEM_PROPERTIES_METHOD_GET = "get";

    private static final Object STATIC_CLASS_INSTANCE = null;

    @SuppressLint("PrivateApi") // This method uses reflection to call android.os.SystemProperties.get(String) since the class is hidden
    String get(String key) throws MissingSystemPropertiesException {
        try {
            Class<?> systemProperties = Class.forName(SYSTEM_PROPERTIES_CLASS);
            Method getMethod = systemProperties.getMethod(SYSTEM_PROPERTIES_METHOD_GET, String.class);
            return (String) getMethod.invoke(STATIC_CLASS_INSTANCE, key);
        } catch (ClassNotFoundException e) {
            throw new MissingSystemPropertiesException(e);
        } catch (NoSuchMethodException e) {
            throw new MissingSystemPropertiesException(e);
        } catch (InvocationTargetException e) {
            throw new MissingSystemPropertiesException(e);
        } catch (IllegalAccessException e) {
            throw new MissingSystemPropertiesException(e);
        }
    }

    static class MissingSystemPropertiesException extends Exception {
        MissingSystemPropertiesException(Exception e) {
            super(e);
        }
    }
}
