package ru.macrobit.abonnews.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.controller.ImageUtils;
import ru.macrobit.abonnews.model.FullNews;


public class DetailNewsFragment extends EnvFragment {

    TextView mTitle;
    TextView mDate;
    TextView mBody;
    ImageView mImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_detail,
                container, false);
        initFragment(view);
        return view;
    }

    private void initFragment(View parent) {
        Bundle bundle = this.getArguments();
        mTitle = (TextView) parent.findViewById(R.id.title);
        mDate = (TextView) parent.findViewById(R.id.date);
        mBody = (TextView) parent.findViewById(R.id.body);
        mImage = (ImageView) parent.findViewById(R.id.imageView);
        FullNews news = bundle.getParcelable("data");
        mTitle.setText(news.getTitle());
        mDate.setText(news.getDate());
        Spanned spanned = Html.fromHtml(news.getBody());
        mBody.setText(spanned);
        ImageUtils.getUIL(getActivity()).displayImage(news.getImageUrl(), mImage);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }
}

