package ru.macrobit.abonnews.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.adapter.MyExpandableAdapter;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.controller.ImageUtils;
import ru.macrobit.abonnews.controller.Utils;
import ru.macrobit.abonnews.loader.AddDataRequest;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.model.AddComment;
import ru.macrobit.abonnews.model.Ads;
import ru.macrobit.abonnews.model.Comments;
import ru.macrobit.abonnews.model.FullNews;


public class DetailNewsFragment extends EnvFragment implements OnTaskCompleted, View.OnClickListener {

    private TextView mTitle;
    private TextView mDate;
    private ImageView mImage;
    private ExpandableListView mListView;
    private ProgressBar mCommentProgressBar;
    private String mId;
    private WebView webView;
    private FullNews mNews;
    private View mFooter;
    private int mCommentId = -999;
    private View mImageLayout;
    private ImageView mWebImage;
    private View mLayout;
    public static WebView mShareWebView;
    public static FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    private myWebChromeClient mWebChromeClient;
    private myWebViewClient mWebViewClient;

    String fulljs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        showDialog(getString(R.string.loading_detail));
        View view = inflater.inflate(R.layout.fragment_detail,
                container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(webView, (Object[]) null);

        } catch (ClassNotFoundException cnfe) {
        } catch (NoSuchMethodException nsme) {
        } catch (InvocationTargetException ite) {
        } catch (IllegalAccessException iae) {
        }
    }

    private void initFragment(View parent) {
        webView = (WebView) parent.findViewById(R.id.webView);
        mWebViewClient = new myWebViewClient();
        webView.setWebViewClient(mWebViewClient);
        mWebChromeClient = new myWebChromeClient();
        webView.setWebChromeClient(mWebChromeClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setSaveFormData(true);
        ImageView adHeader = (ImageView) parent.findViewById(R.id.ad_header);
        initAdv(adHeader);
        ImageView adFooter = (ImageView) parent.findViewById(R.id.ad_footer);
        initAdv(adFooter);
        mCustomViewContainer = (FrameLayout) parent.findViewById(R.id.customViewContainer);
        ImageButton vk = (ImageButton) parent.findViewById(R.id.det_vk);
        ImageButton ok = (ImageButton) parent.findViewById(R.id.det_ok);
        ImageButton fb = (ImageButton) parent.findViewById(R.id.det_fb);
        ImageButton tw = (ImageButton) parent.findViewById(R.id.det_tw);
        vk.setOnClickListener(this);
        ok.setOnClickListener(this);
        fb.setOnClickListener(this);
        tw.setOnClickListener(this);
        mShareWebView = (WebView) parent.findViewById(R.id.shareWebView);
        mLayout = parent.findViewById(R.id.det_scroll);
        mLayout.setVisibility(View.GONE);
        mCommentProgressBar = (ProgressBar) parent.findViewById(R.id.det_progress);
        mCommentProgressBar.setVisibility(View.GONE);
        mImageLayout = parent.findViewById(R.id.det_imageLayout);
        mWebImage = (ImageView) parent.findViewById(R.id.det_webImage);
        mTitle = (TextView) parent.findViewById(R.id.det_title);
        mDate = (TextView) parent.findViewById(R.id.det_date);
        mImage = (ImageView) parent.findViewById(R.id.det_imageView);
        mFooter = parent.findViewById(R.id.det_footer);
        mFooter.setVisibility(View.GONE);
        mListView = (ExpandableListView) parent.findViewById(R.id.det_listView);
        mListView.setVisibility(View.GONE);
        String s = Utils.loadFromSharedPreferences(Values.FULL_NEWS, Utils.getPrefs(getActivity()));
        mNews = GsonUtils.fromJson(s, FullNews.class);
        if (mNews.getBody() != null) {
            if (mNews.getBody().contains(mNews.getImageUrl())) {
                mImage.setVisibility(View.GONE);
            }
            webView.loadData(Utils.getHtmlData(mNews.getBody()), "text/html; charset=UTF-8", null);
        }
        Spanned span = Html.fromHtml(mNews.getTitle());
        mTitle.setText(span);
        mDate.setText(mNews.getDate());
        mId = mNews.getId();
        mImageLayout.setOnClickListener(this);
        mWebImage.setOnClickListener(this);
        getComments(mNews.getId() + "/comments/");
        ImageUtils.getUIL(getActivity()).displayImage(mNews.getImageUrl(), mImage);
        Button addComment = (Button) parent.findViewById(R.id.addComment);
        final EditText commentEdit = (EditText) parent.findViewById(R.id.comment);
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
                    new AddDataRequest(null, Utils.loadCookieFromSharedPreferences(Values.COOKIES,
                            Utils.getPrefs(getActivity())), json)
                            .execute(Values.POSTS + mNews.getId() + "/comments/");
                    commentEdit.setText("");
                    Toast.makeText(getActivity(), getString(R.string.comment_moder), Toast.LENGTH_LONG).show();
                } else {
                    add(new ProfileFragment(), Values.PROFILE_TAG);
//                    Intent intent = new Intent(getActivity(), FragmentActivity.class);
//                    intent.putExtra(Values.TAG, Values.PROFILE_TAG);
//                    startActivity(intent);
                }
            }
        });

    }

    private void initAdv(ImageView imageView) {
        final Ads ad = Utils.getAd(Values.AD_DETAIL, getActivity());
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

    private void getComments(String url) {
        if (Utils.isConnected(getActivity())) {
            mCommentProgressBar.setVisibility(View.VISIBLE);
            new GetRequest(DetailNewsFragment.this).execute(Values.POSTS + url);
        }
    }

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

    private void initComments(String result) {
        try {
            if (result != null) {
                Comments[] comments = GsonUtils.fromJson(result, Comments[].class);
                if (comments.length > 0) {
                    final ArrayList<Comments> arrayList = new ArrayList<Comments>(Arrays.asList(comments));
                    ArrayList<String> group = new ArrayList<>();
                    group.add(getActivity().getString(R.string.comments));
                    MyExpandableAdapter adapter = new MyExpandableAdapter(group, arrayList);
                    adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
                    mListView.setAdapter(adapter);
                    mListView.setVisibility(View.VISIBLE);
                    mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v,
                                                    int groupPosition, long id) {
                            setListViewHeight(parent, groupPosition);
                            return false;
                        }
                    });
                    mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView expandableListView, View view, int parentId, int childId, long l) {
                            mCommentId = arrayList.get(childId).getId();
                            return false;
                        }
                    });
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
        }
    }

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

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        MyExpandableAdapter listAdapter = (MyExpandableAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void hide() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mLayout.setVisibility(View.VISIBLE);
        if (mCustomView == null)
            return;
        webView.setVisibility(View.VISIBLE);
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
            mLayout.setVisibility(View.GONE);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
//            webView.setVisibility(View.GONE);
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.setVisibility(View.VISIBLE);
                mLayout.setVisibility(View.VISIBLE);
                mFooter.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.VISIBLE);
                mLayout.setVisibility(View.VISIBLE);
                hideDialog();
            }
        });

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

