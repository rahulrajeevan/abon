package ru.macrobit.abonnews.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ru.macrobit.abonnews.model.Media;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.ShortNews;

/**
 * Created by Comp on 01.06.2015.
 */
public class NewsUtils {

    public static ArrayList<ShortNews> generateShortNews(News[] news) {
        ArrayList<ShortNews> arrayList = new ArrayList<ShortNews>();
        for (News n : news) {
            ShortNews shortNews = new ShortNews(n.getTitle(), parseDate(n.getDate()), n.getFeaturedImage().getGuid(), String.valueOf(n.getId()), n.isSticky());
            arrayList.add(shortNews);
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
