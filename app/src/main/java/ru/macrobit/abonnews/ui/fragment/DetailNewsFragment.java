package ru.macrobit.abonnews.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.controller.ImageUtils;
import ru.macrobit.abonnews.controller.NewsUtils;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.model.FullNews;
import ru.macrobit.abonnews.model.Media;
import ru.macrobit.abonnews.ui.view.VideoEnabledWebChromeClient;
import ru.macrobit.abonnews.ui.view.VideoEnabledWebView;


public class DetailNewsFragment extends EnvFragment implements OnTaskCompleted {

    TextView mTitle;
    TextView mDate;
    WebView mBody;
    ImageView mImage;
    ExpandableListView mListView;
    ProgressBar mProgressBar;
    int mode;
    String mId;
    private VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;


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
        webView = (VideoEnabledWebView)parent.findViewById(R.id.webView);

        // Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = parent.findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup)parent.findViewById(R.id.videoLayout); // Your own view, read class comments
        //noinspection all
        View loadingView = getActivity().getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                // Your code...
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
        {
            @Override
            public void toggledFullscreen(boolean fullscreen)
            {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen)
                {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        //noinspection all
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                }
                else
                {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
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
//        mBody = (WebView) parent.findViewById(R.id.det_body);
        mImage = (ImageView) parent.findViewById(R.id.det_imageView);
        mListView = (ExpandableListView) parent.findViewById(R.id.det_listView);
        FullNews news = bundle.getParcelable("data");
        mTitle.setText(news.getTitle());
        mDate.setText(news.getDate());
        mId = news.getId();
//        initWebView(news.getBody());
//        getComments(news.getId() + "/comments/");
        initVideo(parent, news.getBody());
        getMedia();
        ImageUtils.getUIL(getActivity()).displayImage(news.getImageUrl(), mImage);
    }

    private void initWebView(String data) {
        mBody.setWebChromeClient(new WebChromeClient());
        mBody.setWebViewClient(new ProgressWebClient());

        mBody.loadData(getHtmlData(data), "text/html; charset=UTF-8", null);
        mBody.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mBody.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        WebSettings webSettings = mBody.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        mBody.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:100% !important; height: auto;}" +
                " iframe{width:100%% !important; height: auto !important;} " +
                "video{ idth:100%% !important; height: auto !important;} " +
                "wp-video{ width:100%% !important; height: auto !important;}" +
                "audio{visibility: visible !important}" +
                "</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    private void getMedia() {
        mode = 1;
        new GetRequest(DetailNewsFragment.this).execute(Values.MEDIA);
    }

    private void getComments(String url) {
        mode = 2;
        new GetRequest(DetailNewsFragment.this).execute(Values.GET_POST + url);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    public void onTaskCompleted(String result) {
        if (mode == 1) {
            Media[] media = GsonUtils.fromJson(result, Media[].class);
            ArrayList<Media> arrayList = NewsUtils.getMediaById(mId, media);
            if (arrayList.size() > 0) {
                for (int i = 0; i<arrayList.size(); i++) {
                    String source = arrayList.get(i).getSource();
                }
            }
        }
    }
//        Comments[] comments = GsonUtils.fromJson(result, Comments[].class);
//        ArrayList<Comments> arrayList = new ArrayList<Comments>(Arrays.asList(comments));
//        ArrayList<String> group = new ArrayList<>();
//        group.add("comments");
////        ArrayList<ArrayList<Comments>> group = new ArrayList<ArrayList<Comments>>();
////        group.add(arrayList);
////        CommentsAdapter adapter = new CommentsAdapter(getActivity(), group);
//        MyExpandableAdapter adapter = new MyExpandableAdapter(group, arrayList);
//        adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
//
////        adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
//
//        mListView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();


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
        mProgressBar.setVisibility(View.GONE);
    }
}

}

