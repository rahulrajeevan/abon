package ru.macrobit.abonnews.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.activity.FragmentActivity;
import ru.macrobit.abonnews.adapter.CommentsAdapter;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.model.AddComment;
import ru.macrobit.abonnews.model.Ads;
import ru.macrobit.abonnews.model.Comments;
import ru.macrobit.abonnews.model.FullNews;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.ShortNews;
import ru.macrobit.abonnews.ui.view.DynamicImageView;
import ru.macrobit.abonnews.utils.API;
import ru.macrobit.abonnews.utils.GsonUtils;
import ru.macrobit.abonnews.utils.ImageUtils;
import ru.macrobit.abonnews.utils.NewsUtils;
import ru.macrobit.abonnews.utils.Utils;


public class DetailNewsFragment extends EnvFragment implements OnTaskCompleted, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private TextView mTitle;
    private TextView mDate;
    private ImageView mImage;
    //    private ExpandableListView mListView;
    private ListView mListView;
    private ProgressBar mCommentProgressBar;
    private String mId;
    private WebView mWebView;
    private FullNews mNews;
    private View mFooter;
    private int mCommentId = -999;
    public static View mImageLayout;
    private ImageView mWebImage;
    private View mLayout;
    public static WebView mShareWebView;
    public static FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    private myWebChromeClient mWebChromeClient;
    private myWebViewClient mWebViewClient;
    private ShareActionProvider mShareActionProvider;
    //    private MyExpandableAdapter mAdapter;
    private CommentsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        showProgressDialog(getString(R.string.loading_detail));
        mView = inflater.inflate(R.layout.fragment_detail,
                container, false);
        initFragment(mView);
        return mView;
    }

    @Override
    public void onResume() {
        mWebView.loadData(Utils.getHtmlData(mNews.getBody(), getActivity()), "text/html; charset=UTF-8", null);
        super.onResume();
    }

    @Override
    public void onPause() {
        mWebView.loadData("", "text/html; charset=UTF-8", null);
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(mWebView, (Object[]) null);


        } catch (ClassNotFoundException cnfe) {
        } catch (NoSuchMethodException nsme) {
        } catch (InvocationTargetException ite) {
        } catch (IllegalAccessException iae) {
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_menu, menu);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mNews.getTitle() + "\n" + mNews.getUrl());
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(intent);
    }

    private void initUI(View parent) {
        mWebView = (WebView) parent.findViewById(R.id.webView);
        DynamicImageView adHeader = (DynamicImageView) parent.findViewById(R.id.ad_header);
        initAdv(adHeader);
        DynamicImageView adFooter = (DynamicImageView) parent.findViewById(R.id.ad_footer);
        initAdv(adFooter);
        mCustomViewContainer = (FrameLayout) parent.findViewById(R.id.customViewContainer);
        mShareWebView = (WebView) parent.findViewById(R.id.shareWebView);
        mLayout = parent.findViewById(R.id.det_scroll);
        mSwipeRefreshLayout = (SwipeRefreshLayout) parent.findViewById(R.id.det_swipe);
        mCommentProgressBar = (ProgressBar) parent.findViewById(R.id.det_progress);
        mImageLayout = parent.findViewById(R.id.det_imageLayout);
        mWebImage = (ImageView) parent.findViewById(R.id.det_webImage);
        mTitle = (TextView) parent.findViewById(R.id.det_title);
        mDate = (TextView) parent.findViewById(R.id.det_date);
        mImage = (ImageView) parent.findViewById(R.id.det_imageView);
        mFooter = parent.findViewById(R.id.det_footer);
//        mListView = (ExpandableListView) parent.findViewById(R.id.det_listView);
        mListView = (ListView) parent.findViewById(R.id.det_listView);
    }

    private void setVisibilities() {
        mLayout.setVisibility(View.GONE);
        mCommentProgressBar.setVisibility(View.GONE);
        mFooter.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
    }

    private void setListeners(View parent) {
        ImageButton vk = (ImageButton) parent.findViewById(R.id.det_vk);
        ImageButton ok = (ImageButton) parent.findViewById(R.id.det_ok);
        ImageButton fb = (ImageButton) parent.findViewById(R.id.det_fb);
        ImageButton tw = (ImageButton) parent.findViewById(R.id.det_tw);
        vk.setOnClickListener(this);
        ok.setOnClickListener(this);
        fb.setOnClickListener(this);
        tw.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mImageLayout.setOnClickListener(this);
//        mWebImage.setOnClickListener(this);
        Button addComment = (Button) parent.findViewById(R.id.addComment);
        final EditText commentEdit = (EditText) parent.findViewById(R.id.comment);
        addListenerToEditText(commentEdit, getActivity());
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isCookiesExist(getActivity())) {
                    AddComment comments;
                    if (mCommentId == -999) {
                        comments = new AddComment(commentEdit.getText().toString());
                    } else {
                        comments = new AddComment(commentEdit.getText().toString(), mCommentId);
                    }
                    String json = GsonUtils.toJson(comments);
                    API.IAddComment addComment = API.getRestAdapter().create(API.IAddComment.class);
                    addComment.addComment(comments, new Callback<List<Comments>>() {
                        @Override
                        public void success(List<Comments> commentses, Response response) {
                            initComments(commentses);
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
//                    new AddDataRequest(DetailNewsFragment.this, Utils.loadCookieFromSharedPreferences(getActivity()), json, null)
//                            .execute(Values.POSTS + mNews.getId() + "/comments/");
//                    commentEdit.setText("");
//                    initComments(mNews.getId() + "/comments/");
                    Toast.makeText(getActivity(), getString(R.string.comment_added), Toast.LENGTH_LONG).show();
                } else {
                    add(new ProfileFragment(), Values.PROFILE_TAG);
//                    Intent intent = new Intent(getActivity(), FragmentActivity.class);
//                    intent.putExtra(Values.TAG, Values.PROFILE_TAG);
//                    startActivity(intent);
                }
            }
        });
    }

    private void setWebViews() {
        mWebViewClient = new myWebViewClient();
        mWebView.setWebViewClient(mWebViewClient);
        mWebChromeClient = new myWebChromeClient();
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setSaveFormData(true);


//        mShareWebView.getSettings().setUserAgentString(System.getProperty("http.agent"));
        mShareWebView.getSettings().setJavaScriptEnabled(true);
//        mShareWebView.setWebChromeClient(mWebChromeClient);
//        mShareWebView.getSettings().setSupportMultipleWindows(true);
        mShareWebView.getSettings().setUseWideViewPort(true);
//        mShareWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

    private void setData() {
        Bundle bundle = getArguments();
        String json = null;
        String id = null;
        try {
            id = bundle.getString(Values.POST_ID);
        } catch (NullPointerException e) {
        }
        if (id != null) {
            try {
                json = new GetRequest(null).execute(Values.POSTS + id).get();
                News news = GsonUtils.fromJson(json, News.class);
                ShortNews shortNews = NewsUtils.generateShortNews(news);
                mNews = new FullNews(shortNews, news.getContent(), news.getLink());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                makeText(getString(R.string.server_error));
            }
        } else {
            json = Utils.loadFromSharedPreferences(Values.FULL_NEWS, getActivity());
            mNews = GsonUtils.fromJson(json, FullNews.class);
        }

        if (mNews.getBody() != null) {
            if (mNews.getBody().contains(mNews.getImageUrl())) {
                mImage.setVisibility(View.GONE);
            }
            mWebView.loadData(Utils.getHtmlData(mNews.getBody(), getActivity()), "text/html; charset=UTF-8", null);
        }
        Spanned span = Html.fromHtml(mNews.getTitle());
        mTitle.setText(span);
        mDate.setText(mNews.getDate());
        mId = mNews.getId();
        getComments();
        ImageUtils.getUIL(getActivity()).displayImage(mNews.getImageUrl(), mImage);
    }

    private void initFragment(View parent) {
        initUI(parent);
        setVisibilities();
        setWebViews();
        setData();
        setListeners(parent);
    }

    private void initAdv(DynamicImageView imageView) {
        final Ads ad = Utils.getAd(Values.AD_DETAIL, getActivity());
        if (ad != null) {
            ImageUtils.getUIL(getActivity()).displayImage(ad.getAdImg(), imageView);
            if (ad.getAdTarget() != null) {
                if (!ad.getAdTarget().equals("")) {
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = ad.getAdTarget();
                            if (!url.startsWith("http://") && !url.startsWith("https://"))
                                url = "http://" + url;
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);
                        }
                    });
                }
            }
        }
    }

