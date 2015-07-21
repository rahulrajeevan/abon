package ru.macrobit.abonnews.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.loader.RegistrationRequest;

public class RegistrationFragment extends EnvFragment implements OnTaskCompleted {
    private EditText mLogin;
    private EditText mEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_registration,
                container, false);
        addListenerToEditText(view, getActivity());
        Button button = (Button) view.findViewById(R.id.reg_button);
        mLogin = (EditText) view.findViewById(R.id.reg_login);
        mEmail = (EditText) view.findViewById(R.id.reg_email);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(getString(R.string.loading_request));
                new RegistrationRequest(RegistrationFragment.this, mEmail.getText().toString(), mLogin.getText().toString()).execute(Values.REGISTRY);
            }
        });
        return view;
    }

    @Override
    public void onTaskCompleted(String result) {
        hideProgressDialog();
//        if (result.equals(getString(R.string.success_reg))) {
            makeText(getString(R.string.reg_message));
//        } else {
//            makeText(getString(R.string.reg_error));
//        }
        getActivity().finish();
    }
 }
