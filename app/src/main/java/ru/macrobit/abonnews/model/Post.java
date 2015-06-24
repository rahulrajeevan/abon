package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("ID")
    private int id;
    @SerializedName("post_title")
    private String postTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }
}
