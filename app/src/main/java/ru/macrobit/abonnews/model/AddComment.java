package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ghost Surfer on 26.06.2015.
 */
public class AddComment {
    private String content;
    @SerializedName("parent_id")
    private int parentId;

    public AddComment(String content, int id) {
        this.content = content;
        parentId = id;
    }

    public AddComment(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
