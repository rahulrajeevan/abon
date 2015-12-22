package ru.macrobit.abonnews.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ru.macrobit.abonnews.R;

public class Env extends AppCompatActivity {
    private static FragmentTransaction mTransaction;
    private static FragmentManager mManager;
    private List<WeakReference<Fragment>> mFragList = new ArrayList<WeakReference<Fragment>>();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = getSupportFragmentManager();
    }

    @Override
    public void onAttachFragment (Fragment fragment) {
        mFragList.add(new WeakReference(fragment));
    }

    void showDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    void hideDialog() {
        mProgressDialog.hide();
    }

    List<Fragment> getActiveFragments() {
        ArrayList<Fragment> ret = new ArrayList<Fragment>();
        for(WeakReference<Fragment> ref : mFragList) {
            Fragment f = ref.get();
            if(f != null) {
                if(f.isVisible()) {
                    ret.add(f);
                }
            }
        }
        return ret;
    }

    void add(Fragment fragment) {
            mTransaction = mManager.beginTransaction();
            mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            mTransaction.add(R.id.fragment_container, fragment);
            mTransaction.addToBackStack(null);
            mTransaction.commit();
    }

    void add(Fragment fragment, Bundle bundle, String tag) {
        mTransaction = mManager.beginTransaction();
        mTransaction.add(R.id.fragment_container, fragment, tag);
        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragment.setArguments(bundle);
        mTransaction.addToBackStack(tag);
        mTransaction.commit();
    }

    void add(Fragment fragment, String tag) {
        if (!isFragmentExist(tag)) {
            mTransaction = mManager.beginTransaction();
            mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            mTransaction.add(R.id.fragment_container, fragment, tag);
            mTransaction.addToBackStack(tag);
            mTransaction.commit();
        } else {
            popBackStack(tag);
        }
//        if (!tag.equals(Values.NEWS_TAG)) {
//            if (!Values.isDisplayHomeEnabled) {
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                Values.isDisplayHomeEnabled = true;
//            }
//        }
    }

    void popBackStack (String tag) {
        mManager.popBackStack(tag, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
    }

    void popBackStack () {
        mManager.popBackStack();
    }

    void addHiding(Fragment fragment, String tag) {
        mTransaction = mManager.beginTransaction();
        mTransaction.add(R.id.fragment_container, fragment, tag);
        mTransaction.hide(fragment);
        mTransaction.commit();
    }

    void replace(Fragment fragment, String tag) {
        mTransaction = mManager.beginTransaction();
        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mTransaction.replace(R.id.fragment_container, fragment, tag);
        mTransaction.commit();
    }

    void remove(String tag) {
        if (isFragmentExist(tag)) {
            mTransaction = mManager.beginTransaction();
            mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            mTransaction.remove(getFragmentByTag(tag));
            mTransaction.commit();
        }
    }

    void hide(String tag) {
        mTransaction = mManager.beginTransaction();
        mTransaction.hide(getFragmentByTag(tag));
        mTransaction.commit();
    }

    void show(String tag) {
        mTransaction = mManager.beginTransaction();
        mTransaction.show(getFragmentByTag(tag));
        mTransaction.commit();
    }

    boolean isFragmentExist(String tag) {
        return (getFragmentByTag(tag) != null)? true: false;
    }

    Fragment getFragmentByTag(String tag) {
        return mManager.findFragmentByTag(tag);
    }

}