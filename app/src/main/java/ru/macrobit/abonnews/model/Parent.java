package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Comp on 17.06.2015.
 */
public class Parent {
    @SerializedName("ID")
    private int id;
    private String title;
    private String status;
    private String type;
    private Author author;
    private String content;
    private String parent;
    private String link;
    private String date;
    private String modified;
    private String format;
    private String slug;
    private String guid;
    private String excerpt;
    @SerializedName("menu_order")
    private int menuOrder;
    @SerializedName("comment_status")
    private String commentStatus;
    @SerializedName("ping_status")
    private String pingStatus;
    private boolean sticky;
    @SerializedName("date_tz")
    private String dateTz;
    @SerializedName("date_gmt")
    private String dateGmt;
    @SerializedName("modified_tz")
    private String modifiedTz;
    @SerializedName("modified_gmt")
    private String modifiedGmt;
    private Meta meta;
    @SerializedName("featured_image")
    private int featuredImage;
    private Terms terms;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public int getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(int menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public String getPingStatus() {
        return pingStatus;
    }

    public void setPingStatus(String pingStatus) {
        this.pingStatus = pingStatus;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public String getDateTz() {
        return dateTz;
    }

    public void setDateTz(String dateTz) {
        this.dateTz = dateTz;
    }

    public String getDateGmt() {
        return dateGmt;
    }

    public void setDateGmt(String dateGmt) {
        this.dateGmt = dateGmt;
    }

    public String getModifiedTz() {
        return modifiedTz;
    }

    public void setModifiedTz(String modifiedTz) {
        this.modifiedTz = modifiedTz;
    }

    public String getModifiedGmt() {
        return modifiedGmt;
    }

    public void setModifiedGmt(String modifiedGmt) {
        this.modifiedGmt = modifiedGmt;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public int getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(int featuredImage) {
        this.featuredImage = featuredImage;
    }

    public Terms getTerms() {
        return terms;
    }

    public void setTerms(Terms terms) {
        this.terms = terms;
    }
}
