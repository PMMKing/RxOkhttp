package com.yuan.library;

import android.util.Log;

/**
 * Created by shucheng.qu on 2018/7/13
 */
public class HttpLogUtils {
    public static boolean debug = false;

    public static void d(String log) {
        d("RxOkhttp", log);
    }

    public static void d(String tag, String log) {
        if (debug) {
            Log.d(tag, log);
        }
    }

    public static void e(String log) {
        e("RxOkhttp", log);
    }

    public static void e(String tag, String log) {
        if (debug) {
            Log.e(tag, log);
        }
    }

}
