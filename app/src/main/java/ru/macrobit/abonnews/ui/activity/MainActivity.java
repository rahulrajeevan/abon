package ru.macrobit.abonnews.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.apache.http.client.CookieStore;

import ru.macrobit.abonnews.OnAuthorizationTaskCompleted;
import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.ui.fragment.NewsFragment;
import ru.macrobit.abonnews.ui.fragment.ProfileFragment;


public class MainActivity extends Env implements
        NavigationView.OnNavigationItemSelectedListener, OnAuthorizationTaskCompleted, OnTaskCompleted {

    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";

    private final Handler mDrawerActionHandler = new Handler();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;
    private Intent mIntent;
    private Toolbar mToolbar;
    private boolean isActivityCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntent = new Intent(MainActivity.this, FragmentActivity.class);
        if (!isActivityCreated) {
            initNavigationView();
            getAds();
        }
    }

    private void getAds() {
        if (Utils.isConnected(this)) {
            new GetRequest(this).execute(Values.ADS);
        }
    }

    private void initNavigationView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(MainActivity.this);
                if (getActiveFragments().size() > 1) {
                    onBackPressed();
                } else {
                    if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            }

        });
        isActivityCreated = true;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onPause() {
//        popBackStack();
//        mToolbar.getMenu().clear();
        super.onPause();
    }

    private void navigate(final int itemId) {
        switch (itemId) {
//            case R.id.news:
//                if (getActiveFragments().size() > 1) {
//                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                    replace(getFragmentByTag(Values.NEWS_TAG), Values.NEWS_TAG);
//                }
//                break;
            case R.id.profile:
//                add(new ProfileFragment(), Values.PROFILE_TAG);
                mIntent.putExtra(Values.TAG, Values.PROFILE_TAG);
                startActivity(mIntent);
                break;
            case R.id.comments:
                if (Utils.isCookiesExist(this)) {
//                    add(new MyCommentFragment(), Values.MY_COMMENTS);
                    mIntent.putExtra(Values.TAG, Values.MY_COMMENTS_TAG);
                    startActivity(mIntent);
                } else {
//                    add(new ProfileFragment(), Values.PROFILE_TAG);
                    mIntent.putExtra(Values.TAG, Values.PROFILE_TAG);
                    startActivity(mIntent);
                }
                break;
            case R.id.about:
//                add(new AboutFragment(), Values.ABOUT_TAG);
                mIntent.putExtra(Values.TAG, Values.ABOUT_TAG);
                startActivity(mIntent);
                break;
            case R.id.add_news:
                if (Utils.isCookiesExist(this)) {
//                    add(new AddPostFragment(), Values.ADD_TAG);
                    mIntent.putExtra(Values.TAG, Values.ADD_TAG);
                    startActivity(mIntent);
                } else {
//                    add(new ProfileFragment(), Values.PROFILE_TAG);
                    mIntent.putExtra(Values.TAG, Values.PROFILE_TAG);
                    startActivity(mIntent);
                }
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
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                initNavigationView();
                popBackStack();
            }
//            List<Fragment> fragments = getActiveFragments();
//            replace(getFragmentByTag(Values.NEWS_TAG), Values.NEWS_TAG);
//            if (fragments.size() == 1) {
//                super.onBackPressed();
//            } else {
//                for (Fragment f : fragments) {
//                    if (!f.getTag().equals(Values.NEWS_TAG))
//                        remove(f.getTag());
//                }
//            }
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (getActiveFragments().size() > 1) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
        }
    }

    @Override
    public void onAuthorizationTaskCompleted(CookieStore result) {
        Utils.saveCookieToSharedPreferences(Values.COOKIES, result, Utils.getPrefs(this));
        remove(Values.PROFILE_TAG);
        add(new ProfileFragment(), Values.PROFILE_TAG);
    }

    @Override
    public void onTaskCompleted(String result) {
        Utils.saveToSharedPreferences(Values.ADS_PREF, result, Utils.getPrefs(this));
        if (!isFragmentExist(Values.NEWS_TAG)) {
            add(new NewsFragment(), Values.NEWS_TAG);
        }
    }
}
