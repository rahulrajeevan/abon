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
        mTransaction = mManager.beginTransaction();
        mTransaction.add(R.id.fragment_container, fragment);
        mTransaction.commit();
    }

    void addHiding(Fragment fragment) {
        mTransaction = mManager.beginTransaction();
        mTransaction.add(R.id.fragment_container, fragment);
        mTransaction.hide(fragment);
        mTransaction.commit();
    }

    void replace(Fragment fragment, String tag) {
        mTransaction = mManager.beginTransaction();
        mTransaction.replace(R.id.fragment_container, fragment);
        mTransaction.commit();
    }

    void remove(Fragment fragment) {
        mTransaction = mManager.beginTransaction();
        mTransaction.remove(fragment);
        mTransaction.commit();
    }

    void hide(Fragment fragment) {
        mTransaction = mManager.beginTransaction();
        mTransaction.hide(fragment);
        mTransaction.commit();
    }

    void show(Fragment fragment) {
        mTransaction = mManager.beginTransaction();
        mTransaction.show(fragment);
        mTransaction.commit();
    }
}