package ru.macrobit.abonnews.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.model.ChangePass;
import ru.macrobit.abonnews.utils.API;
import ru.macrobit.abonnews.utils.Utils;

public class DialogChangePasswordFragment extends DialogFragment implements View.OnClickListener, OnTaskCompleted {

    EditText mEditText;
    Button mButtonPositive;
    Button mButtonNegative;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pass, container);
        mEditText = (EditText) view.findViewById(R.id.changePasswordEdit);
        mButtonPositive = (Button) view.findViewById(R.id.positiveButton);
        mButtonNegative = (Button) view.findViewById(R.id.negativeButton);
        mButtonPositive.setOnClickListener(this);
        mButtonNegative.setOnClickListener(this);
        getDialog().setTitle(R.string.change_password);
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positiveButton:
                API.IChangePassword changePassword = API.getRestAdapter().create(API.IChangePassword.class);
                changePassword.changePass(new ChangePass(mEditText.getText().toString()), new Callback<String>() {
                    @Override
                    public void success(String o, Response response) {
                        Utils.deleteCookies(getActivity());
                        Toast.makeText(getActivity(), R.string.need_reauthorize, Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
//                new ChangePassRequest(mEditText.getText().toString(), Utils.loadCookieFromSharedPreferences(getActivity()), this).execute(Values.CHANGE_PASS);
                break;
            case R.id.negativeButton:
                this.dismiss();
                break;
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        Utils.deleteCookies(getActivity());
        Toast.makeText(getActivity(), R.string.need_reauthorize, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }
}
