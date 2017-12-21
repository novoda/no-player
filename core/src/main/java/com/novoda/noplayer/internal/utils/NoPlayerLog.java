package com.novoda.noplayer.internal.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

@SuppressWarnings("PMD.ShortMethodName")    // This is a logger class, the logging methods are 1-letter
public final class NoPlayerLog {

    private static final String TAG = "No-Player";
    private static final int DEPTH = 5;
    private static final int CLASS_SUFFIX = 5;
    private static final String DETAILED_LOG_TEMPLATE = "[%s][%s.%s:%d] %s";

    private static boolean isEnabled = true;

    private NoPlayerLog() {
        // Not instantiable
    }

    public static void setLoggingEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private static String logMessage(String message, Throwable throwable) {
        StringBuilder detailedMessage = new StringBuilder(getDetailedLog(message));
        if (throwable != null) {
            detailedMessage.append('\n').append(getStackTraceString(throwable));
        }
        return detailedMessage.toString();
    }

    private static String getDetailedLog(String message) {
        Thread current = Thread.currentThread();
        final StackTraceElement trace = current.getStackTrace()[DEPTH];
        final String filename = trace.getFileName();
        return String.format(Locale.US,
                DETAILED_LOG_TEMPLATE,
                current.getName(),
                filename.substring(0, filename.length() - CLASS_SUFFIX),
                trace.getMethodName(),
                trace.getLineNumber(),
                message);
    }

    private static String getStackTraceString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
            return sw.toString().trim();
        } finally {
            pw.close();
        }
    }

    public static void d(String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.d(TAG, logMessage(msg, null));
    }

    public static void d(Throwable throwable, String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.d(TAG, logMessage(msg, throwable));
    }

    public static void e(String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.e(TAG, logMessage(msg, null));
    }

    public static void e(Throwable throwable, String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.e(TAG, logMessage(msg, throwable));
    }

    public static void i(String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.i(TAG, logMessage(msg, null));
    }

    public static void i(Throwable throwable, String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.i(TAG, logMessage(msg, throwable));
    }

    public static void v(String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.v(TAG, logMessage(msg, null));
    }

    public static void v(Throwable throwable, String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.v(TAG, logMessage(msg, throwable));
    }

    public static void w(String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.w(TAG, logMessage(msg, null));
    }

    public static void w(Throwable throwable, String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.w(TAG, logMessage(msg, throwable));
    }

    public static void wtf(String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.wtf(TAG, logMessage(msg, null));
    }

    public static void wtf(Throwable throwable) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.wtf(TAG, logMessage("", throwable));
    }

    public static void wtf(Throwable throwable, String msg) {
        if (!isEnabled) {
            return;
        }
        android.util.Log.wtf(TAG, logMessage(msg, throwable));
    }
}
