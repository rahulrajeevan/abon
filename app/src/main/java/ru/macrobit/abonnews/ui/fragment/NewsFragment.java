package ru.macrobit.abonnews.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ru.macrobit.abonnews.NewsAdapter;
import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.controller.NewsUtils;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.model.FullNews;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.ShortNews;


public class NewsFragment extends EnvFragment implements OnTaskCompleted, SwipeRefreshLayout.OnRefreshListener {

    ListView mListView;
    News[] mNews;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_newslist,
                container, false);

        mListView = (ListView) view.findViewById(R.id.listView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        getNewsFromServer();
        return view;
    }

    private void UiInit(ArrayList<ShortNews> newsList) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_light);
        NewsAdapter adapter = new NewsAdapter(getActivity(), R.layout.news_item, newsList);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShortNews shortNews = (ShortNews) mListView.getAdapter().getItem(position);
                News news = mNews[position];
                FullNews fullNews = new FullNews(shortNews, news.getContent());
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", fullNews);
                add(new DetailNewsFragment(), bundle);
            }
        });
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
        mNews = GsonUtils.fromJson(result, News[].class);
        ArrayList<ShortNews> newsList = NewsUtils.generateShortNews(mNews);
        UiInit(newsList);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        getNewsFromServer();
    }
}
