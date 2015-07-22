package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Comp on 30.05.2015.
 */
public class News {
    @SerializedName("ID")
    private int id;
    private String title;
    private String status;
    private String type;
    private String author;
    private String content;
    private String link;
    private String date;
    private boolean sticky;
    @SerializedName("featured_image")
    private String featuredImage;
    @SerializedName("comment_count")
    private String commentCount;
    private transient String adUrl;
    private transient String url;
    private transient boolean isAdv;

    public News (String title, String content) {
        this.title = title;
        this.content = content;
        this.isAdv = false;
    }

    public News () {
        this.isAdv = false;
    }

    public News (String adUrl, String url, boolean isAdv) {
        this.url = url;
        this.adUrl = adUrl;
        this.isAdv = isAdv;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public boolean isAdv() {
        return isAdv;
    }

    public void setIsAdv(boolean isAdv) {
        this.isAdv = isAdv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }
}
