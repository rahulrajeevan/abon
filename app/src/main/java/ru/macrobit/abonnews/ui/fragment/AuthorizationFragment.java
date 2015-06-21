package ru.macrobit.abonnews.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.client.CookieStore;

import java.util.ArrayList;
import java.util.Arrays;

import ru.macrobit.abonnews.OnAuthorizationTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.AuthorizationRequest;
import ru.ulogin.sdk.UloginAuthActivity;

public class AuthorizationFragment extends EnvFragment implements OnAuthorizationTaskCompleted, View.OnClickListener {
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
//
        Button button = (Button) view.findViewById(R.id.auto_button);
        Button button1 = (Button) view.findViewById(R.id.auto_soc_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runUlogin();
            }
        });
        mLogin = (EditText) view.findViewById(R.id.auto_login);
        mPass = (EditText) view.findViewById(R.id.auto_pass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthorizationRequest(AuthorizationFragment.this, mLogin.getText().toString(), mPass.getText().toString()).execute(Values.AUTHORIZATION);
            }
        });
        return view;
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
    }

    @Override
    public void onAutorizationTaskCompleted(CookieStore result) {
        Utils.saveCookieToSharedPreferences(Values.COOKIES, result, Utils.getPrefs(getActivity()));
    }

    @Override
    public void onClick(View view) {

    }
}
