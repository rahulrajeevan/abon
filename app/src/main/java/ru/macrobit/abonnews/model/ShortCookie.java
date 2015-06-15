package ru.macrobit.abonnews.model;

import java.util.Date;

/**
 * Created by Ghost Surfer on 14.06.2015.
 */
public class ShortCookie {
    private String key;
    private String value;
    private String domain;
    private Date expiryDate;

    public ShortCookie(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ShortCookie(String key, String value, String domain, Date expiryDate) {
        this.key = key;
        this.value = value;
        this.domain = domain;
        this.expiryDate = expiryDate;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
