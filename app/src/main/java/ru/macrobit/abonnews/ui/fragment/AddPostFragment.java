package ru.macrobit.abonnews.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.AddDataRequest;
import ru.macrobit.abonnews.model.News;

public class AddPostFragment extends EnvFragment {

    private EditText mTitle;
    private EditText mContent;
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_add_post,
                container, false);
        initFragment(view);
        return view;
    }

    private void initFragment(View parent) {
        mTitle = (EditText) parent.findViewById(R.id.addTitle);
        mContent = (EditText) parent.findViewById(R.id.addContent);
        mButton = (Button) parent.findViewById(R.id.addNewsButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                News news = new News(mTitle.getText().toString(), mContent.getText().toString());
                String json = GsonUtils.toJson(news);
                new AddDataRequest(null, Utils.loadCookieFromSharedPreferences(Values.COOKIES,
                        Utils.getPrefs(getActivity())),json).execute(Values.POSTS);
            }
        });
    }
}
