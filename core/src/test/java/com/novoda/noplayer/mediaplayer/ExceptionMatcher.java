package com.novoda.noplayer.mediaplayer;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

class ExceptionMatcher extends BaseMatcher<Exception> {

    private final String expectedMessage;

    private ExceptionMatcher(String expectedMessage) {
        this.expectedMessage = expectedMessage;
    }

    static ExceptionMatcher message(String message) {
        return new ExceptionMatcher(message);
    }

    @Override
    public boolean matches(Object o) {
        Exception exception = (Exception) o;
        return expectedMessage.equals(exception.getMessage());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("Exception: %s", expectedMessage));
    }
}
