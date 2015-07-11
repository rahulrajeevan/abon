package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Comp on 11.07.2015.
 */
public class NewsAdd {
    String title;
    @SerializedName("content_raw")
    String content;

    public NewsAdd(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
