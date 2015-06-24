package ru.macrobit.abonnews.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.client.CookieStore;

import ru.macrobit.abonnews.OnAuthorizationTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.Utils;

public class RegistrationFragment extends EnvFragment implements OnAuthorizationTaskCompleted, View.OnClickListener {
    EditText mLogin;

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
        mLogin = (EditText) view.findViewById(R.id.auto_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new AuthorizationRequest(RegistrationFragment.this, mLogin.getText().toString(), mPass.getText().toString()).execute(Values.AUTHORIZATION);
            }
        });
        return view;
    }

    @Override
    public void onAutorizationTaskCompleted(CookieStore result) {
        Utils.saveCookieToSharedPreferences(Values.COOKIES, result, Utils.getPrefs(getActivity()));
        remove(Values.AUTHORIZATION_TAG);
    }

    @Override
    public void onClick(View view) {

    }
}
