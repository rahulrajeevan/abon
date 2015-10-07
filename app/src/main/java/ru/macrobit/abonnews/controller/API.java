package ru.macrobit.abonnews.controller;

import com.google.android.gms.auth.api.Auth;

import org.apache.http.client.CookieStore;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import ru.macrobit.abonnews.model.AddComment;
import ru.macrobit.abonnews.model.Ads;
import ru.macrobit.abonnews.model.Author;
import ru.macrobit.abonnews.model.Comments;
import ru.macrobit.abonnews.model.MyComment;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.NewsAdd;
import ru.macrobit.abonnews.model.PushReg;
import ru.macrobit.abonnews.model.UploadedMedia;

public class API {

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
        @GET("/wp-json/comments")
        void getPostComments(retrofit.Callback<List<Comments>> callback);
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
        void auth(@Body Auth auth, retrofit.Callback<CookieStore> callback);
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
        @POST("/wp-json/posts/")
        void addComment(@Body AddComment addComment, retrofit.Callback<List<Comments>> callback);
    }

    public interface IPostFile {
        @Multipart
        @POST("/upload.php")
        void sendFile(@Part("file") TypedFile file, retrofit.Callback<UploadedMedia> callback);
    }

}