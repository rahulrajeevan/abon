package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

public class MyComment {
    @SerializedName("ID")
    private int id;
    private Post post;
    private String content;
    @SerializedName("author_name")
    private String authorName;
    private String date;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
