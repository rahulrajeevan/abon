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

public class RegistrationRequest extends AsyncTask<String, String, String> {
    private OnTaskCompleted callback;
    private String mEmail;
    private String mLogin;

    public RegistrationRequest(OnTaskCompleted callback, String email, String login) {
        this.callback = callback;
        mEmail = email;
        mLogin = login;
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
                nameValuePairs.add(new BasicNameValuePair("user_login", mLogin));
                nameValuePairs.add(new BasicNameValuePair("user_email", mEmail));

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            StringBuilder sb = new StringBuilder();
            String line = null;
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity()
                            .getContent(), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
            result = String.valueOf(response.getStatusLine().getStatusCode());
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

//        super.onPostExecute(result);
    }
}