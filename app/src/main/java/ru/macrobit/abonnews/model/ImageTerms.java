package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Comp on 30.05.2015.
 */
public class ImageTerms {
    @SerializedName("media_category")
    private MediaCategory[] mediaCategory;

    public MediaCategory[] getCategory() {
        return mediaCategory;
    }

    public void setCategory(MediaCategory[] category) {
        this.mediaCategory = category;
    }
}
