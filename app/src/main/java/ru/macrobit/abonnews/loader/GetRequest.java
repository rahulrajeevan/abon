package ru.macrobit.abonnews.loader;

/**
 * Created by Comp on 30.05.2015.
 */

import android.os.AsyncTask;
import android.webkit.CookieManager;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.macrobit.abonnews.OnTaskCompleted;

public class GetRequest extends AsyncTask<String, String, String> {
    private String result;
    private OnTaskCompleted callback;
    private CookieStore mCookies;

    public GetRequest(OnTaskCompleted callback) {
        this.callback = callback;
    }

    public GetRequest(OnTaskCompleted callback, CookieStore cookieStore) {
        this.callback = callback;
        this.mCookies = cookieStore;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpGet httpGet = new HttpGet(url.toString());
            DefaultHttpClient client = new DefaultHttpClient();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            if (mCookies != null) {
                client.setCookieStore(mCookies);
            }
            HttpResponse response = client.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "WINDOWS-1251"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
            result = sb.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {
        if (callback != null) {
            callback.onTaskCompleted(result);
        }
        super.onPostExecute(result);
    }
}