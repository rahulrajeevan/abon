package ru.macrobit.abonnews.controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceID;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.loader.AddDataRequest;
import ru.macrobit.abonnews.model.Ads;
import ru.macrobit.abonnews.model.PushReg;
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
        if (loadCookieFromSharedPreferences(Values.COOKIES, prefs) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Saving cookies in SharedPreferences
     * @param key
     * @param cookieStore
     * @param pref
     */

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

    /**
     * Load cookies from SharedPreferences
     * @param key key in SharedPreferences
     * @param pref SharedPreferences
     * @return cookies
     */

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

    /**
     * Delete cookies from SharedPreferences
     * @param context
     */

    public static void deleteCookies(Context context) {
        SharedPreferences prefs = getPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Values.COOKIES);
        editor.remove(Values.TOKEN);
        editor.commit();
    }

    /**
     * Checking internet state
     * @param context
     * @return true if connected
     */

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

    public static String convertToHex(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String getHtmlData(String bodyHTML, Activity activity) {
        int width = (int)(getScreenWidth(activity) - convertDpToPixel(20, activity));
        int heigth = (int)(width*0.45);
        String head = "<html><head> " +
                "<style> " +
//                "p {text-align: justify !important;}" +
                "img {max-width:100%%; height:auto !important;width:auto !important; visibility: visible !important;} " +
                ".wp-video {height:"+ heigth + "px  !important; width:100%% !important; visibility: visible !important;} " +
                ".wp-video-shortcode {height:" + heigth + "px  !important; width:100% !important; visibility: visible !important;} " +
                "audio {visibility: visible !important;} " +
                "iframe {height:"+ heigth + "px !important; width:100%% !important; visibility: visible !important;} " +
                "</style>" +
                "</head><body style='margin:0; '>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    private static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static Ads getAd(int pid, Context context) {
        String json = Utils.loadFromSharedPreferences(Values.ADS_PREF, getPrefs(context));
        Ads[] ads = GsonUtils.fromJson(json, Ads[].class);
        ArrayList<Ads> arrayList = new ArrayList<>();
        for (Ads a:ads) {
            if (a.getPid() == pid) {
                arrayList.add(a);
            }
        }
        Random rnd = new Random();
        int idx = rnd.nextInt(arrayList.size());
        return arrayList.get(idx);
    }

    public static void setDeviceToken(Context context, String deviceToken) {
        SharedPreferences sp = getPrefs(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Values.PREF_DEVICE_TOKEN, deviceToken);
        editor.commit();
    }

    public static String getDeviceToken(Context context) {
        SharedPreferences sp = getPrefs(context);
        return sp.getString(Values.PREF_DEVICE_TOKEN, null);
    }

    public static boolean isDeviceTokenSent(Context context) {
        SharedPreferences sp = getPrefs(context);
        return sp.getBoolean(Values.PREF_DEVICE_TOKEN_SENT, false);
    }

    public static void setDeviceTokenSend(Context context, boolean sent) {
        SharedPreferences sp = getPrefs(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Values.PREF_DEVICE_TOKEN_SENT, sent);
        editor.commit();
    }

    public static String createDeviceToken(Context context) throws IOException {
        String token = getDeviceToken(context);
        if(token == null) {
                token = InstanceID.getInstance(context).getToken(Values.SENDER_ID, Values.SCOPE, null);
            setDeviceToken(context, token);
        }
        return token;
    }

    public static void createAndSendDeviceToken(final Context context) {
        if(isDeviceTokenSent(context)) {
            return;
        }

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    return createDeviceToken(context);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s != null) {
                    PushReg pushReg = new PushReg(s);
                    String json = GsonUtils.toJson(pushReg);
                    new AddDataRequest(null, null, json).execute(Values.PUSH);
                } else {
                }
            }
        }.execute();
    }
}
