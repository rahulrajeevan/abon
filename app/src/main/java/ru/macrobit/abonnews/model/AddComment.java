package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

public class AddComment {
    private String content;
    @SerializedName("parent_id")
    private int parentId;
    private int approved;

    public AddComment(String content, int id) {
        this.content = content;
        parentId = id;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public AddComment(String content) {
        this.content = content;
        this.approved = 1;
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
