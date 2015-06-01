package ru.macrobit.abonnews.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonUtils {
    private static Gson gson;

    public static String toJson(Object o) {
        gson = new GsonBuilder().create();
        return gson.toJson(o);
    }

    public static <T> T fromJson(String s, Type typeOfT) {
        gson = new GsonBuilder().create();
        return gson.fromJson(s, typeOfT);
    }

    public static String toJsonWithNull(Object o) {
        gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(o);
    }

    public static Object fromJsonWithNull(String s, Class c) {
        gson = new GsonBuilder().serializeNulls().create();
        return gson.fromJson(s, c);
    }
}

