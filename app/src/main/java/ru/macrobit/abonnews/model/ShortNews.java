package ru.macrobit.abonnews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShortNews implements Parcelable{
    String title;
    String date;
    String imageUrl;
    String id;
    String redirectUrl;
    boolean sticky;
    boolean isAd;

    public ShortNews(String title, String date, String imageUrl, String id, boolean sticky) {
        this.title = title;
        this.date = date;
        this.imageUrl = imageUrl;
        this.id = id;
        this.sticky = sticky;
        this.isAd = false;
    }

    public ShortNews(Parcel in) {
        this.title = in.readString();
        this.date = in.readString();
        this.imageUrl = in.readString();
        this.id = in.readString();
        this.sticky = in.readByte() != 0;
        this.isAd = in.readByte() != 0;
        this.redirectUrl = in.readString();
    }

    public ShortNews(String imageUrl, String url) {
        this.imageUrl = imageUrl;
        this.redirectUrl = url;
        this.isAd = true;
    }


    public String getUrl() {
        return redirectUrl;
    }

    public void setUrl(String url) {
        this.redirectUrl = url;
    }
    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setIsAd(boolean isAd) {
        this.isAd = isAd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(imageUrl);
        dest.writeString(id);
        dest.writeByte((byte) (sticky ? 1 : 0));
        dest.writeByte((byte) (isAd ? 1 : 0));
        dest.writeString(redirectUrl);
    }



    public static final Parcelable.Creator<ShortNews> CREATOR
            = new Parcelable.Creator<ShortNews>() {
        public ShortNews createFromParcel(Parcel in) {
            return new ShortNews(in);
        }

        public ShortNews[] newArray(int size) {
            return new ShortNews[size];
        }
    };

}
