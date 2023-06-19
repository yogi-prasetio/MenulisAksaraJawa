package com.android.menulisaksarajawa.ui.utils;

import android.content.Context;
import android.util.Log;

import androidx.multidex.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogUtils {
    private static final String LOG_TAG = "Tracing";
    private static char LOG_TYPE = 'v';

    public static void w(Object msg) {
        w(LOG_TAG, msg);
    }

    public static void w(String tag, Object msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'w');
    }

    public static void e(Object msg) {
        e(LOG_TAG, msg);
    }

    public static void e(String tag, Object msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'e');
    }

    public static void d(Object msg) {
        d(LOG_TAG, msg);
    }

    public static void d(String tag, Object msg) {
        d(tag, msg, null);
    }

    public static void d(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'd');
    }

    public static void i(Object msg) {
        i(LOG_TAG, msg);
    }

    public static void i(String tag, Object msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'i');
    }

    public static void v(Object msg) {
        v(LOG_TAG, msg);
    }

    public static void v(String tag, Object msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'v');
    }

    private static void log(String tag, String msg, Throwable tr, char level) {
        if (BuildConfig.DEBUG) {
            char c;
            if ('e' == level) {
                c = LOG_TYPE;
                if ('e' == c || 'v' == c) {
                    Log.e(tag, createMessage(msg), tr);
                    return;
                }
            }
            if ('w' == level) {
                c = LOG_TYPE;
                if ('w' == c || 'v' == c) {
                    Log.w(tag, createMessage(msg), tr);
                    return;
                }
            }
            if ('d' == level) {
                c = LOG_TYPE;
                if ('d' == c || 'v' == c) {
                    Log.d(tag, createMessage(msg), tr);
                    return;
                }
            }
            if ('i' == level) {
                c = LOG_TYPE;
                if ('d' == c || 'v' == c) {
                    Log.i(tag, createMessage(msg), tr);
                    return;
                }
            }
            Log.v(tag, createMessage(msg), tr);
        }
    }

    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        int length = sts.length;
        int i = 0;
        while (i < length) {
            StackTraceElement st = sts[i];
            if (st.isNativeMethod() || st.getClassName().equals(Thread.class.getName()) || st.getFileName().equals("LogUtils.java")) {
                i++;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[");
                stringBuilder.append(Thread.currentThread().getName());
                stringBuilder.append("(");
                stringBuilder.append(Thread.currentThread().getId());
                stringBuilder.append("): ");
                stringBuilder.append(st.getFileName());
                stringBuilder.append(":");
                stringBuilder.append(st.getLineNumber());
                stringBuilder.append("]");
                return stringBuilder.toString();
            }
        }
        return null;
    }

    private static String createMessage(String msg) {
        String functionName = getFunctionName();
        if (functionName == null) {
            return msg;
        }
        StringBuilder message = new StringBuilder();
        message.append(functionName);
        message.append(" - ");
        message.append(msg);
        return message.toString();
    }

    public static void logToFile(Context context, String text) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getFilesDir());
        stringBuilder.append(File.separator);
        stringBuilder.append("log.file");
        File logFile = new File(stringBuilder.toString());
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}