package ru.macrobit.abonnews.loader;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

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
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            MultipartEntity entity = new MultipartEntity();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            File f = new File(mFile);
            if (f.exists()) {
                FormBodyPart part = new FormBodyPart("file", new FileBody(f, ContentType.MULTIPART_FORM_DATA, null));
                String s = part.getHeader().toString();
                builder.addPart("file", new FileBody(f, ContentType.MULTIPART_FORM_DATA, null));
                entity.addPart(part);
            }
            HttpPost post = new HttpPost(urls[0]);
            int portOfProxy = android.net.Proxy.getDefaultPort();
            if (portOfProxy > 0) {
                HttpHost proxy = new HttpHost(
                        android.net.Proxy.getDefaultHost(), portOfProxy);
                client.getParams().setParameter(
                        ConnRoutePNames.DEFAULT_PROXY, proxy);
            }

//            post.setHeader("Content-type", "multipart/form-data; boundary=--------");

            HttpEntity ent = builder.build();
            post.setHeader("Content-type", "multipart/form-data");
//            post.setHeader("Content-Disposition", "form-data; name=\"file\", " + "filename=" + "\"" + f.getName() + "\", " + "Content-Type: multipart/form-data; charset=ISO-8859-1, Content-Transfer-Encoding: binary");
//            post.setEntity(builder.build());
            post.setEntity(entity);
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
