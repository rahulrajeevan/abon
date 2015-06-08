package ru.macrobit.abonnews.ui.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.adapter.NewsAdapter;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.news_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                SearchView search = (SearchView) menu.findItem(R.id.menu_search).getActionView();
                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchNews(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
        }

    }

    private void listViewInit(ArrayList<ShortNews> newsList) {
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

    private void searchNews(String searchWord) {
        new GetRequest(NewsFragment.this).execute(Values.SEARCH + searchWord);
    }

    private void getNewsFromServer() {
        new GetRequest(NewsFragment.this).execute(Values.GET_POST);
    }

    @Override
    public void onCreate(Bundle arg0) {
        setHasOptionsMenu(true);
        super.onCreate(arg0);
    }

    @Override
    public void onTaskCompleted(String result) {
        mNews = GsonUtils.fromJson(result, News[].class);
        ArrayList<ShortNews> newsList = NewsUtils.generateShortNews(mNews);
        listViewInit(newsList);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        getNewsFromServer();
    }
}
