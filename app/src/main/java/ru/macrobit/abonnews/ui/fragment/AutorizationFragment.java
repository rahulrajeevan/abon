package ru.macrobit.abonnews.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.loader.AutorizationRequest;
import ru.ulogin.sdk.UloginAuthActivity;

public class AutorizationFragment extends EnvFragment {
    public final int REQUEST_ULOGIN = 1;
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
//        runUlogin();
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.auto_button);
        mLogin = (EditText) view.findViewById(R.id.auto_login);
        mPass = (EditText) view.findViewById(R.id.auto_pass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AutorizationRequest(mLogin.getText().toString(), mPass.getText().toString()).execute(Values.AUTORIZATION);
            }
        });
        return view;
    }

    public void runUlogin() {
        Intent intent = new Intent(getActivity(), UloginAuthActivity.class);

        String[] providers = getResources()
                .getStringArray(ru.ulogin.sdk.R.array.ulogin_providers);
        String[] mandatory_fields = new String[]{"first_name", "last_name"};
        String[] optional_fields = new String[]{"nickname", "photo"};

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
        startActivityForResult(intent, REQUEST_ULOGIN);
    }
}
