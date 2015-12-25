package ru.macrobit.abonnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.activity.FragmentActivity;
import ru.macrobit.abonnews.model.FullNews;
import ru.macrobit.abonnews.model.News;
import ru.macrobit.abonnews.model.ShortNews;
import ru.macrobit.abonnews.utils.GsonUtils;
import ru.macrobit.abonnews.utils.ImageUtils;
import ru.macrobit.abonnews.utils.Utils;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<ShortNews> mNews;
    private ArrayList<News> mFullNews;

    public CustomPagerAdapter(Context mContext, ArrayList<ShortNews> mNews, ArrayList<News> mFullNews) {
        this.mContext = mContext;
        this.mNews = mNews;
        this.mFullNews = mFullNews;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup convertView = (ViewGroup) inflater.inflate(R.layout.item_main_news, collection, false);
        TextView title = ((TextView) convertView.findViewById(R.id.textBody));
        TextView date = ((TextView) convertView.findViewById(R.id.det_date));
        ImageView image = ((ImageView) convertView.findViewById(R.id.det_imageView));
        TextView commentsCount = ((TextView) convertView.findViewById(R.id.comments_count));
        commentsCount.setText("");
        Spanned span = Html.fromHtml(mNews.get(position).getTitle());
        title.setText(span);
        date.setText(mNews.get(position).getDate());
        String s = mNews.get(position).getCommentCount();
        if (!s.equals("0")) {
            commentsCount.setText(mContext.getString(R.string.comments) + "(" + s + ")");
        }
        image.setImageBitmap(null);
        ImageUtils.getUIL(mContext).displayImage(mNews.get(position).getImageUrl(), image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortNews shortNews = mNews.get(position);
                News news = mFullNews.get(position);
                FullNews fullNews = new FullNews(shortNews, news.getContent(), news.getLink());
                Bundle bundle = new Bundle();
                bundle.putString(Values.TAG, Values.DETAIL_TAG);
                Intent intent = new Intent(mContext, FragmentActivity.class);
                Utils.saveToSharedPreferences(Values.FULL_NEWS, GsonUtils.toJson(fullNews), mContext);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        ((ViewPager) collection).addView(convertView, 0);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mNews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}