package utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class ExceptionMatcher extends BaseMatcher<Exception> {

    private final String expectedMessage;
    private final Class<? extends Exception> expectedExceptionClass;

    public static ExceptionMatcher matches(String message, Class<? extends Exception> expectedExceptionClass) {
        return new ExceptionMatcher(message, expectedExceptionClass);
    }

    private ExceptionMatcher(String expectedMessage, Class<? extends Exception> expectedExceptionClass) {
        this.expectedMessage = expectedMessage;
        this.expectedExceptionClass = expectedExceptionClass;
    }

    @Override
    public boolean matches(Object o) {
        Exception exception = (Exception) o;
        return expectedMessage.equals(exception.getMessage()) && exception.getClass().isAssignableFrom(expectedExceptionClass);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("<%s: %s>", expectedExceptionClass.getName(), expectedMessage));
    }
}
