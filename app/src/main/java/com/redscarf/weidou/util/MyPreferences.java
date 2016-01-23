package com.redscarf.weidou.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 系统属性存储
 * @author yeahwang
 *
 */
public class MyPreferences {

    private static final String MY_PREFERENCES = "app_config";

    /**
     * 判断是否为第一次运行
     * @param context
     * @return
     */
    public static boolean isFirst(Context context){
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first", true);
        if(first){
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.commit();
        }
        return first;
    }

    /**
     * @return SharedPreference实例
     */
    public static SharedPreferences getAppSharedPerences() {
        SharedPreferences reader = GlobalApplication.getAppContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        return reader;
    }

    public static String getAppPerenceAttribute(String key){
        SharedPreferences tmp_reader = MyPreferences.getAppSharedPerences();
        String value = tmp_reader.getString(key, "");
        return value;
    }

    public static void setAppPerenceAttribute(String key, String value){
        SharedPreferences tmp_writer = MyPreferences.getAppSharedPerences();
        SharedPreferences.Editor editor = tmp_writer.edit();
        editor.putString(key,value);
        editor.commit();
    }

}
