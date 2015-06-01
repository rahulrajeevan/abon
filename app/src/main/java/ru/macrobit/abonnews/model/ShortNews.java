package ru.macrobit.abonnews.model;

/**
 * Created by Comp on 30.05.2015.
 */
public class ShortNews {
    String title;
    String date;
    String imageUrl;
    String id;

    public ShortNews(String title, String date, String imageUrl, String id) {
        this.title = title;
        this.date = date;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
