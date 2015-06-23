package ru.macrobit.abonnews.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.controller.ImageUtils;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.model.Author;

public class ProfileFragment extends EnvFragment implements OnTaskCompleted{

    ImageView mAvatar;
    TextView mUrl;
    TextView mName;
    TextView mEmail;
    Button mLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_profile,
                container, false);
        new GetRequest(this, Utils.loadCookieFromSharedPreferences(Values.COOKIES, Utils.getPrefs(getActivity()))).execute(Values.PROFILE);
        initFragment(view);
        return view;
    }

    private void initFragment(View v) {
        mAvatar = (ImageView) v.findViewById(R.id.profile_avatar);
        mName = (TextView) v.findViewById(R.id.profile_name);
        mEmail = (TextView) v.findViewById(R.id.profile_email);
        mUrl = (TextView) v.findViewById(R.id.profile_url);
        mLogout = (Button) v.findViewById(R.id.profile_logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.deleteCookies(getActivity());
                remove(Values.PROFILE_TAG);
            }
        });
    }

    @Override
    public void onTaskCompleted(String result) {
        Author author = GsonUtils.fromJson(result, Author.class);
        ImageUtils.getUIL(getActivity()).displayImage(author.getAvatar(), mAvatar);
        mName.setText(author.getFirstName());
        mUrl.setText(author.getUrl());
        mEmail.setText(author.getUrl());
    }
}
