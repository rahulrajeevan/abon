package ru.macrobit.abonnews;

import org.apache.http.client.CookieStore;


public interface OnAuthorizationTaskCompleted {
    void onAuthorizationTaskCompleted(CookieStore result);
}