package ru.macrobit.abonnews;

public class Values {
    public static final String URL = "http://abon-news.ru/";
    public static final String GET_POST = URL + "wp-json/posts?filter[posts_per_page]=-1";
    public static final String AUTHORIZATION = URL + "wp-login.php";
    public static final String POSTS = URL + "wp-json/posts/";
    public static final String MEDIA = URL + "wp-json/media?filter[posts_per_page]=-1";
    public static final String COMMENTS = "/comments";
    public static final String PUSH = URL + "/push";
    public static final String SEARCH = URL + "wp-json/posts?filter[s]=";
    public static final String GET_PAGE_POSTS = URL + "/wp-json/posts?filter[posts_per_page]=10&page=";
    public static final String REGISTRY = URL + "wp-login.php?action=register";
    public static final String PREF = "Abon news preferences";
    public static final String COOKIES = "cookies";
    public static final String PROFILE = URL + "wp-json/users/me";
    public static final int REQUEST_ULOGIN = 1;
    public static final String TOKEN = "token";
    public static final String ULOGIN = URL + "?ulogin=token&backurl=http://abon-news.ru/wp-login.php?loggedout=true";
//    public static final String SOC_AUTORIZATION = URL + "?ulogin=token&backurl=http://abon-news.ru/wp-login.php";
    public static final String SOC_AUTORIZATION = "http://abon-news.ru/?ulogin=token&backurl=http%3A%2F%2Fabon-news.ru%2Fwp-login.php%3Fredirect_to%3Dhttp%253A%252F%252Fabon-news.ru%252Fwp-admin%252F%26reauth%3D1";
//            "?loggedout=true";
    public static final String DETAIL_TAG = "detail";

    public static final String AUTHORIZATION_TAG = "authorization";
    public static final String NEWS_TAG = "news";
    public static final String PROFILE_TAG = "profile";
    public static final String ADS = URL +  "/wp-json/ads";
    public static final String ADD_TAG = "add";
    public static final int MEDIA_RESULT = 121;
}
