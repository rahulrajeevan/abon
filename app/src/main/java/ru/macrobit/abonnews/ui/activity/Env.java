package ru.macrobit.abonnews.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ru.macrobit.abonnews.R;

public class Env extends AppCompatActivity {
    private static FragmentTransaction mTransaction;
    private static FragmentManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = getSupportFragmentManager();
    }

    void add(Fragment fragment, String tag) {
        if (!isFragmentExist(tag)) {
            mTransaction = mManager.beginTransaction();
            mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            mTransaction.add(R.id.fragment_container, fragment, tag);
            mTransaction.commit();
        } else {
            popBackStack(tag);
        }
    }

    void popBackStack (String tag) {
        mManager.popBackStack(tag, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
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
        mTransaction = mManager.beginTransaction();
        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        mTransaction.remove(getFragmentByTag(tag));
        mTransaction.commit();
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