package ru.macrobit.abonnews.loader;

import android.os.AsyncTask;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import ru.macrobit.abonnews.OnTaskCompleted;

public class AddDataRequest extends AsyncTask<String, String, String> {

    String result;
    private OnTaskCompleted callback;
    private CookieStore mCookies;
    private String mJson;

    public AddDataRequest(OnTaskCompleted callback, BasicClientCookie[] basicClientCookies, String json) {
        this.callback = callback;
        this.mJson = json;
        if (basicClientCookies != null && basicClientCookies.length > 0) {
            mCookies = new BasicCookieStore();
            for (int i = 0; i < basicClientCookies.length; i++) {
                mCookies.addCookie(basicClientCookies[i]);
            }
        }
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            HttpParams httpParams = new BasicHttpParams();
            HttpProtocolParams.setVersion(httpParams,
                    HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
            HttpProtocolParams.setHttpElementCharset(httpParams,
                    "UTF-8");
            DefaultHttpClient client = new DefaultHttpClient(httpParams);
            if (mCookies != null) {
                client.setCookieStore(mCookies);
            }
            HttpPost post = new HttpPost(urls[0]);
            int portOfProxy = android.net.Proxy.getDefaultPort();
            if (portOfProxy > 0) {
                HttpHost proxy = new HttpHost(
                        android.net.Proxy.getDefaultHost(), portOfProxy);
                client.getParams().setParameter(
                        ConnRoutePNames.DEFAULT_PROXY, proxy);
            }
            StringEntity ent = null;
            ent = new StringEntity(mJson);
            post.setEntity(ent);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(post);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity()
                            .getContent(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
            result = sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected void onPostExecute(String s) {
        if (callback != null) {
            callback.onTaskCompleted(result);
        }
        super.onPostExecute(result);
    }
}
