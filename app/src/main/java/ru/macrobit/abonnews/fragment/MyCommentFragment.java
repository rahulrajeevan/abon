package ru.macrobit.abonnews.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.macrobit.abonnews.OnTaskCompleted;
import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.adapter.MyCommentsAdapter;
import ru.macrobit.abonnews.model.FullNews;
import ru.macrobit.abonnews.model.MyComment;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.ShortNews;
import ru.macrobit.abonnews.utils.API;
import ru.macrobit.abonnews.utils.GsonUtils;
import ru.macrobit.abonnews.utils.NewsUtils;
import ru.macrobit.abonnews.utils.Utils;

public class MyCommentFragment extends EnvFragment implements OnTaskCompleted {

    private ListView mListView;
    private ArrayList<MyComment> mComments;
    private MyCommentsAdapter mAdapter;
    private TextView mText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_my_comment,
                container, false);
        mListView = (ListView) view.findViewById(R.id.mycom_listview);
        mText = (TextView) view.findViewById(R.id.mycom_text);
        showProgressDialog(getString(R.string.loading_comments));
        if (Utils.isConnected(getActivity())) {
            API.IGetMyComments getMyComments = API.getRestAdapter().create(API.IGetMyComments.class);
            getMyComments.getMyComments(new Callback<List<MyComment>>() {
                @Override
                public void success(List<MyComment> commentses, Response response) {
                    mComments = new ArrayList<>();
                    mComments.addAll(commentses);
                    initComments();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
//            new GetRequest(this, Utils.loadCookieFromSharedPreferences(getActivity())).execute(Values.MY_COMMENTS);
        }
        return view;
    }

    private void initComments() {
        if (mComments.size() > 0) {
            initListView();
        } else {
            mText.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
        hideProgressDialog();
    }

    private void initListView() {
        mAdapter = new MyCommentsAdapter(getActivity(), R.layout.item_my_comments, mComments);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String s = null;
                API.IGetPost getPost = API.getRestAdapter().create(API.IGetPost.class);
                getPost.getPost(String.valueOf(mComments.get(position).getPost().getId()), new Callback<News>() {
                    @Override
                    public void success(News news, Response response) {
                        ShortNews shortNews = NewsUtils.generateShortNews(news);
                        FullNews fullNews = new FullNews(shortNews, news.getContent(), news.getLink());
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("data", fullNews);
                        add(new DetailNewsFragment(), bundle, Values.DETAIL_TAG);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });
    }

    @Override
    public void onTaskCompleted(String result) {
        try {
            mComments = new ArrayList<>();
            MyComment[] comm = GsonUtils.fromJson(result, MyComment[].class);
            if (comm.length > 0) {
                mComments.addAll(Arrays.asList(comm));
                initListView();
            } else {
                mText.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
        }
        hideProgressDialog();

    }
}
