package ru.macrobit.abonnews.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.model.Message;
import ru.macrobit.abonnews.model.Reg;
import ru.macrobit.abonnews.utils.API;
import ru.macrobit.abonnews.utils.GsonUtils;

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
                API.IRegistration registration = API.getRestAdapter().create(API.IRegistration.class);
                registration.reg(new Reg(mLogin.getText().toString(), mEmail.getText().toString()), new Callback() {
                    @Override
                    public void success(Object o, Response response) {
                        hideProgressDialog();
                        makeText(getString(R.string.reg_message));
                        getActivity().finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
//                new RegistrationRequest(RegistrationFragment.this, mEmail.getText().toString(), mLogin.getText().toString()).execute(Values.REGISTRY);
            }
        });
        return view;
    }

    @Override
    public void onTaskCompleted(String result) {
        hideProgressDialog();
        try {
            Message[] message = GsonUtils.fromJson(result, Message[].class);
            String s = "<strong>ОШИБКА</strong>: ";
            makeText(message[0].getMessage().replace(s, ""));
        } catch (Exception e) {
            makeText(getString(R.string.reg_message));
            getActivity().finish();
        }
    }
 }
