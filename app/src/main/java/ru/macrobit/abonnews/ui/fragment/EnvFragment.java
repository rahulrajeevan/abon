package ru.macrobit.abonnews.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;


public class EnvFragment extends Fragment {
    private static FragmentTransaction mTransaction;
    private FragmentManager mManager;
    private ProgressDialog mProgressDialog;


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Values.isDisplayHomeEnabled = false;
        mManager = getActivity().getSupportFragmentManager();
    }

    void showDialog(String message) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    void hideDialog() {
        mProgressDialog.hide();
    }
    void add(Fragment fragment, String tag) {
//        if (!Values.isDisplayHomeEnabled) {
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            Values.isDisplayHomeEnabled = true;
//        }
        mTransaction = mManager.beginTransaction();
        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mTransaction.add(R.id.fragment_container, fragment, tag);
        mTransaction.addToBackStack(tag);
        mTransaction.commit();
    }

    void add(Fragment fragment, Bundle bundle, String tag) {
        if (!Values.isDisplayHomeEnabled) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Values.isDisplayHomeEnabled = true;
        }
        mTransaction = mManager.beginTransaction();
        mTransaction.add(R.id.fragment_container, fragment, tag);
        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragment.setArguments(bundle);
        mTransaction.addToBackStack(tag);
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

    Fragment getFragmentByTag(String tag) {
        return mManager.findFragmentByTag(tag);
    }

    void replace(Fragment fragment, String tag) {
        mTransaction = mManager.beginTransaction();
        mTransaction.replace(R.id.fragment_container, fragment, tag);
        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mTransaction.addToBackStack(tag);
        mTransaction.commit();
    }

    void popBackStack () {
        mManager.popBackStack();
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

    void makeText(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    boolean isFragmentExist(String tag) {
        return (getFragmentByTag(tag) != null)? true: false;
    }
}