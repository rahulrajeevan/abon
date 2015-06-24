package ru.macrobit.abonnews.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.model.ShortCookie;

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

    public static boolean isCookiesExist(Context context) {
        SharedPreferences prefs = getPrefs(context);
        if (loadCookieFromSharedPreferences(Values.COOKIES, prefs) != null || loadFromSharedPreferences(Values.TOKEN, prefs) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void saveCookieToSharedPreferences(String key, CookieStore cookieStore,
                                                          SharedPreferences pref) {
        List<Cookie> list = cookieStore.getCookies();
        Set<ShortCookie> cookies = new HashSet<>();
        for (Cookie c:list) {
            cookies.add(new ShortCookie(c.getName(), c.getValue(), c.getDomain(), c.getExpiryDate()));
        }
        SharedPreferences.Editor edit = pref.edit();
        String s = GsonUtils.toJson(cookies);
        edit.putString(key, s);
        edit.commit();
    }

    public static BasicClientCookie[] loadCookieFromSharedPreferences(String key,
                                                     SharedPreferences pref) {
        String s = pref.getString(key, null);
        if (s != null) {
            ShortCookie[] cookies = GsonUtils.fromJson(s, ShortCookie[].class);
            BasicClientCookie[] basicClientCookies = new BasicClientCookie[cookies.length];
            for (int i = 0; i < cookies.length; i++) {
                basicClientCookies[i] = new BasicClientCookie(cookies[i].getKey(), cookies[i].getValue());
                basicClientCookies[i].setDomain(cookies[i].getDomain());
                basicClientCookies[i].setExpiryDate(cookies[i].getExpiryDate());
            }
            return basicClientCookies;
        } else {
            return null;
        }
    }

    public static void deleteCookies(Context context) {
        SharedPreferences prefs = getPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Values.COOKIES);
        editor.remove(Values.TOKEN);
        editor.commit();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return true;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        Toast.makeText(context, context.getString(R.string.not_connect), Toast.LENGTH_LONG).show();
        return false;
    }
}
