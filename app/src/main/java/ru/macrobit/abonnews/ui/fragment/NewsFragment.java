package ru.macrobit.abonnews.ui.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

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

    private ListView mListView;
    private ArrayList<News> mNews = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mPage = 0;
    private boolean isEndNewsList = false;
    private boolean isSearchList = false;
    private boolean isLastItemVisible = false;
    private NewsAdapter mAdapter;
    private View mFooter;

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
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.float_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replace(new AutorizationFragment());
            }
        });
        getNewsFromServer();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
        mSwipeRefreshLayout.setColorSchemeColors(R.color.abc_search_url_text, R.color.ulogin_provider_diviter);
        if (mAdapter == null) {
            mAdapter = new NewsAdapter(getActivity(), R.layout.news_item, newsList);
            createFooter();
            mListView.addFooterView(mFooter);
            mListView.setAdapter(mAdapter);
        } else {
            for (ShortNews s : newsList)
                mAdapter.add(s);
        }
//        mListView.removeFooterView(mFooter);
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShortNews shortNews = (ShortNews) mListView.getAdapter().getItem(position);
                News news = mNews.get(position);
                FullNews fullNews = new FullNews(shortNews, news.getContent());
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", fullNews);
                add(new DetailNewsFragment(), bundle);
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(isEndNewsList)) {
                    isLastItemVisible = false;
                    getNewsFromServer();
                }
                if ((lastInScreen == totalItemCount) && isEndNewsList) {
                    if (!isLastItemVisible) {
                        isLastItemVisible = true;
                        Toast.makeText(getActivity(), getActivity().getString(R.string.list_end), Toast.LENGTH_LONG).show();
                        mListView.removeFooterView(mFooter);
                        mFooter.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void createFooter() {
        mFooter = getActivity().getLayoutInflater().inflate(R.layout.footer, null, false);
    }

    private void searchNews(String searchWord) {
        isSearchList = true;
        new GetRequest(NewsFragment.this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, Values.SEARCH + searchWord);
        mFooter.setVisibility(View.GONE);
    }

    private void getNewsFromServer() {
        if (!isEndNewsList && !isSearchList) {
            mPage++;
            new GetRequest(NewsFragment.this).execute(Values.GET_PAGE_POSTS + mPage);
        } else {

        }
    }

    @Override
    public void onCreate(Bundle arg0) {
        setHasOptionsMenu(true);
        super.onCreate(arg0);
    }

    @Override
    public void onTaskCompleted(String result) {
        News[] news = GsonUtils.fromJson(result, News[].class);
        mNews.addAll(Arrays.asList(news));
        if (news.length > 0) {
            ArrayList<ShortNews> newsList = NewsUtils.generateShortNews(news);
            listViewInit(newsList);
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            isEndNewsList = true;
        }
    }

    @Override
    public void onRefresh() {
        isSearchList = false;
        mPage = 0;
        mAdapter = null;
        isEndNewsList = false;
        mSwipeRefreshLayout.setRefreshing(true);
        getNewsFromServer();
    }
}
