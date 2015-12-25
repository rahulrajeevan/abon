package ru.macrobit.abonnews.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.activity.FragmentActivity;
import ru.macrobit.abonnews.adapter.CustomPagerAdapter;
import ru.macrobit.abonnews.adapter.NewsAdapter;
import ru.macrobit.abonnews.model.Ads;
import ru.macrobit.abonnews.model.FullNews;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.ShortNews;
import ru.macrobit.abonnews.utils.API;
import ru.macrobit.abonnews.utils.GsonUtils;
import ru.macrobit.abonnews.utils.ImageUtils;
import ru.macrobit.abonnews.utils.NewsUtils;
import ru.macrobit.abonnews.utils.Utils;


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
    private View mHeader;
    private int adCount = 0;
    private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;
    private TextView mSearchResults;
    private ViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    private CustomPagerAdapter mPagerAdapter;
//    private ArrayList<ShortNews> mNews;

//    private boolean isNewsLoading = false;
    private OkHttpClient client = new OkHttpClient();
    Executor executor = Executors.newSingleThreadExecutor();
    private RestAdapter restAdapter = new RestAdapter.Builder()
            .setExecutors(executor, executor)
            .setEndpoint(Values.URL)
            .setClient(new OkClient(client))
            .build();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_newslist,
                container, false);
        initFragment(view);
        getStickyNews();
        return view;
    }

    private void initFragment(View parent) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) parent.findViewById(R.id.refresh);
        createFooter();
        Ads ad = Utils.getAd(Values.AD_TOP, getActivity());
        createFooter();
        mListView = (ListView) parent.findViewById(R.id.listView);

//        CustomPagerAdapter adapter = new CustomPagerAdapter(getActivity(), arrayList);
//        viewPager.setAdapter(adapter);
//        indicator.setViewPager(viewPager);
//        viewPager.setCurrentItem(0);
        if (ad != null) {
            if (ad.getAdTarget() != null) {
                String link = ad.getAdTarget();
                createHeader(link);
                mListView.addHeaderView(mHeader, null, true);
            } else {
                mListView.addHeaderView(mHeader, null, false);
            }
        }
        mSearchResults = (TextView) parent.findViewById(R.id.searchResults);
        mListView.addFooterView(mFooter, null, false);
        mProgressBar = (ProgressBar) parent.findViewById(R.id.searchProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);
