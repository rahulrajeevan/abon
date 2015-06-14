package ru.macrobit.abonnews.controller;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.model.ShortCookie;

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

    public static void saveCookieToSharedPreferences(String key, CookieStore cookieStore,
                                                          SharedPreferences pref) {
        List<Cookie> list = cookieStore.getCookies();
        Set<ShortCookie> cookies = new HashSet<>();
        for (Cookie c:list) {
            cookies.add(new ShortCookie(c.getName(), c.getValue()));
        }
        SharedPreferences.Editor edit = pref.edit();
        String s = GsonUtils.toJson(cookies);
        edit.putString(key, s);
        edit.commit();
    }

    public static BasicClientCookie[] loadCookieFromSharedPreferences(String key,
                                                     SharedPreferences pref) {
        String s = pref.getString(key, null);
        ShortCookie[] cookies = GsonUtils.fromJson(s, ShortCookie.class);
        BasicClientCookie[] basicClientCookies = new BasicClientCookie[cookies.length];
        for (int i = 0; i<cookies.length; i++) {
            basicClientCookies[i] = new BasicClientCookie(cookies[i].getKey(), cookies[i].getValue());
        }
        return basicClientCookies;
    }
}