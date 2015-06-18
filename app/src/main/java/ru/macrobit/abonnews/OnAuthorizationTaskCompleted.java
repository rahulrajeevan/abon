package ru.macrobit.abonnews;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;


public interface OnAuthorizationTaskCompleted {
    void onAutorizationTaskCompleted(CookieStore result);
}