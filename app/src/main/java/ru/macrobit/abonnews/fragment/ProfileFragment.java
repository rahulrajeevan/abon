package ru.macrobit.abonnews.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import java.net.CookieHandler;
import java.net.CookieManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.model.Author;
import ru.macrobit.abonnews.model.ErrorJson;
import ru.macrobit.abonnews.utils.API;
import ru.macrobit.abonnews.utils.GsonUtils;
import ru.macrobit.abonnews.utils.ImageUtils;
import ru.macrobit.abonnews.utils.Utils;

public class ProfileFragment extends EnvFragment implements OnTaskCompleted, View.OnClickListener {

    private ImageView mAvatar;
    private TextView mUrl;
    private TextView mName;
    private TextView mEmail;
    private Button mLogout;
    private Button mAuthorization;
//    private Button mSocAuthorization;
    private Button mRegistration;
    private Button mChangePass;
    private boolean isCookieExist;
    View mParentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        isCookieExist = Utils.isCookiesExist(getActivity());
        mParentView = inflater.inflate(R.layout.fragment_profile,
                container, false);
        if (Utils.isConnected(getActivity())) {
            showProgressDialog(getString(R.string.loading_profile));
            API.IGetProfileInfo getProfileInfo = API.getRestAdapter().create(API.IGetProfileInfo.class);
            getProfileInfo.getProfileInfo(new Callback<Author>() {
                @Override
                public void success(Author author, Response response) {
                    initProfile(author);
                    initFragment(mParentView);
                }
                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    hideProgressDialog();
                    setVisibility(mParentView);
                }
            });
//            new GetRequest(this, Utils.loadCookieFromSharedPreferences(getActivity())).execute(Values.PROFILE);
        }
        initFragment(mParentView);
        return mParentView;
    }

    private void initProfile(Author author) {
        ImageUtils.getUIL(getActivity()).displayImage(author.getAvatar(), mAvatar);
        mName.setText(author.getFirstName());
        mUrl.setText(author.getUrl());
        mEmail.setText(author.getEmail());
        hideProgressDialog();
    }

    private void initFragment(View v) {
        setVisibility(v);
        mAvatar = (ImageView) v.findViewById(R.id.profile_avatar);
        mName = (TextView) v.findViewById(R.id.profile_name);
        mEmail = (TextView) v.findViewById(R.id.profile_email);
        mUrl = (TextView) v.findViewById(R.id.profile_url);
        mLogout = (Button) v.findViewById(R.id.profile_logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.deleteCookies(getActivity());
                CookieHandler.setDefault(new CookieManager());
                getActivity().finish();
//                remove(Values.PROFILE_TAG);
            }
        });
        mChangePass = (Button) v.findViewById(R.id.profile_change_pass);
        mAuthorization = (Button) v.findViewById(R.id.profile_authorization);
//        mSocAuthorization = (Button) v.findViewById(R.id.profile_soc_auto);
        mRegistration = (Button) v.findViewById(R.id.profile_reg);
        mAuthorization.setOnClickListener(this);
//        mSocAuthorization.setOnClickListener(this);
        mRegistration.setOnClickListener(this);
        mChangePass.setOnClickListener(this);
    }

    private void setVisibility(View v) {
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
    }

    @Override
    public void onTaskCompleted(String result) {
        try {
            try {
                Author author = GsonUtils.fromJson(result, Author.class);
                ImageUtils.getUIL(getActivity()).displayImage(author.getAvatar(), mAvatar);
                mName.setText(author.getFirstName());
                mUrl.setText(author.getUrl());
                mEmail.setText(author.getEmail());
            } catch (JsonSyntaxException e) {
                ErrorJson[] error = GsonUtils.fromJson(result, ErrorJson[].class);
                if (error[0].getCode().equals(getString(R.string.code_error))) {
                    isCookieExist = false;
                    setVisibility(mParentView);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
        }
        hideProgressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_authorization:
                popBackStack();
                add(new AuthorizationFragment(), Values.AUTHORIZATION_TAG);
                break;
//            case R.id.profile_soc_auto:
////                hide(this.getTag());
//                popBackStack();
//                break;
            case R.id.profile_reg:
                popBackStack();
                add(new RegistrationFragment(), Values.REGISTRATION_TAG);
                break;
            case R.id.profile_change_pass:
                showDialog(new DialogChangePasswordFragment(), Values.DIALOG_TAG);
                break;
        }
    }



}
