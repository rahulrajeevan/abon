package ru.macrobit.abonnews.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.macrobit.abonnews.BuildConfig;
import ru.macrobit.abonnews.R;

public class AboutFragment extends EnvFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_about,
                container, false);
        String versionName = BuildConfig.VERSION_NAME;
        TextView tv = (TextView) view.findViewById(R.id.version);
        tv.setText(getString(R.string.version) + ": " + versionName);
        return view;
    }
}