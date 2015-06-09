package ru.macrobit.abonnews;

public class Values {
    public static final String URL = "http://abon-news.ru/";
    public static final String GET_POST = URL + "wp-json/posts?filter[posts_per_page]=-1";
    public static final String AUTORIZATION = URL + "wp-login.php";
    public static final String MEDIA = URL + "media";
    public static final String COMMENTS = "/comments";
    public static final String PUSH = URL + "/push";
    public static final String SEARCH = URL + "wp-json/posts?filter[s]=";
    public static final String GET_PAGE_POSTS = URL + "/wp-json/posts?filter[posts_per_page]=10&page=";
}
