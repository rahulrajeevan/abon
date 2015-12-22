package ru.macrobit.abonnews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.model.Auth;
import ru.macrobit.abonnews.model.AuthorizationResponse;
import ru.macrobit.abonnews.utils.API;
import ru.macrobit.abonnews.utils.Utils;
import ru.ulogin.sdk.UloginAuthActivity;

public class AuthorizationFragment extends EnvFragment {
    EditText mLogin;
    EditText mPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_autorization,
                container, false);
        addListenerToEditText(view, getActivity());
        Button button = (Button) view.findViewById(R.id.auto_button);
        mLogin = (EditText) view.findViewById(R.id.auto_login);
        mPass = (EditText) view.findViewById(R.id.auto_pass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnected(getActivity())) {
                    API.IAuthorization authorization = API.getRestAdapter().create(API.IAuthorization.class);
                    authorization.auth(new Auth(mLogin.getText().toString(), mPass.getText().toString()), new Callback<AuthorizationResponse>() {
                        @Override
                        public void success(AuthorizationResponse resp, Response response) {
                            Utils.saveCookieToSharedPreferences(getActivity());
                            makeText(R.string.success_authorization);
                            getActivity().finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            makeText(R.string.error_authorization);
                            error.printStackTrace();
                        }
                    });
                }
//                if (Utils.isConnected(getActivity()))
//                    new AuthorizationRequest(AuthorizationFragment.this, mLogin.getText().toString(), mPass.getText().toString()).execute(Values.AUTHORIZATION);
            }
        });

        Button button1 = (Button) view.findViewById(R.id.profile_soc_auto);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runUlogin();
            }
        });
        return view;
    }

    public void runUlogin() {
        Intent intent = new Intent(getActivity(), UloginAuthActivity.class);

        String[] providers = {"vkontakte", "odnoklassniki", "yandex", "google", "facebook"};
//                getResources()
//                .getStringArray(ru.ulogin.sdk.R.array.ulogin_providers);
        String[] mandatory_fields = new String[]{"first_name", "last_name", "email"};
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
