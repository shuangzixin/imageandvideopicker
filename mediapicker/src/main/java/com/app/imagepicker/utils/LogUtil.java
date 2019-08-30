package com.app.imagepicker.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import static java.lang.String.format;
import static java.util.Locale.getDefault;

/**
 * Created by stefan on 2017/8/10.
 */
public class LogUtil {

    static boolean DEBUG = Boolean.parseBoolean("true");

    private static final String TAG = "LogUtil";

//    static String className;//class name
//    static String methodName;//method name
//    static int lineNumber;//the number of rows

    /**
     * Whether the judgment is in the Debug mode.
     *
     * @return Whether or not that it is in the Debug mode.
     */
    public static boolean isDebuggable() {
        return DEBUG;
    }


    public static void setDebuggagle(boolean debug) {
        DEBUG = debug;
    }

    /*
     * Create log information, include methodName, className, lineNumber and custom content
     * @param log custom content
     * @return
     */
    private static String createLog(String log, String methodName, String className, int lineNumber) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    /**
     * Send an ERROR log message.
     *
     * @param tag     The tag to check.
     * @param message The message you would like logged.
     */
    public static void e(String tag, String message) {
        if (!isDebuggable())
            return;

        // Throwable instance must be created before any methods
        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        Log.e(tag, createLog(message, methodName, className, lineNumber));
    }

    /**
     * Send an ERROR log message.
     *
     * @param message The message you would like logged.
     */
    public static void e(String message) {
        if (!isDebuggable())
            return;

        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        Log.e(className, createLog(message, methodName, className, lineNumber));
    }

    /**
     * Send an INFO log message.
     *
     * @param tag     The tag to check.
     * @param message The message you would like logged.
     */
    public static void i(String tag, String message) {
        if (!isDebuggable())
            return;

        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        Log.i(tag, createLog(message, methodName, className, lineNumber));
    }

    /**
     * Send an INFO log message.
     *
     * @param message The message you would like logged.
     */
    public static void i(String message) {
        if (!isDebuggable())
            return;

        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        Log.i(className, createLog(message, methodName, className, lineNumber));
    }

    /**
     * Send a DEBUG log message.
     *
     * @param tag     The tag to check.
     * @param message The message you would like logged.
     */
    public static void d(String tag, String message) {
        if (!isDebuggable())
            return;

        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        Log.d(tag, createLog(message, methodName, className, lineNumber));
    }

    /**
     * Send a DEBUG log message.
     *
     * @param message The message you would like logged.
     */
    public static void d(String message) {
        if (!isDebuggable())
            return;

        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        Log.d(className, createLog(message, methodName, className, lineNumber));
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param tag     The tag to check.
     * @param message The message you would like logged.
     */
    public static void v(String tag, String message) {
        if (!isDebuggable())
            return;

        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        Log.v(tag, createLog(message, methodName, className, lineNumber));
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param message The message you would like logged.
     */
    public static void v(String message) {
        if (!isDebuggable())
            return;

        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        Log.v(className, createLog(message, methodName, className, lineNumber));
    }

    /**
     * Send a WARN log message.
     *
     * @param tag     The tag to check.
     * @param message The message you would like logged.
     */
    public static void w(String tag, String message) {
        if (!isDebuggable())
            return;

        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        Log.w(tag, createLog(message, methodName, className, lineNumber));
    }

    /**
     * Send a WARN log message.
     *
     * @param message The message you would like logged.
     */
    public static void w(String message) {
        if (!isDebuggable())
            return;

        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        Log.w(className, createLog(message, methodName, className, lineNumber));
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen.
     * The error will always be logged at level ASSERT with the call stack.
     * Depending on system configuration, a report may be added to the
     *
     * @param tag     The tag to check.
     * @param message The message you would like logged.
     */
    public static void wtf(String tag, String message) {
        if (!isDebuggable())
            return;

        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            Log.wtf(tag, createLog(message, methodName, className, lineNumber));
        }
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen.
     * The error will always be logged at level ASSERT with the call stack.
     * Depending on system configuration, a report may be added to the
     *
     * @param message The message you would like logged.
     */
    public static void wtf(String message) {
        if (!isDebuggable())
            return;

        StackTraceElement[] sElements = new Throwable().getStackTrace();
        String methodName = sElements[1].getMethodName();
        String className = sElements[1].getFileName();
        int lineNumber = sElements[1].getLineNumber();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            Log.wtf(className, createLog(message, methodName, className, lineNumber));
        }
    }

    public static void log(String message) {
        if (TextUtils.isEmpty(message)) return;
        if (DEBUG) {
            Log.i(TAG, message);
        }
    }

    public static void log(String message, Object... args) {
        log(format(getDefault(), message, args));
    }

    public static void log(Throwable throwable) {
        Log.w(TAG, throwable);
    }

    public static String formatStr(String str, Object... args) {
        return format(getDefault(), str, args);
    }
}
