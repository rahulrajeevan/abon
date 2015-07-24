package ru.macrobit.abonnews.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.AddDataRequest;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.NewsAdd;
import ru.macrobit.abonnews.model.UploadedMedia;

public class AddPostFragment extends EnvFragment implements OnTaskCompleted, View.OnClickListener {

    private EditText mTitle;
    private EditText mContent;
    private Button mPostButton;
    private Button mPickImage;
    private Button mPickVideo;
    String mImages;
    String mVideos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        mImages = "";
        View view = inflater.inflate(R.layout.fragment_add_post,
                container, false);
        addListenerToEditText(view, getActivity());
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
                String title = mTitle.getText().toString();
                String content = mImages + mContent.getText().toString();
                if (!title.equals("")) {
                    if (!content.equals("")) {
                        NewsAdd news = new NewsAdd(title, content);
                        String json = GsonUtils.toJson(news);
                        if (Utils.isConnected(getActivity())) {
                            new AddDataRequest(AddPostFragment.this, Utils.loadCookieFromSharedPreferences(getActivity()), json, null).execute(Values.POSTS);
                            showProgressDialog(getString(R.string.loading_add));
                        }
                    } else {
                        makeText(getString(R.string.need_content));
                    }
                } else {
                    makeText(getString(R.string.need_title));
                }
            }
        });
        mPickImage = (Button) parent.findViewById(R.id.addImageButton);
        mPickVideo = (Button) parent.findViewById(R.id.addVideoButton);
        mPickImage.setOnClickListener(this);
        mPickVideo.setOnClickListener(this);
    }

    @Override
    public void onTaskCompleted(String result) {
        try {
            News news = GsonUtils.fromJson(result, News.class);
            if (news.getStatus().equals("pending")) {
                makeText(getString(R.string.added_news));
            }
            hideProgressDialog();
            getActivity().onBackPressed();
        } catch (Exception e) {
            try {
                UploadedMedia media = GsonUtils.fromJson(result, UploadedMedia.class);
                String url = media.getPath();
                if (url.contains(".jpg") || url.contains(".png")) {
                    mImages += "<img src=\"" + url + "\" alt=\"\" />" + "\n";
                } else {
                    mVideos += url + "\n";
                }
                makeText(getString(R.string.image_attached));
            } catch (Exception e1) {
                makeText(getString(R.string.server_error));
            }
        }
        hideProgressDialog();

    }

    @Override
    public void onClick(View v) {
        Intent pickerIntent = new Intent();
        switch (v.getId()) {
            case R.id.addImageButton:
                pickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                break;
            case R.id.addVideoButton:
                pickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                break;
        }
        if (Utils.isConnected(getActivity())) {
            getActivity().startActivityForResult(pickerIntent, Values.MEDIA_RESULT);
        }
    }
}