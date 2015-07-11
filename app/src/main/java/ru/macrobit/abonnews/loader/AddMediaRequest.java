package ru.macrobit.abonnews.loader;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import ru.macrobit.abonnews.OnTaskCompleted;

public class AddMediaRequest extends AsyncTask<String, String, String> {

    String result;
    private OnTaskCompleted callback;
    private CookieStore mCookies;
    private String mFile;

    public AddMediaRequest(OnTaskCompleted callback, BasicClientCookie[] basicClientCookies, String file) {
        this.callback = callback;
        this.mFile = file;
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
            DefaultHttpClient client = new DefaultHttpClient();
            if (mCookies != null) {
                client.setCookieStore(mCookies);
            }
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            File f = new File(mFile);
            if (f.exists()) {
                builder.addPart("file", new FileBody(f));
//                builder.addPart(part);
            }
            HttpPost post = new HttpPost(urls[0]);
//            post.setHeader("Content-type", "multipart/form-data");
            post.setEntity(builder.build());
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
