package ru.macrobit.abonnews.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import ru.macrobit.abonnews.model.Comments;
import ru.macrobit.abonnews.model.FullNews;
import ru.macrobit.abonnews.ui.view.VideoEnabledWebChromeClient;
import ru.macrobit.abonnews.ui.view.VideoEnabledWebView;


public class DetailNewsFragment extends EnvFragment implements OnTaskCompleted {

    private TextView mTitle;
    private TextView mDate;
    private WebView mBody;
    private ImageView mImage;
    private ExpandableListView mListView;
    private ProgressBar mProgressBar;
    private String mId;
    private VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;
    private FullNews mNews;
    private View mFooter;
    private ShareActionProvider mShareActionProvider;
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

    private void initVideo(View parent, String data) {
        webView = (VideoEnabledWebView) parent.findViewById(R.id.webView);

        // Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = parent.findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup) parent.findViewById(R.id.videoLayout); // Your own view, read class comments
        //noinspection all
        View loadingView = getActivity().getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Your code...
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        //noinspection all
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        webView.setWebChromeClient(webChromeClient);
        webView.loadData(getHtmlData(data), "text/html; charset=UTF-8", null);
    }

    private void initFragment(View parent) {
        Bundle bundle = this.getArguments();
        mProgressBar = (ProgressBar) parent.findViewById(R.id.progressBar);
        mTitle = (TextView) parent.findViewById(R.id.det_title);
        mDate = (TextView) parent.findViewById(R.id.det_date);
        mImage = (ImageView) parent.findViewById(R.id.det_imageView);
        mFooter = parent.findViewById(R.id.det_footer);
        mListView = (ExpandableListView) parent.findViewById(R.id.det_listView);
        mNews = bundle.getParcelable("data");
        mTitle.setText(mNews.getTitle());
        mDate.setText(mNews.getDate());
        mId = mNews.getId();
        getComments(mNews.getId() + "/comments/");
        initVideo(parent, mNews.getBody());
        ImageUtils.getUIL(getActivity()).displayImage(mNews.getImageUrl(), mImage);
        Button addComment = (Button) parent.findViewById(R.id.addComment);
        final EditText commentEdit = (EditText) parent.findViewById(R.id.comment);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comments comments = new Comments(commentEdit.getText().toString());
                String json = GsonUtils.toJson(comments);
                new AddDataRequest(null, Utils.loadCookieFromSharedPreferences(Values.COOKIES,
                        Utils.getPrefs(getActivity())), json)
                        .execute(Values.POSTS + mNews.getId() + "/comments/");
            }
        });
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<html><head> " +
                "<style> " +
                "img {max-width:100%%; height:auto !important;width:auto !important; visibility: visible !important;} " +
                ".wp-video {height:auto !important; width:100%% !important; visibility: visible !important;} " +
                ".wp-video-shortcode {height:auto !important; width:100% !important; visibility: visible !important;} " +
                "audio {visibility: visible !important;} " +
                "iframe {height:auto !important; width:100%% !important; visibility: visible !important;} " +
                "</style>" +
                "</head><body style='margin:0; '>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    private void getComments(String url) {
        new GetRequest(DetailNewsFragment.this).execute(Values.POSTS + url);
    }

    @Override
    public void onCreate(Bundle arg0) {
        setHasOptionsMenu(true);
        super.onCreate(arg0);
    }

    @Override
    public void onTaskCompleted(String result) {
        Comments[] comments = GsonUtils.fromJson(result, Comments[].class);
        ArrayList<Comments> arrayList = new ArrayList<Comments>(Arrays.asList(comments));
        ArrayList<String> group = new ArrayList<>();
        group.add("comments");
        MyExpandableAdapter adapter = new MyExpandableAdapter(group, arrayList);
        adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
//        mListView.addFooterView(mFooter);
        mListView.setAdapter(adapter);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.getItem(0).setVisible(false);
        inflater.inflate(R.menu.global, menu);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mNews.getUrl());
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(intent);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    public class ProgressWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mProgressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mListView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
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
            groupItem.setLayoutParams(new ViewGroup.LayoutParams(0,0));
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
//        mFooter.setLayoutParams(new ViewGroup.LayoutParams(0,0));
//        mFooter.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        int footHeight = mFooter.getMeasuredHeight();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}

