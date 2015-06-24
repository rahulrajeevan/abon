package ru.macrobit.abonnews.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.adapter.MyCommentsAdapter;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.controller.NewsUtils;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.model.FullNews;
import ru.macrobit.abonnews.model.MyComment;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.ShortNews;

public class MyCommentFragment extends EnvFragment implements OnTaskCompleted {

    private ListView mListView;
    private ArrayList<MyComment> mComments;
    private MyCommentsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_my_comment,
                container, false);
        mListView = (ListView) view.findViewById(R.id.mycom_listview);
        if (Utils.isConnected(getActivity())) {
            new GetRequest(this, Utils.loadCookieFromSharedPreferences(Values.COOKIES,
                    Utils.getPrefs(getActivity()))).execute(Values.MY_COMMENTS);
        }
        return view;
    }

    private void initListView() {
        mAdapter = new MyCommentsAdapter(getActivity(), R.layout.my_comments_item, mComments);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String s = null;
                try {
                    s = new GetRequest(null,
                            Utils.loadCookieFromSharedPreferences(Values.COOKIES,
                                    Utils.getPrefs(getActivity())))
                            .execute(Values.POSTS + mComments.get(position).getPost().getId()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                News news = GsonUtils.fromJson(s, News.class);
                ShortNews shortNews = NewsUtils.generateShortNews(news);
                FullNews fullNews = new FullNews(shortNews, news.getContent(), news.getLink());
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", fullNews);
                add(new DetailNewsFragment(), bundle, Values.DETAIL_TAG);
            }
        });
    }

    @Override
    public void onTaskCompleted(String result) {
        mComments = new ArrayList<>();
        MyComment[] comm = GsonUtils.fromJson(result, MyComment[].class);
        mComments.addAll(Arrays.asList(comm));
        initListView();
    }
}
