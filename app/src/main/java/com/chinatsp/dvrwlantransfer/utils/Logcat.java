package com.chinatsp.dvrwlantransfer.utils;

import android.util.Log;

/**
 * 日志管理类
 *
 * @author chenzuohua
 * created at 2019/4/8 11:40 AM
 */
public final class Logcat {
    private static final String TAG = "DvrWlanTransfer";
    private static boolean sPrintLog = true;

    private Logcat() {}

    public static void v() {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.v(TAG, "");
            } else {
                Log.v(TAG, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()");
            }
        }
    }

    public static void v(String msg) {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.v(TAG, msg);
            } else {
                Log.v(TAG, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()-----"
                        + msg);
            }
        }
    }

    public static void v(String tag, String msg) {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.v(tag, msg);
            } else {
                Log.v(tag, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()-----"
                        + msg);
            }
        }
    }

    public static void d() {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.d(TAG, "");
            } else {
                Log.d(TAG, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()");
            }
        }
    }

    public static void d(String msg) {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.d(TAG, msg);
            } else {
                Log.d(TAG, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()-----"
                        + msg);
            }
        }
    }

    public static void d(String tag, String msg) {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.d(tag, msg);
            } else {
                Log.d(tag, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()-----"
                        + msg);
            }
        }
    }

    public static void i() {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.i(TAG, "");
            } else {
                Log.i(TAG, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()");
            }
        }
    }

    public static void i(String msg) {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.i(TAG, msg);
            } else {
                Log.i(TAG, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()-----"
                        + msg);
            }
        }
    }

    public static void i(String tag, String msg) {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.i(tag, msg);
            } else {
                Log.i(tag, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()-----"
                        + msg);
            }
        }
    }

    public static void w() {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.w(TAG, "");
            } else {
                Log.w(TAG, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()");
            }
        }
    }

    public static void w(String msg) {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.w(TAG, msg);
            } else {
                Log.w(TAG, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()-----"
                        + msg);
            }
        }
    }

    public static void w(String tag, String msg) {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.w(tag, msg);
            } else {
                Log.w(tag, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()-----"
                        + msg);
            }
        }
    }

    public static void e() {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.e(TAG, "");
            } else {
                Log.e(TAG, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()");
            }
        }
    }

    public static void e(String msg) {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.e(TAG, msg);
            } else {
                Log.e(TAG, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()-----"
                        + msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (sPrintLog) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements == null || elements.length < 4) {
                Log.e(tag, msg);
            } else {
                Log.e(tag, elements[3].getFileName()// 文件名
                        + "("
                        + elements[3].getLineNumber()// 行号
                        + "):"
                        + elements[3].getMethodName()// 方法名
                        + "()-----"
                        + msg);
            }
        }
    }
}
