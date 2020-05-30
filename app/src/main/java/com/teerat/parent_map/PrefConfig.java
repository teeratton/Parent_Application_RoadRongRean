package com.teerat.parent_map;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefConfig {

    private static final String MY_PREFERENCE_NAME = "com.teerat.parent_map";

    public static void saveStudentIdPref(Context context, int id){
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("student_id",id);
        editor.apply();
    }

    public static int loadIdFromPref(Context context){
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME,Context.MODE_PRIVATE);
        return pref.getInt("student_id",0);
    }
}
