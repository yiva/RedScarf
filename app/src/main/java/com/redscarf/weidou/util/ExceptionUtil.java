package com.redscarf.weidou.util;

import android.util.Log;

/**
 * Created by yeahwang on 2015/10/27.
 */
public class ExceptionUtil {

    public static void printAndRecord(String tag,Exception ex) {
        Log.e(tag, ex.getMessage(), ex);
    }
}
