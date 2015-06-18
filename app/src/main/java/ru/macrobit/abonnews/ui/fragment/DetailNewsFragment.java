package ru.macrobit.abonnews.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.ImageUtils;
import ru.macrobit.abonnews.loader.GetRequest;
import ru.macrobit.abonnews.model.FullNews;


public class DetailNewsFragment extends EnvFragment implements OnTaskCompleted {

    TextView mTitle;
    TextView mDate;
    WebView mBody;
    ImageView mImage;
    ExpandableListView mListView;
    ProgressBar mProgressBar;


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

    private void initFragment(View parent) {
        Bundle bundle = this.getArguments();
        mProgressBar = (ProgressBar) parent.findViewById(R.id.progressBar);
        mTitle = (TextView) parent.findViewById(R.id.det_title);
        mDate = (TextView) parent.findViewById(R.id.det_date);
        mBody = (WebView) parent.findViewById(R.id.det_body);
        mImage = (ImageView) parent.findViewById(R.id.det_imageView);
        mListView = (ExpandableListView) parent.findViewById(R.id.det_listView);
        FullNews news = bundle.getParcelable("data");
        mTitle.setText(news.getTitle());
        mDate.setText(news.getDate());
        initWebView(news.getBody());
        getComments(news.getId() + "/comments/");
        ImageUtils.getUIL(getActivity()).displayImage(news.getImageUrl(), mImage);
    }

    private void initWebView(String data) {
//        mBody.setWebChromeClient(new WebChromeClient());
        mBody.setWebViewClient(new ProgressWebClient());

        mBody.loadData(getHtmlData(data), "text/html; charset=UTF-8", null);
        mBody.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mBody.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        WebSettings webSettings = mBody.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        mBody.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        mBody.addJavascriptInterface(new AudioInterface(getActivity()), "Aud");
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:100% !important; height: auto;}" +
                " iframe{max-width: 0%; width:0% !important; height: 0%;} " +
                "video{max-width: 0%; width:0% !important; height: 0%;} " +
                "wp-video{max-width: 0%; width:0% !important; height: 0%;}" +
                "</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    private void getComments(String url) {
        new GetRequest(DetailNewsFragment.this).execute(Values.GET_POST + url);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    public void onTaskCompleted(String result) {
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
    }

    public class ProgressWebClient extends WebViewClient
    {
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

