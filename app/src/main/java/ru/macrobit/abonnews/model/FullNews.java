package ru.macrobit.abonnews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FullNews extends ShortNews implements Parcelable {
    private String body;

    public FullNews(String title, String date, String imageUrl, String id, String body) {
        super(title, date, imageUrl, id);
        this.body = body;
    }

    public FullNews(ShortNews news, String body){
        super(news.getTitle(), news.getDate(), news.getImageUrl(), news.getId());
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    protected FullNews(Parcel in) {
        super(in);
        body = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
    }

    public static final Parcelable.Creator<FullNews> CREATOR = new Parcelable.Creator<FullNews>() {

        @Override
        public FullNews createFromParcel(Parcel source) {
            return new FullNews(source);
        }

        @Override
        public FullNews[] newArray(int size) {
            return new FullNews[size];
        }
    };
}
