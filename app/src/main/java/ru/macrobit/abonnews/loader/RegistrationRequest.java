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

import ru.macrobit.abonnews.OnAuthorizationTaskCompleted;

public class RegistrationRequest extends AsyncTask<String, String, CookieStore> {
    private OnAuthorizationTaskCompleted callback;
    private String mEmail;
    private String mLogin;

    public RegistrationRequest(OnAuthorizationTaskCompleted callback, String email, String login) {
        this.callback = callback;
        mEmail = email;
        mLogin = login;
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
                    "application/x-www-form-urlencoded; charset=UTF-8");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("user_login", mLogin));
                nameValuePairs.add(new BasicNameValuePair("user_email", mEmail));

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

//            mCookieStore = new BasicCookieStore();
//            HttpContext ctx = new BasicHttpContext();
//            ctx.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);

            HttpResponse response = client.execute(post);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
//            List<Cookie> cookies = mCookieStore.getCookies();
//            if( !cookies.isEmpty() ){
//                for (Cookie cookie : cookies){
//                    String cookieString = cookie.getName() + " : " + cookie.getValue();
//                }
//            }
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
        return null;
    }

    @Override
    protected void onPostExecute(CookieStore result) {
        if (callback != null) {
            callback.onAutorizationTaskCompleted(result);
        }

//        super.onPostExecute(result);
    }
}