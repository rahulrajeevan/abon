package ru.macrobit.abonnews;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

@ReportsCrashes(formKey="",
        formUri = "http://abon-news.ru/test/test.php",
        mode = ReportingInteractionMode.TOAST,
        forceCloseDialogAfterToast = true,
        resToastText = R.string.crash_toast_text)
public class AbonApplication extends Application {
    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
    }
}
