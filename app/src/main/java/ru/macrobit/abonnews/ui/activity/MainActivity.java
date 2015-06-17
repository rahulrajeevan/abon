package ru.macrobit.abonnews.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.client.CookieStore;

import java.util.HashMap;

import ru.macrobit.abonnews.OnAutorizationTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.GetCookiesFromTokenRequest;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.ui.fragment.NewsFragment;
import ru.macrobit.abonnews.ui.fragment.ProfileFragment;
import ru.ulogin.sdk.UloginAuthActivity;


public class MainActivity extends Env implements
        NavigationView.OnNavigationItemSelectedListener, OnAutorizationTaskCompleted {

    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";

    private final Handler mDrawerActionHandler = new Handler();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add(new NewsFragment(), "NEWS");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        navigate(mNavItemId);
    }

    private void navigate(final int itemId) {
        switch (itemId) {
            case R.id.profile:
                if (Utils.loadCookieFromSharedPreferences(Values.COOKIES, Utils.getPrefs(MainActivity.this)) != null) {
                    add(new ProfileFragment(), "Profile");
                }
                break;
            case R.id.comments:
                Toast.makeText(this, "Comments", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Values.REQUEST_ULOGIN) {
            HashMap userdata =
                    (HashMap) intent.getSerializableExtra (UloginAuthActivity.USERDATA);

            switch (resultCode) {
                case RESULT_OK:
                    String token = userdata.get(Values.TOKEN).toString();
                    Utils.saveToSharedPreferences(Values.TOKEN, token, Utils.getPrefs(this));
                    new GetCookiesFromTokenRequest(MainActivity.this).execute(Values.ULOGIN + token + Values.SOC_AUTORIZATION);
                    break;
                case RESULT_CANCELED:
                    if(userdata.get("error").equals("canceled")) {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error: "+userdata.get("error"),
                                Toast.LENGTH_SHORT).show();
                    }
            }
        }
    }

    @Override
    public void onAutorizationTaskCompleted(CookieStore result) {
        Utils.saveCookieToSharedPreferences(Values.COOKIES, result, Utils.getPrefs(this));
    }
}
