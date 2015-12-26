package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Comp on 03.06.2015.
 */
public class Comments {
    @SerializedName("ID")
    private int id;
    private int post;
    private String content;
    private String status;
    private String type;
    private int parent;
//    private int author;
    @SerializedName("author_name")
    private String authorName;
    private String date;
    @SerializedName("date_tz")
    private String dateTz;
    @SerializedName("date_gmt")
    private String dateGmt;

    public Comments(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTz() {
        return dateTz;
    }

    public void setDateTz(String dateTz) {
        this.dateTz = dateTz;
    }

    public String getDateGmt() {
        return dateGmt;
    }

    public void setDateGmt(String dateGmt) {
        this.dateGmt = dateGmt;
    }

}
