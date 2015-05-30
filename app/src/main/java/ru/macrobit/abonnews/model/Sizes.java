package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Comp on 30.05.2015.
 */
public class Sizes {
    private Thumbnail thumbnail;
    private Thumbnail medium;
    private Thumbnail large;
    @SerializedName("post-thumbnail")
    private Thumbnail postThumbnail;

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Thumbnail getMedium() {
        return medium;
    }

    public void setMedium(Thumbnail medium) {
        this.medium = medium;
    }

    public Thumbnail getLarge() {
        return large;
    }

    public void setLarge(Thumbnail large) {
        this.large = large;
    }

    public Thumbnail getPostThumbnail() {
        return postThumbnail;
    }

    public void setPostThumbnail(Thumbnail postThumbnail) {
        this.postThumbnail = postThumbnail;
    }
}
