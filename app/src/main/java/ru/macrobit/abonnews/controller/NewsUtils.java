package ru.macrobit.abonnews.controller;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ru.macrobit.abonnews.model.Media;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.ShortNews;


public class NewsUtils {

    public static ArrayList<ShortNews> generateShortNews(News[] news, Context context) {
        ArrayList<ShortNews> arrayList = new ArrayList<ShortNews>();
//        for (News n : news) {
        int ad = 0;
        int x = news.length + (int) (news.length / 7);
        for (int i = 0; i < x; i++) {
            ShortNews shortNews;
            if (i % 6 == 0) {
                if (i != 0) {
                    ad++;
                    shortNews = new ShortNews(Utils.getAd(5, context));
                    arrayList.add(shortNews);
                } else {
                    News n = news[i - ad];
                    shortNews = new ShortNews(n.getTitle(), parseDate(n.getDate()), n.getFeaturedImage().getGuid(), String.valueOf(n.getId()), n.isSticky());
                    arrayList.add(shortNews);
                }
            } else {
                News n = news[i - ad];
                shortNews = new ShortNews(n.getTitle(), parseDate(n.getDate()), n.getFeaturedImage().getGuid(), String.valueOf(n.getId()), n.isSticky());
                arrayList.add(shortNews);
            }


        }
        return arrayList;
    }

    public static ShortNews generateShortNews(News n) {
        ShortNews shortNews = new ShortNews(n.getTitle(), parseDate(n.getDate()), n.getFeaturedImage().getGuid(), String.valueOf(n.getId()), n.isSticky());
        return shortNews;
    }

    public static String parseDate(String s) {
        s = s.replaceAll("T", " ");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return df.format(date);
    }

    public static ArrayList<Media> getMediaById(String id, Media[] media) {
        int i = Integer.parseInt(id);
        ArrayList<Media> array = new ArrayList<>();
        for (Media m : media) {
            if (m.getParent() != null) {
                if (m.getParent().getId() == i) {
                    array.add(m);
                }
            }
        }
        return array;
    }
}
