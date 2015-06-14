package ru.macrobit.abonnews.controller;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ru.macrobit.abonnews.Values;

/**
 * Created by Ghost Surfer on 14.06.2015.
 */
public class Utils {
    public static void saveToSharedPreferences(String key, String[] array, SharedPreferences pref) {
        SharedPreferences.Editor edit = pref.edit();
        Set<String> mySet = new HashSet<String>(Arrays.asList(array));
        edit.putStringSet(key, mySet);
        edit.commit();
    }


    public static void saveToSharedPreferences(String key, String value,
                                               SharedPreferences pref) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String loadFromSharedPreferences(String key,
                                                   SharedPreferences pref) {
        String s = pref.getString(key, null);
        return s;
    }

    public static String[] loadSetFromSharedPreferences(String key,
                                                   SharedPreferences pref) {
        Set<String> mySet = pref.getStringSet(key, null);
        String[] s = mySet.toArray(new String[mySet.size()]);
        return s;
    }

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(Values.PREF, Context.MODE_PRIVATE);
    }

}
