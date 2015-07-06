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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.apache.http.client.CookieStore;

import java.util.HashMap;

import ru.macrobit.abonnews.OnAuthorizationTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.AddMediaRequest;
import ru.macrobit.abonnews.loader.AuthorizationRequest;
import ru.macrobit.abonnews.ui.fragment.AboutFragment;
import ru.macrobit.abonnews.ui.fragment.AddPostFragment;
import ru.macrobit.abonnews.ui.fragment.AuthorizationFragment;
import ru.macrobit.abonnews.ui.fragment.DetailNewsFragment;
import ru.macrobit.abonnews.ui.fragment.EnvFragment;
import ru.macrobit.abonnews.ui.fragment.MyCommentFragment;
import ru.macrobit.abonnews.ui.fragment.ProfileFragment;
import ru.macrobit.abonnews.ui.fragment.RegistrationFragment;
import ru.ulogin.sdk.UloginAuthActivity;

public class FragmentActivity extends Env implements NavigationView.OnNavigationItemSelectedListener, OnAuthorizationTaskCompleted {

    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";

    private final Handler mDrawerActionHandler = new Handler();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;
    private EnvFragment mFragment;
    private Bundle mExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        initNavigationView();
        add(getFragment(getTag()), getTag());
    }

    private String getTag() {
        String value = null;
        mExtras = getIntent().getExtras();
        if (mExtras != null) {
            value = mExtras.getString(Values.TAG);
        }
        return value;
    }

    private Fragment getFragment(String nameFrag) {
        switch (nameFrag) {
            case Values.ABOUT_TAG:
                mFragment = new AboutFragment();
                break;
            case Values.AUTHORIZATION_TAG:
                mFragment = new AuthorizationFragment();
                break;
            case Values.ADD_TAG:
                mFragment = new AddPostFragment();
                break;
            case Values.DETAIL_TAG:
                mFragment = new DetailNewsFragment();
                break;
            case Values.PROFILE_TAG:
                mFragment = new ProfileFragment();
                break;
            case Values.REGISTRATION_TAG:
                mFragment = new RegistrationFragment();
                break;
            case Values.MY_COMMENTS_TAG:
                mFragment = new MyCommentFragment();
                break;
        }
        return mFragment;
    }

    private void initNavigationView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
//        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    mDrawerLayout.openDrawer(GravityCompat.START);
//                } else {
                onBackPressed();
//                }
            }

        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void navigate(final int itemId) {
        switch (itemId) {
//            case R.id.news:
//                if (getActiveFragments().size() > 1) {
////                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                    goToMain();
////                    replace(getFragmentByTag(Values.NEWS_TAG), Values.NEWS_TAG);
//                }
//                break;
            case R.id.profile:
                add(new ProfileFragment(), Values.PROFILE_TAG);
                break;
            case R.id.comments:
                if (Utils.isCookiesExist(this)) {
                    add(new MyCommentFragment(), Values.MY_COMMENTS);
                } else {
                    add(new ProfileFragment(), Values.PROFILE_TAG);
                }
                break;
            case R.id.about:
                add(new AboutFragment(), Values.ABOUT_TAG);
                break;
            case R.id.add_news:
                if (Utils.isCookiesExist(this)) {
                    add(new AddPostFragment(), Values.ADD_TAG);
                } else {
                    add(new ProfileFragment(), Values.PROFILE_TAG);
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
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (isFragmentExist(Values.DETAIL_TAG)) {
                DetailNewsFragment frag = (DetailNewsFragment) mFragment;
                if (DetailNewsFragment.mShareWebView.getVisibility() != View.GONE) {
                    DetailNewsFragment.mShareWebView.setVisibility(View.GONE);
                    DetailNewsFragment.mShareWebView.loadUrl("about:blank");
                } else {
                    if (DetailNewsFragment.mCustomViewContainer.getVisibility() != View.GONE) {
                        frag.hide();
                    } else {
                        goToMain();
                    }
                }
            } else {
                if (getActiveFragments().size() <= 1) {
                    goToMain();
                } else {
                    popBackStack();
                }
            }
        }
    }

    private void goToMain() {
        finish();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(mExtras);
//        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mExtras = savedInstanceState;
        add(getFragment(getTag()), getTag());
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
                    new AuthorizationRequest(FragmentActivity.this, token).execute(Values.SOC_AUTORIZATION);
                    break;
                case RESULT_CANCELED:
//                    popBackStack();

                    if (userdata.get("error").equals("canceled")) {
                        Toast.makeText(this, getString(R.string.cancel), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error: " + userdata.get("error"),
                                Toast.LENGTH_SHORT).show();
                    }
                    finish();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_, menu);
        return true;
    }

    @Override
    public void onAuthorizationTaskCompleted(CookieStore result) {
        Utils.saveCookieToSharedPreferences(Values.COOKIES, result, Utils.getPrefs(this));

        popBackStack();
        finish();
//        add(new ProfileFragment(), Values.PROFILE_TAG);
    }
}
