package ru.macrobit.abonnews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Comp on 20.06.2015.
 */
public class Ads {
    private int id;
    private String name;
    private String description;
    @SerializedName("ad_img")
    private String adImg;
}