//    private void getComments() {
//        if (Utils.isConnected(getActivity())) {
//            mCommentProgressBar.setVisibility(View.VISIBLE);
//            new GetRequest(DetailNewsFragment.this).execute(Values.POSTS + mNews.getId() + "/comments/");
//        }
//    }

    @Override
    public void onCreate(Bundle arg0) {
        setHasOptionsMenu(true);
        super.onCreate(arg0);
    }

    @Override
    public void onTaskCompleted(String result) {
        mCommentProgressBar.setVisibility(View.GONE);
        initComments(result);
    }

    private void initComments(List<Comments> commentses) {
        ArrayList<Comments> group = new ArrayList<>(commentses);
//                    group.add(getActivity().getString(R.string.comments) + " (" + comments.length + ")");
        mAdapter = new CommentsAdapter(getActivity(), R.layout.item_comments, group);
        mListView.setAdapter(mAdapter);
        mListView.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
        setListViewHeight(mListView);
    }

    private void getComments() {
        if (Utils.isConnected(getActivity())) {
            mCommentProgressBar.setVisibility(View.VISIBLE);
            API.IGetPostComments getPostComments = API.getRestAdapter().create(API.IGetPostComments.class);
            getPostComments.getPostComments(mNews.getId(), new Callback<List<Comments>>() {
                @Override
                public void success(List<Comments> commentses, Response response) {
                    mCommentProgressBar.setVisibility(View.GONE);
                    initComments(commentses);
                }

                @Override
                public void failure(RetrofitError error) {
                    mCommentProgressBar.setVisibility(View.GONE);
                }
            });
//            new GetRequest(DetailNewsFragment.this).execute(Values.POSTS + mNews.getId() + "/comments/");
        }
    }

    private void initComments(String result) {
        try {
            if (result != null) {
                Comments[] comments = GsonUtils.fromJson(result, Comments[].class);
                if (comments.length > 0) {
                    final ArrayList<Comments> arrayList = new ArrayList<Comments>(Arrays.asList(comments));
                    ArrayList<Comments> group = new ArrayList<>();
                    group.addAll(Arrays.asList(comments));
//                    group.add(getActivity().getString(R.string.comments) + " (" + comments.length + ")");
                    mAdapter = new CommentsAdapter(getActivity(), R.layout.item_comments, group);
                    mListView.setAdapter(mAdapter);
                    mListView.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                    setListViewHeight(mListView);
                }
            }
        } catch (Exception e) {
            try {
                Comments comment = GsonUtils.fromJson(result, Comments.class);
                getComments();
            } catch (Exception e1) {

            }
//            makeText(getString(R.string.server_error));
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

//    private void initComments(String result) {
//        try {
//            if (result != null) {
//                Comments[] comments = GsonUtils.fromJson(result, Comments[].class);
//                if (comments.length > 0) {
//                    final ArrayList<Comments> arrayList = new ArrayList<Comments>(Arrays.asList(comments));
//                    ArrayList<String> group = new ArrayList<>();
//                    group.add(getActivity().getString(R.string.comments) + " (" + comments.length + ")");
//                    mAdapter = new MyExpandableAdapter(group, arrayList);
//                    mAdapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
//                    mListView.setAdapter(mAdapter);
//
//                    mListView.setVisibility(View.VISIBLE);
//                    mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//                        @Override
//                        public void onGroupExpand(int groupPosition) {
//                            setListViewHeight(mListView, groupPosition);
//                        }
//                    });
//                    mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//                        @Override
//                        public boolean onGroupClick(ExpandableListView parent, View v,
//                                                    int groupPosition, long id) {
//                            setListViewHeight(parent, groupPosition);
//                            return false;
//                        }
//                    });
//                    mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//                        @Override
//                        public boolean onChildClick(ExpandableListView expandableListView, View view, int parentId, int childId, long l) {
//                            mCommentId = arrayList.get(childId).getId();
//                            return false;
//                        }
//                    });
//                    mListView.expandGroup(0);
//                    setListViewHeight(mListView, 0);
//                }
//            }
//        } catch (Exception e) {
//            try {
//                Comments comment = GsonUtils.fromJson(result, Comments.class);
//                getComments();
//            } catch (Exception e1) {
//
//            }
////            makeText(getString(R.string.server_error));
//        }
//        mSwipeRefreshLayout.setRefreshing(false);
//    }

    @Override
    public void onClick(View v) {
        mShareWebView.setVisibility(View.VISIBLE);
        mShareWebView.setWebViewClient(new ProgressWebClient());
        switch (v.getId()) {
            case R.id.det_vk:
                mShareWebView.loadUrl(Values.VK + mNews.getUrl() + "&image=" + Values.ABON_LOGO);
                break;
            case R.id.det_ok:
                mShareWebView.loadUrl(Values.OK + mNews.getUrl());
                break;
            case R.id.det_fb:
                mShareWebView.loadUrl(Values.FACEBOOK + mNews.getUrl() + "&p[images][0]=" + Values.ABON_LOGO);
                break;
            case R.id.det_tw:
                mShareWebView.loadUrl(Values.TWITTER + mNews.getUrl());
                break;
        }
    }

    @Override
    public void onRefresh() {
        getComments();
    }

    public class ProgressWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
    }

    private void setListViewHeight(ListView listView) {
        CommentsAdapter listAdapter = (CommentsAdapter) listView.getAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += listItem.getMeasuredHeight();

        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() - 1);
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

//    private void setListViewHeight(ExpandableListView listView,
//                                   int group) {
//        MyExpandableAdapter listAdapter = (MyExpandableAdapter) listView.getExpandableListAdapter();
//        int totalHeight = 0;
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
//                View.MeasureSpec.EXACTLY);
//        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
//            View groupItem = listAdapter.getGroupView(i, false, null, listView);
//            groupItem.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
//            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//
//            totalHeight += groupItem.getMeasuredHeight();
//
//            if (((listView.isGroupExpanded(i)) && (i != group))
//                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
//                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
//                    View listItem = listAdapter.getChildView(i, j, false, null,
//                            listView);
//                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//                    totalHeight += listItem.getMeasuredHeight();
//
//                }
//            }
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        int height = totalHeight
//                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
//        if (height < 10)
//            height = 200;
//        params.height = height;
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//    }

    public void hide() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        FragmentActivity fragmentActivity = (FragmentActivity) DetailNewsFragment.this.getActivity();
        fragmentActivity.getSupportActionBar().show();
        mLayout.setVisibility(View.VISIBLE);
        if (mCustomView == null)
            return;
        mWebView.setVisibility(View.VISIBLE);
        mCustomViewContainer.setVisibility(View.GONE);
        mCustomView.setVisibility(View.GONE);
        mCustomViewContainer.removeView(mCustomView);
        customViewCallback.onCustomViewHidden();
        mCustomView = null;
    }

    class myWebChromeClient extends WebChromeClient {
        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;


        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            onShowCustomView(view, callback);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            FragmentActivity fragmentActivity = (FragmentActivity) DetailNewsFragment.this.getActivity();
            fragmentActivity.getSupportActionBar().hide();
            mLayout.setVisibility(View.GONE);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
//            mWebView.setVisibility(View.GONE);
            mCustomViewContainer.setVisibility(View.VISIBLE);
            mCustomViewContainer.addView(view);
            customViewCallback = callback;
        }

        @Override
        public View getVideoLoadingProgressView() {
            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                mVideoProgressView = inflater.inflate(R.layout.view_loading_video, null);
            }
            return mVideoProgressView;
        }

        @Override
        public Bitmap getDefaultVideoPoster() {
            return super.getDefaultVideoPoster();
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            hide();
        }
    }

    private void onPageLoaded() {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
                mWebView.setVisibility(View.VISIBLE);
                mLayout.setVisibility(View.VISIBLE);
                mFooter.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.VISIBLE);
                mLayout.setVisibility(View.VISIBLE);
                hideProgressDialog();
//            }
//        });

    }

    class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png")) {
                mImageLayout.setVisibility(View.VISIBLE);
                ImageUtils.getUIL(getActivity()).displayImage(url, mWebImage);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            onPageLoaded();
        }
    }

}

