package ru.macrobit.abonnews.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ru.macrobit.abonnews.NewsAdapter;
import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.controller.NewsUtils;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.ShortNews;


public class NewsFragment extends Fragment implements OnTaskCompleted{

    ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = (RelativeLayout) inflater.inflate(R.layout.fragment_newslist,
                container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        getNewsFromServer();
        return view;
    }

    private void listViewInit(ArrayList<ShortNews> newsList) {
        NewsAdapter adapter = new NewsAdapter(getActivity(), R.layout.news_item, newsList);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getNewsFromServer() {
        new GetRequest(NewsFragment.this).execute(Values.GET_POST);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    public void onTaskCompleted(String result) {
        News[] news = GsonUtils.fromJson(result, News[].class);
        ArrayList<ShortNews> newsList = NewsUtils.generateShortNews(news);
        listViewInit(newsList);
    }
}
