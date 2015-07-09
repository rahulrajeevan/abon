package ru.macrobit.abonnews.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.AddDataRequest;
import ru.macrobit.abonnews.model.News;

public class AddPostFragment extends EnvFragment implements OnTaskCompleted {

    private EditText mTitle;
    private EditText mContent;
    private Button mPostButton;
    private Button mPickMedia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_add_post,
                container, false);
        initFragment(view);
        return view;
    }

    private void initFragment(View parent) {
        mTitle = (EditText) parent.findViewById(R.id.addTitle);
        mContent = (EditText) parent.findViewById(R.id.addContent);
        mPostButton = (Button) parent.findViewById(R.id.addNewsButton);
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                News news = new News(mTitle.getText().toString(), mContent.getText().toString());
                String json = GsonUtils.toJson(news);
//                String s = Utils.convertToHex(json);
                if (Utils.isConnected(getActivity())) {
                    new AddDataRequest(AddPostFragment.this, Utils.loadCookieFromSharedPreferences(Values.COOKIES,
                            Utils.getPrefs(getActivity())), json).execute(Values.POSTS);
                    showDialog(getString(R.string.loading_add));
                }
            }
        });
        mPickMedia = (Button) parent.findViewById(R.id.button2);
        mPickMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                if (Utils.isConnected(getActivity())) {
                    getActivity().startActivityForResult(photoPickerIntent, Values.MEDIA_RESULT);
                }
            }
        });
    }

    @Override
    public void onTaskCompleted(String result) {
        hideDialog();
        try {
            News news = GsonUtils.fromJson(result, News.class);
            if (news.getStatus().equals("pending")) {
                Toast.makeText(getActivity(), getString(R.string.added_news), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
        }
        getActivity().onBackPressed();
    }
}
