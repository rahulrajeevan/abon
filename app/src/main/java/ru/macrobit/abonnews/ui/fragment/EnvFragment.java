package ru.macrobit.abonnews.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

    public void showProgressDialog(String message) {
        try {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void hideProgressDialog() {
        try {
            mProgressDialog.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

   public void showDialog(DialogFragment frag, String tag) {
        frag.show(mManager, tag);
    }


    void popBackStack() {
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

    public void addListenerToEditText(View view, final Activity activity) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                addListenerToEditText(innerView, activity);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    boolean isFragmentExist(String tag) {
        return (getFragmentByTag(tag) != null) ? true : false;
    }
}