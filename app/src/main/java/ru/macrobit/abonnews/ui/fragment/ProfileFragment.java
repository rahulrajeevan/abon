package ru.macrobit.abonnews.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.controller.ImageUtils;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.model.Author;
import ru.ulogin.sdk.UloginAuthActivity;

public class ProfileFragment extends EnvFragment implements OnTaskCompleted, View.OnClickListener{

    private ImageView mAvatar;
    private TextView mUrl;
    private TextView mName;
    private TextView mEmail;
    private Button mLogout;
    private Button mAuthorization;
    private Button mSocAuthorization;
    private Button mRegistration;
    private boolean isCookieExist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        isCookieExist = Utils.isCookiesExist(getActivity());
        View view = inflater.inflate(R.layout.fragment_profile,
                container, false);
        if (Utils.isConnected(getActivity()) && isCookieExist) {
            new GetRequest(this, Utils.loadCookieFromSharedPreferences(Values.COOKIES,
                    Utils.getPrefs(getActivity()))).execute(Values.PROFILE);
        }
        initFragment(view);
        return view;
    }

    private void initFragment(View v) {
        View profile = v.findViewById(R.id.profile);
        View authorization = v.findViewById(R.id.authorization);
        TextView needAuthorization = (TextView) v.findViewById(R.id.need_authorization);
        if (isCookieExist) {
            profile.setVisibility(View.VISIBLE);
            needAuthorization.setVisibility(View.GONE);
            authorization.setVisibility(View.GONE);
        } else {
            profile.setVisibility(View.GONE);
            needAuthorization.setVisibility(View.VISIBLE);
            authorization.setVisibility(View.VISIBLE);
        }
        mAvatar = (ImageView) v.findViewById(R.id.profile_avatar);
        mName = (TextView) v.findViewById(R.id.profile_name);
        mEmail = (TextView) v.findViewById(R.id.profile_email);
        mUrl = (TextView) v.findViewById(R.id.profile_url);
        mLogout = (Button) v.findViewById(R.id.profile_logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.deleteCookies(getActivity());
                getActivity().finish();
//                remove(Values.PROFILE_TAG);
            }
        });
        mAuthorization = (Button) v.findViewById(R.id.profile_authorization);
        mSocAuthorization = (Button) v.findViewById(R.id.profile_soc_auto);
        mRegistration = (Button) v.findViewById(R.id.profile_reg);
        mAuthorization.setOnClickListener(this);
        mSocAuthorization.setOnClickListener(this);
        mRegistration.setOnClickListener(this);
    }

    @Override
    public void onTaskCompleted(String result) {
        Author author = GsonUtils.fromJson(result, Author.class);
        ImageUtils.getUIL(getActivity()).displayImage(author.getAvatar(), mAvatar);
        mName.setText(author.getFirstName());
        mUrl.setText(author.getUrl());
        mEmail.setText(Utils.loadFromSharedPreferences(Values.EMAIL, Utils.getPrefs(getActivity())));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_authorization:
                popBackStack();
                add(new AuthorizationFragment(), Values.AUTHORIZATION_TAG);
                break;
            case R.id.profile_soc_auto:
//                hide(this.getTag());
                runUlogin();
                popBackStack();
                break;
            case R.id.profile_reg:
                popBackStack();
                add(new RegistrationFragment(), Values.REGISTRATION_TAG);

            break;
        }
    }

    public void runUlogin() {
        Intent intent = new Intent(getActivity(), UloginAuthActivity.class);

        String[] providers = getResources()
                .getStringArray(ru.ulogin.sdk.R.array.ulogin_providers);
        String[] mandatory_fields = new String[]{"first_name", "last_name"};
        String[] optional_fields = new String[]{"nickname", "photo", "email"};

        intent.putExtra(
                UloginAuthActivity.PROVIDERS,
                new ArrayList(Arrays.asList(providers))
        );
        intent.putExtra(
                UloginAuthActivity.FIELDS,
                new ArrayList(Arrays.asList(mandatory_fields))
        );
        intent.putExtra(
                UloginAuthActivity.OPTIONAL,
                new ArrayList(Arrays.asList(optional_fields))
        );
        getActivity().startActivityForResult(intent, Values.REQUEST_ULOGIN);
//        remove(Values.PROFILE_TAG);
    }

}
