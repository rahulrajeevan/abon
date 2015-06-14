package ru.macrobit.abonnews.model;

/**
 * Created by Ghost Surfer on 14.06.2015.
 */
public class ShortCookie {
    private String key;
    private String value;

    public ShortCookie(String key, String value) {
        this.key = key;
        this.value = value;
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
