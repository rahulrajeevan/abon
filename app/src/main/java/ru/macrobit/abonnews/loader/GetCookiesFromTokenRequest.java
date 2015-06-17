package ru.macrobit.abonnews.loader;

import android.os.AsyncTask;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.macrobit.abonnews.OnAutorizationTaskCompleted;
import ru.macrobit.abonnews.OnTaskCompleted;

/**
 * Created by Ghost Surfer on 17.06.2015.
 */
public class GetCookiesFromTokenRequest  extends AsyncTask<String, String, CookieStore>  {
    private String result;
    private OnAutorizationTaskCompleted callback;
    CookieStore mCookieStore;

    public GetCookiesFromTokenRequest(OnAutorizationTaskCompleted  callback) {
        this.callback = callback;
    }

    @Override
    protected CookieStore doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpGet httpGet = new HttpGet(url.toString());
            DefaultHttpClient client = new DefaultHttpClient();

            mCookieStore = new BasicCookieStore();
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);

            HttpResponse response = client.execute(httpGet, ctx);
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
        return mCookieStore;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(CookieStore result) {
        if (callback != null) {
            callback.onAutorizationTaskCompleted(result);
        }
    }
}
