package ru.macrobit.abonnews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FullNews extends ShortNews implements Parcelable {
    private String body;
    private String url;

    public FullNews(String title, String date, String imageUrl, String id, String body, boolean sticky, String url, String commentCount) {
        super(title, date, imageUrl, id, sticky, commentCount);
        this.body = body;
        this.url = url;
    }

    public FullNews(ShortNews news, String body, String url){
        super(news.getTitle(), news.getDate(), news.getImageUrl(), news.getId(), news.isSticky(), news.getCommentCount());
        this.body = body;
        this.url = url;
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
        url = in.readString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
        dest.writeString(url);
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
