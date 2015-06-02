package ru.macrobit.abonnews.ui.fragment;

/**
 * Created by Comp on 02.06.2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.macrobit.abonnews.R;


public class EnvFragment extends Fragment {
    static FragmentTransaction ft;
    FragmentManager mManager;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mManager = getActivity().getSupportFragmentManager();
    }

    void add(Fragment fragment) {
        ft = mManager.beginTransaction();
        ft.add(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    void add(Fragment fragment, Bundle bundle) {
        ft = mManager.beginTransaction();
        ft.add(R.id.fragment_container, fragment);
        fragment.setArguments(bundle);
        ft.addToBackStack(null);
        ft.commit();
    }

    void remove(Fragment fragment) {
        ft = mManager.beginTransaction();
        ft.remove(fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    void replace(Fragment fragment) {
        ft = mManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}