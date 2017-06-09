package com.novoda.utils;


import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class NoPlayerLog {

    private static final String TAG = "No-Player";
    private static final int DEPTH = 5;
    private static final int CLASS_SUFFIX = 5;

    private static boolean isEnabled = true;

    public static void setLoggingEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private static String logMessage(String message, Throwable throwable) {
        String detailedMessage = getDetailedLog(message);
        if (throwable != null) {
            detailedMessage += "\n" + getStackTraceString(throwable);
        }
        return detailedMessage;
    }

    private static String getDetailedLog(String message) {
        Thread current = Thread.currentThread();
        final StackTraceElement trace = current.getStackTrace()[DEPTH];
        final String filename = trace.getFileName();
        return "[" + current.getName() + "][" + filename.substring(0, filename.length() - CLASS_SUFFIX) + "."
                + trace.getMethodName() + ":" + trace.getLineNumber() + "] " + message;
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
