package ru.macrobit.abonnews;

public class Values {
    public static final String URL = "http://abon-news.ru/";
    public static final String GET_POST = URL + "wp-json/posts?filter[posts_per_page]=-1";
    public static final String AUTORIZATION = URL + "wp-login.php";
    public static final String MEDIA = URL + "wp-json/media";
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
    public static final String ULOGIN = URL + "?ulogin=";
    public static final String SOC_AUTORIZATION = "&backurl=http://abon-news.ru/wp-login.php?loggedout=true";
}
