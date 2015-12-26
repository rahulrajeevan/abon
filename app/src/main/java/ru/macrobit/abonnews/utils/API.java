package ru.macrobit.abonnews.utils;

import com.squareup.okhttp.OkHttpClient;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.model.AddComment;
import ru.macrobit.abonnews.model.Ads;
import ru.macrobit.abonnews.model.Auth;
import ru.macrobit.abonnews.model.Author;
import ru.macrobit.abonnews.model.AuthorizationResponse;
import ru.macrobit.abonnews.model.ChangePass;
import ru.macrobit.abonnews.model.Comments;
import ru.macrobit.abonnews.model.MyComment;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.NewsAdd;
import ru.macrobit.abonnews.model.PushReg;
import ru.macrobit.abonnews.model.Reg;
import ru.macrobit.abonnews.model.UploadedMedia;

public class API {

    private static OkHttpClient client = new OkHttpClient();
    Executor executor = Executors.newSingleThreadExecutor();
    public static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(Values.URL)
            .setClient(new OkClient(client))
            .build();

    public static RestAdapter getRestAdapter() {
        client.setCookieHandler(CookieHandler.getDefault());
        return restAdapter;
    }

    public interface IGetAds {
        @GET("/wp-json/ads")
        void getAds(retrofit.Callback<List<Ads>> callback);
    }

    public interface IGetNews {
        @GET("/wp-json/posts?filter[posts_per_page]=30")
        void getNews(@Query("page") int page, @Query("filter[sticky]") int sticky, retrofit.Callback<List<News>>callback);
    }

    public interface ISearchNews {
        @GET("/wp-json/posts")
        void searchNews(@Query("filter[s]") String filter, retrofit.Callback<List<News>> callback);
    }

    public interface IGetMyComments {
        @GET("/wp-json/comments/my")
        void getMyComments(retrofit.Callback<List<MyComment>> callback);
    }

    public interface IGetPostComments {
        @GET("/wp-json/posts/{id}/comments")
        void getPostComments(@Path("id") String newsId, retrofit.Callback<ArrayList<Comments>> callback);
    }

    public interface IGetProfileInfo {
        @GET("/wp-json/users/me")
        void getProfileInfo(retrofit.Callback<Author> callback);
    }

    public interface IGetPost {
        @GET("/wp-json/posts/{post}")
        void getPost(@Path("post") String post, retrofit.Callback<News>callback);
    }

    public interface IAuthorization {
        @POST("/wp-json/users/auth")
        void auth(@Body Auth auth, retrofit.Callback<AuthorizationResponse> callback);
    }

    public interface ISendPushID {
        @POST("/wp-json/push")
        void sendPushId(@Body PushReg pushReg, retrofit.Callback<String> callback);
    }


    public interface IAddPost {
        @POST("/wp-json/posts/")
        void addPost(@Body NewsAdd newsAdd, retrofit.Callback<News> callback);
    }

    public interface IAddComment {
        @POST("/wp-json/posts/{id}/comments")
        void addComment(@Body AddComment addComment, @Path("id") String id, retrofit.Callback<Comments> callback);
        @POST("/wp-json/posts/{id}/comments")
        void addCommentResp(@Body AddComment addComment, @Path("id") String id, retrofit.Callback<Response> callback);
    }

    public interface IPostFile {
        @Multipart
        @POST("/upload.php")
        void sendFile(@Part("file") TypedFile file, retrofit.Callback<UploadedMedia> callback);
    }

    public interface IChangePassword {
        @POST("/wp-json/users/changepass")
        void changePass(@Body ChangePass pass, retrofit.Callback<String> callback);
    }

    public interface ISocAuthorization{
        @FormUrlEncoded
        @POST("/?ulogin=token&backurl=http%3A%2F%2Fabon-news.ru%2Fwp-login.php%3Fredirect_to%3Dhttp%253A%252F%252Fabon-news.ru%252Fwp-admin%252F%26reauth%3D1")
        void auth(@Field("token") String token, Callback<Response> callback);
    }

    public interface IRegistration {
        @POST("/wp-json/users/register")
        void reg(@Body Reg reg, retrofit.Callback callback);
    }

    public interface IGetPageId {
        @GET("/posts/{two}/{three}")
        void getPageId(@Path("two") String razdel, @Path("three") String newsName, Callback<Response> callback);
    }

}