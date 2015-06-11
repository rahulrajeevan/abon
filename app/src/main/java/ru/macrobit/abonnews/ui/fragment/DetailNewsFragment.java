package ru.macrobit.abonnews.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.adapter.MyExpandableAdapter;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.controller.ImageUtils;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.model.Comments;
import ru.macrobit.abonnews.model.FullNews;


public class DetailNewsFragment extends EnvFragment implements OnTaskCompleted{

    TextView mTitle;
    TextView mDate;
    TextView mBody;
    ImageView mImage;
    ExpandableListView mListView;


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
        mTitle = (TextView) parent.findViewById(R.id.det_title);
        mDate = (TextView) parent.findViewById(R.id.det_date);
        mBody = (TextView) parent.findViewById(R.id.det_body);
        mImage = (ImageView) parent.findViewById(R.id.det_imageView);
        mListView = (ExpandableListView) parent.findViewById(R.id.det_listView);
        FullNews news = bundle.getParcelable("data");
        mTitle.setText(news.getTitle());
        mDate.setText(news.getDate());
        Spanned spanned = Html.fromHtml(news.getBody());
        mBody.setText(spanned);
        getComments(news.getId() + "/comments/");
        ImageUtils.getUIL(getActivity()).displayImage(news.getImageUrl(), mImage);
    }

    private void getComments(String url) {
        new GetRequest(DetailNewsFragment.this).execute(Values.GET_POST + url);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    public void onTaskCompleted(String result) {
        Comments[] comments = GsonUtils.fromJson(result, Comments[].class);
        ArrayList<Comments> arrayList = new ArrayList<Comments>(Arrays.asList(comments));
        ArrayList<String> group = new ArrayList<>();
        group.add("Комментарии");
//        ArrayList<ArrayList<Comments>> group = new ArrayList<ArrayList<Comments>>();
//        group.add(arrayList);
//        CommentsAdapter adapter = new CommentsAdapter(getActivity(), group);
        MyExpandableAdapter adapter = new MyExpandableAdapter(group, arrayList);
        adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE),getActivity());

//        adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);

        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

