package ru.macrobit.abonnews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShortNews implements Parcelable{
    String title;
    String date;
    String imageUrl;
    String id;

    public ShortNews(String title, String date, String imageUrl, String id) {
        this.title = title;
        this.date = date;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public ShortNews(Parcel in) {
        this.title = in.readString();
        this.date = in.readString();
        this.imageUrl = in.readString();
        this.id = in.readString();
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
