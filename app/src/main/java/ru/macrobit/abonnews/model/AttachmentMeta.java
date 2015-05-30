package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Comp on 30.05.2015.
 */
public class AttachmentMeta {
    private int width;
    private int height;
    private String file;
    private Sizes sizes;
    @SerializedName("image_meta")
    private ImageMeta imageMeta;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Sizes getSizes() {
        return sizes;
    }

    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }

    public ImageMeta getImageMeta() {
        return imageMeta;
    }

    public void setImageMeta(ImageMeta imageMeta) {
        this.imageMeta = imageMeta;
    }
}
