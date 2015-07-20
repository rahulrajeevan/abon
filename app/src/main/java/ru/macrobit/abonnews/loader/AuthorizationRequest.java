package ru.macrobit.abonnews.loader;

import android.os.AsyncTask;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.macrobit.abonnews.OnAuthorizationTaskCompleted;

public class AuthorizationRequest extends AsyncTask<String, String, CookieStore> {
    private String mLogin;
    private String mPass;
    private OnAuthorizationTaskCompleted callback;
    private CookieStore mCookieStore;
    private String mToken;

    public AuthorizationRequest(OnAuthorizationTaskCompleted callback, String login, String pass) {
        this.callback = callback;
        mLogin = login;
        mPass = pass;
    }

    public AuthorizationRequest(OnAuthorizationTaskCompleted callback, String token) {
        this.callback = callback;
        mToken = token;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected CookieStore doInBackground(String... urls) {
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
                    "application/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            if (mLogin != null && mPass != null) {
                nameValuePairs.add(new BasicNameValuePair("log", mLogin));
                nameValuePairs.add(new BasicNameValuePair("pwd", mPass));
            } else {
                nameValuePairs.add(new BasicNameValuePair("token", mToken));
            }

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            mCookieStore = new BasicCookieStore();
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);

            HttpResponse response = client.execute(post, ctx);
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
        return mCookieStore;
    }

    @Override
    protected void onPostExecute(CookieStore result) {
        if (callback != null) {
            callback.onAuthorizationTaskCompleted(result);
        }

//        super.onPostExecute(result);
    }
}