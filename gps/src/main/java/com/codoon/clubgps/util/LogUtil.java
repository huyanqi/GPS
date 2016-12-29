/**
 *
 */
package com.codoon.clubgps.util;

import android.util.Log;


/**
 * 日志管理
 *
 * @author lorcan
 */
public final class LogUtil {


    // 日志级别开关，可分别开启和关闭
    private static boolean TOGGLE_ERROR = true;//BuildConfig.LEO_DEBUG;
    private static boolean TOGGLE_WARN = true;//BuildConfig.LEO_DEBUG;
    private static boolean TOGGLE_INFO = true;//BuildConfig.LEO_DEBUG;
    private static boolean TOGGLE_DEBUG = true;//BuildConfig.LEO_DEBUG;
    private static boolean TOGGLE_VERBOSE = true;//BuildConfig.LEO_DEBUG;

    /**
     * 作者开关，如果开启则日志tag为作者名；如果关闭，则优先使用传递的tag值，tag为null使用作者名
     */
    private static boolean AUTHOR_TOGGLE = true;

    /**
     * 功能开关，如果开启则日志msg前加上模块名；如果关闭，则不加
     */
    private static boolean MODULE_TOGGLE = true;

    public static enum LogModule {
        DEFAULT() {
            @Override
            public String getModule() {
                return "默认";
            }

            @Override
            public boolean isToggleOn() {
                return true;
            }
        },

        DEFAULT_DATABASE() {
            @Override
            public String getModule() {
                return "数据库操作";
            }

            @Override
            public boolean isToggleOn() {
                return true;
            }
        },

        DEFAULT_NETWORK() {
            @Override
            public String getModule() {
                return "网络操作";
            }

            @Override
            public boolean isToggleOn() {
                return true;
            }
        },

        CAMERA() {
            @Override
            public String getModule() {
                return "拍照";
            }

            @Override
            public boolean isToggleOn() {
                return true;
            }
        },;

        /**
         * 获取日志输出对应的模块名
         *
         * @return
         */
        public abstract String getModule();

        /**
         * 是否开启日志打印
         *
         * @return
         */
        public abstract boolean isToggleOn();
    }

    ;

    /**
     * Constructor.
     */
    private LogUtil() {
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        if (TOGGLE_ERROR && LogModule.DEFAULT.isToggleOn()) {
            String msgStr = MODULE_TOGGLE ? "[" + LogModule.DEFAULT.getModule() + "] " + msg : msg;

            Log.e(tag, msgStr);
        }
    }

    /**
     * Send an {@link Log#WARN} log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        if (TOGGLE_WARN && LogModule.DEFAULT.isToggleOn()) {
            String msgStr = MODULE_TOGGLE ? "[" + LogModule.DEFAULT.getModule() + "] " + msg : msg;

            Log.w(tag, msgStr);
        }
    }

    /**
     * Send an {@link Log#INFO} log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        if (TOGGLE_INFO && LogModule.DEFAULT.isToggleOn()) {
            String msgStr = MODULE_TOGGLE ? "[" + LogModule.DEFAULT.getModule() + "] " + msg : msg;

            Log.i(tag, msgStr);
        }
    }

    /**
     * Send an {@link Log#DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if (TOGGLE_DEBUG && LogModule.DEFAULT.isToggleOn()) {
            String msgStr = MODULE_TOGGLE ? "[" + LogModule.DEFAULT.getModule() + "] " + msg : msg;

            Log.d(tag, msgStr);
        }
    }

    /**
     * Send an {@link Log#VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        if (TOGGLE_VERBOSE && LogModule.DEFAULT.isToggleOn()) {
            String msgStr = MODULE_TOGGLE ? "[" + LogModule.DEFAULT.getModule() + "] " + msg : msg;

            Log.v(tag, msgStr);
        }
    }

}