//        mProgressDialog = new ProgressDialog(getActivity());
//        mProgressDialog.setMessage(getString(R.string.loading));
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mProgressDialog.setIndeterminate(true);
//        mProgressDialog.show();
        FloatingActionButton button = (FloatingActionButton) parent.findViewById(R.id.float_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isCookiesExist(getActivity())) {
                    add(new AddPostFragment(), Values.ADD_TAG);
                } else {
                    add(new ProfileFragment(), Values.PROFILE_TAG);
                }
            }
        });
    }

    private void setCursorToSearchView(SearchView search) {
        final EditText searchTextView = (EditText) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor_drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.news_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
        SearchView search = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        setCursorToSearchView(search);
        search.setQueryHint(getString(R.string.search));
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getStickyNews();
                return false;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                Handler handler = new Handler();
                Runnable delayedAction = null;
                if (newText.length() > 2) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    delayedAction = new Runnable() {
                        @Override
                        public void run() {
                            searchNews(newText);
                        }
                    };
                    handler.postDelayed(delayedAction, 1000);
                }
                return false;
            }
        });

    }

    private void listViewInit(ArrayList<ShortNews> newsList) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.primary_dark, R.color.accent);
        if (mAdapter == null) {
            mAdapter = new NewsAdapter(getActivity(), R.layout.item_news, newsList);
//            if (mListView.getFooterViewsCount() == 0) {
//
//            }
            mListView.setAdapter(mAdapter);
        } else {
            for (ShortNews s : newsList)
                mAdapter.add(s);
        }
        if (isSearchList) {
            mListView.removeFooterView(mFooter);
            mAdapter = new NewsAdapter(getActivity(), R.layout.item_news, newsList);
            mListView.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int x =  mListView.getHeaderViewsCount();
                if (!mNews.get(position - x).isAdv()) {
                    ShortNews shortNews = (ShortNews) mListView.getAdapter().getItem(position);
                    News news = mNews.get(position - mListView.getHeaderViewsCount());
                    FullNews fullNews = new FullNews(shortNews, news.getContent(), news.getLink());
                    Bundle bundle = new Bundle();
                    bundle.putString(Values.TAG, Values.DETAIL_TAG);
                    Intent intent = new Intent(getActivity(), FragmentActivity.class);
                    Utils.saveToSharedPreferences(Values.FULL_NEWS, GsonUtils.toJson(fullNews), getActivity());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    String url = mAdapter.getItem(position).getUrl();
                    if (url == null || url.contains("")) {

                    } else {
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                }
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
                if ((lastInScreen == totalItemCount-10) && !(isEndNewsList)) {
                    isLastItemVisible = false;
                    getNewsFromServer();
                }
                if ((lastInScreen >= totalItemCount) && isEndNewsList) {
                    if (!isLastItemVisible) {
                        try {
                            isLastItemVisible = true;
                            if (!isSearchList)
                                makeText(getActivity().getString(R.string.list_end));
                            mListView.removeFooterView(mFooter);
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        API.ISearchNews searchNews = restAdapter.create(API.ISearchNews.class);
        searchNews.searchNews(searchWord, new Callback<List<News>>() {
            @Override
            public void success(final List<News> newses, Response response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initNewsList(newses.toArray(new News[newses.size()]));
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
//        new GetRequest(NewsFragment.this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, Values.SEARCH + Utils.convertToHex(searchWord));
    }

    private void createPager(ArrayList<ShortNews> arrayList, ArrayList<News> news) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.item_viewpager, null, false);
        CirclePageIndicator indicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(getActivity(), arrayList, news);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        indicator.setViewPager(viewPager);
        mListView.addHeaderView(v);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int current = viewPager.getCurrentItem() + 1 > viewPager.getAdapter().getCount()-1 ? 0 : viewPager.getCurrentItem() + 1;
                        viewPager.setCurrentItem(current);
                    }
                });
            }
        };
        timer.schedule(timerTask, 5000, 5000);
    }

    private void getStickyNews() {
        API.IGetNews getNews = restAdapter.create(API.IGetNews.class);
        getNews.getNews(-1, 1, new Callback<List<News>>() {
            @Override
            public void success(final List<News> newses, Response response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        initNewsList(newses.toArray(new News[newses.size()]));
                        ArrayList<ShortNews> news = NewsUtils.generateShortNews(new ArrayList<>(newses), getActivity());
                        createPager(news, new ArrayList<News>(newses));
//                        Fragment fragment = new VewPagerFragment(news);
//                        FragmentManager mManager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction mTransaction = mManager.beginTransaction();
//                        mTransaction.add(R.id.fragment_container_viewPager, fragment, "tag");
//                        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                        mTransaction.addToBackStack("tag");
//                        mTransaction.commit();

                        getNewsFromServer();
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

            }
        });
    }

    private void getNewsFromServer() {
//        isNewsLoading = true;
        if (Utils.isConnected(getActivity())) {
            if (!isEndNewsList && !isSearchList) {
                API.IGetNews getNews = restAdapter.create(API.IGetNews.class);
                mPage++;
                getNews.getNews(mPage, 0, new Callback<List<News>>() {
                    @Override
                    public void success(final List<News> newses, Response response) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initNewsList(newses.toArray(new News[newses.size()]));
//                                isNewsLoading = false;
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
//                new GetRequest(NewsFragment.this).execute(Values.GET_PAGE_POSTS + mPage);
            } else {

            }
        }
    }

    @Override
    public void onCreate(Bundle arg0) {
        setHasOptionsMenu(true);
        super.onCreate(arg0);
    }

    private View createHeader(final String link) {
        mHeader = getActivity().getLayoutInflater().inflate(R.layout.header, null, false);
        mHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = link;
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        ImageView img = (ImageView) mHeader.findViewById(R.id.imageAd);
        ImageUtils.getUIL(getActivity()).displayImage(Utils.getAd(Values.AD_TOP, getActivity()).getAdImg(), img);
        return mHeader;
    }

    private ArrayList<News> addToNewsArray(News[] news, ArrayList<News> arrayList) {
        Ads ad = Utils.getAd(Values.AD_LIST, getActivity());
        if (ad != null) {
            for (int i = 0; i < news.length; i++) {
                if ((mNews.size()) % 6 == adCount && mNews.size() > 0) {
                    adCount++;
                    arrayList.add(new News(ad.getAdImg(), ad.getAdTarget(), true));
                    mNews.add(new News(ad.getAdImg(), ad.getAdTarget(), true));
                }
                arrayList.add(news[i]);
                mNews.add(news[i]);
            }
        } else {
            arrayList.addAll(Arrays.asList(news));
            mNews.addAll(arrayList);
        }
        return arrayList;
    }

    private void initNewsList(News[] newses) {
        ArrayList<News> news = new ArrayList<>();
        if (!isSearchList) {
            news = addToNewsArray(newses, news);
        } else {
            news = addToNewsArray(newses, news);
            if (news.size() == 0) {
                mSearchResults.setVisibility(View.VISIBLE);
                mListView.setAdapter(null);
            } else {
                mSearchResults.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }
            mProgressBar.setVisibility(View.GONE);
        }
        if (news.size() > 0) {
            ArrayList<ShortNews> newsList = NewsUtils.generateShortNews(news, getActivity());
            listViewInit(newsList);
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            isEndNewsList = true;
            mListView.removeFooterView(mFooter);
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        try {
            ArrayList<News> news = new ArrayList<>();
            News[] newses = GsonUtils.fromJson(result, News[].class);
            if (!isSearchList) {
                news = addToNewsArray(newses, news);
            } else {
                news = addToNewsArray(newses, news);
                if (news.size() == 0) {
                    mSearchResults.setVisibility(View.VISIBLE);
                    mListView.setAdapter(null);
                } else {
                    mSearchResults.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
//                    mNews.clear();
                }
                mProgressBar.setVisibility(View.GONE);
            }
            if (news.size() > 0) {
                ArrayList<ShortNews> newsList = NewsUtils.generateShortNews(news, getActivity());
                listViewInit(newsList);
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                isEndNewsList = true;
                mListView.removeFooterView(mFooter);
            }
            mProgressDialog.hide();
        } catch (Exception e) {
            mListView.removeFooterView(mFooter);
            makeText(getString(R.string.server_error));
        }
    }

    private void getNewNewsList() {
        mNews.clear();
        mListView.setVisibility(View.VISIBLE);
        mSearchResults.setVisibility(View.GONE);
        isSearchList = false;
        mPage = 0;
        adCount = 0;
        mAdapter = null;
        isLastItemVisible = false;
        isEndNewsList = false;
        mSwipeRefreshLayout.setRefreshing(true);
        getStickyNews();
//        getNewsFromServer();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        getNewNewsList();
    }
}
