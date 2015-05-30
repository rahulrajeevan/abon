package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Comp on 30.05.2015.
 */
public class ImageMeta {
    private int aperture;
    private String credit;
    private String camera;
    private String caption;
    @SerializedName("created_timestamp")
    private int createdTimestamp;
    private String copyright;
    @SerializedName("focal_length")
    private int focalLength;
    private int iso;
    @SerializedName("shutter_speed")
    private int shutterSpeed;
    private String title;
    private int orientation;

    public int getAperture() {
        return aperture;
    }

    public void setAperture(int aperture) {
        this.aperture = aperture;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(int createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public int getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(int focalLength) {
        this.focalLength = focalLength;
    }

    public int getIso() {
        return iso;
    }

    public void setIso(int iso) {
        this.iso = iso;
    }

    public int getShutterSpeed() {
        return shutterSpeed;
    }

    public void setShutterSpeed(int shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
