package ru.macrobit.abonnews.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.apache.http.client.CookieStore;

import java.util.HashMap;
import java.util.List;

import ru.macrobit.abonnews.OnAuthorizationTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.AddMediaRequest;
import ru.macrobit.abonnews.loader.AuthorizationRequest;
import ru.macrobit.abonnews.ui.fragment.AboutFragment;
import ru.macrobit.abonnews.ui.fragment.AuthorizationFragment;
import ru.macrobit.abonnews.ui.fragment.MyCommentFragment;
import ru.macrobit.abonnews.ui.fragment.NewsFragment;
import ru.macrobit.abonnews.ui.fragment.ProfileFragment;
import ru.ulogin.sdk.UloginAuthActivity;


public class MainActivity extends Env implements
        NavigationView.OnNavigationItemSelectedListener, OnAuthorizationTaskCompleted {

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
        add(new NewsFragment(), Values.NEWS_TAG);

        initNavigationView();

//        navigate(mNavItemId);
    }

    private void initNavigationView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActiveFragments().size() > 1) {
                    onBackPressed();
                } else {
                    if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            }

        });
    }

    private void navigate(final int itemId) {
        switch (itemId) {
            case R.id.profile:
                if (Utils.isCookiesExist(this)) {
                    add(new ProfileFragment(), Values.PROFILE_TAG);
                } else {
                    if (getFragmentByTag(Values.AUTHORIZATION_TAG) == null) {
                        add(new AuthorizationFragment(), Values.AUTHORIZATION_TAG);
                    } else {
                        popBackStack(Values.AUTHORIZATION_TAG);
                    }
                }
                break;
            case R.id.comments:
                if (Utils.isCookiesExist(this)) {
                    add(new MyCommentFragment(), Values.MY_COMMENTS);
                } else {
                    add(new AuthorizationFragment(), Values.AUTHORIZATION_TAG);
                }
                break;
            case R.id.about:
                add(new AboutFragment(), Values.ABOUT_TAG);
                break;
            case R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
//        menuItem.setChecked(true);
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
            if (getActiveFragments().size() > 1) {
                getSupportFragmentManager().popBackStack();
                return super.onOptionsItemSelected(item);
            }
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            initNavigationView();
            List<Fragment> fragments = getActiveFragments();
            if (fragments.size() == 1) {
                super.onBackPressed();
            } else {
                for (Fragment f : fragments) {
                    if (!f.getTag().equals(Values.NEWS_TAG))
                        remove(f.getTag());
                }
            }
        }
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Values.REQUEST_ULOGIN) {
            HashMap userdata =
                    (HashMap) data.getSerializableExtra(UloginAuthActivity.USERDATA);

            switch (resultCode) {
                case RESULT_OK:
                    String token = userdata.get(Values.TOKEN).toString();
                    Utils.saveToSharedPreferences(Values.TOKEN, token, Utils.getPrefs(this));
                    new AuthorizationRequest(MainActivity.this, token).execute(Values.SOC_AUTORIZATION);
                    break;
                case RESULT_CANCELED:
                    if (userdata.get("error").equals("canceled")) {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error: " + userdata.get("error"),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
        if (requestCode == Values.MEDIA_RESULT && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            new AddMediaRequest(null, Utils.loadCookieFromSharedPreferences(Values.COOKIES,
                    Utils.getPrefs(this)), filePath).execute(Values.MEDIA_ADD);
        }
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (getActiveFragments().size() > 1) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
        }
    }


    @Override
    public void onAutorizationTaskCompleted(CookieStore result) {
        remove(Values.AUTHORIZATION_TAG);
        Utils.saveCookieToSharedPreferences(Values.COOKIES, result, Utils.getPrefs(this));
    }
}
