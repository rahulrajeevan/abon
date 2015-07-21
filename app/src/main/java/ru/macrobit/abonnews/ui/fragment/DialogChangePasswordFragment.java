package ru.macrobit.abonnews.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.ChangePassRequest;

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
        mButtonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChangePassRequest(mEditText.getText().toString(), Utils.loadCookieFromSharedPreferences(getActivity()), DialogChangePasswordFragment.this).execute(Values.CHANGE_PASS);
            }
        });
        mButtonPositive.setOnClickListener(this);
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
                new ChangePassRequest(mEditText.getText().toString(), Utils.loadCookieFromSharedPreferences(getActivity()), this).execute(Values.CHANGE_PASS);
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
