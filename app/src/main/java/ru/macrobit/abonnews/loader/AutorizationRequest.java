package ru.macrobit.abonnews.loader;

import android.os.AsyncTask;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.macrobit.abonnews.OnTaskCompleted;

/**
 * Created by Comp on 05.06.2015.
 */
public class AutorizationRequest extends AsyncTask<String, String, String> {
    String mLogin;
    String mPass;
    private OnTaskCompleted callback;

    public AutorizationRequest(OnTaskCompleted callback, String login, String pass) {
        this.callback = callback;
        mLogin = login;
        mPass = pass;
    }

    public AutorizationRequest(String login, String pass) {
        mLogin = login;
        mPass = pass;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = null;
        try {
            HttpParams httpParams = new BasicHttpParams();
            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
            HttpProtocolParams.setHttpElementCharset(httpParams, "UTF-8");
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost post = new HttpPost(
                    urls[0]);
            int portOfProxy = android.net.Proxy.getDefaultPort();
            if (portOfProxy > 0) {
                HttpHost proxy = new HttpHost(
                        android.net.Proxy.getDefaultHost(), portOfProxy);
                client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                        proxy);
            }
            post.setHeader("Content-type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("log", mLogin));
            nameValuePairs.add(new BasicNameValuePair("pwd", mPass));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
            result = sb.toString();
        } catch (org.apache.http.client.ClientProtocolException e) {
            result = "ClientProtocolException: " + e.getMessage();
        } catch (IOException e) {
            result = "IOException: " + e.getMessage();
        } catch (Exception e) {
            result = "Exception: " + e.getMessage();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (callback != null) {
            callback.onTaskCompleted(result);
        }
        super.onPostExecute(result);
    }
}