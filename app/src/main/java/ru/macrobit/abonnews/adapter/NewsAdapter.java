package ru.macrobit.abonnews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.controller.ImageUtils;
import ru.macrobit.abonnews.model.ShortNews;

public class NewsAdapter extends ArrayAdapter<ShortNews> {

    private Context mContext;
    private ArrayList<ShortNews> mNews;
    private final int HEADER = 0;
    private final int ITEM = 1;
    private final int AD = 2;
    private int ad_count = 0;

    public NewsAdapter(Context context, int resource, ArrayList<ShortNews> arrayList) {
        super(context, resource, arrayList);
        mContext = context;
        mNews = arrayList;
    }

    @Override
    public int getCount() {
//        int x = mNews.size() / 6;
        return mNews.size();
    }

    @Override
    public ShortNews getItem(int index) {
        return mNews.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView title;
        TextView date;
        TextView commentsCount;
        ImageView image;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isSticky()) {
            return HEADER;
        } else if (getItem(position).isAd()) {
            return AD;
        } else {
            return ITEM;
        }

    }

    @Override
    public boolean isEnabled(int position) {
        if (mNews.get(position).isAd()) {
            if (mNews.get(position).getUrl() != null) {
                if (!mNews.get(position).getUrl().equals(""))
                    return true;
                else return false;
            }
            else
                return false;
        } else
            return true;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            switch (getItemViewType(position)) {
                case HEADER:
                    convertView = inflater.inflate(R.layout.main_news_item, parent, false);
                    break;
                case ITEM:
                    convertView = inflater.inflate(R.layout.news_item, parent, false);
                    break;
                case AD: {
                    convertView = inflater.inflate(R.layout.ad_item, parent, false);
//                    ad_count++;
//                    View v = inflater.inflate(R.layout.header, parent, false);
//                    ImageView image = ((ImageView) v.findViewById(R.id.imageAd));
//                    ImageUtils.getUIL(mContext).displayImage(Utils.getAd(6, mContext), image);
                }
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title = ((TextView) convertView.findViewById(R.id.textBody));
        viewHolder.date = ((TextView) convertView.findViewById(R.id.det_date));
        viewHolder.image = ((ImageView) convertView.findViewById(R.id.det_imageView));
        viewHolder.commentsCount = ((TextView) convertView.findViewById(R.id.comments_count));
        if (!mNews.get(position).isAd()) {
            Spanned span = Html.fromHtml(mNews.get(position - ad_count).getTitle());
            viewHolder.title.setText(span);
            viewHolder.date.setText(mNews.get(position - ad_count).getDate());
            viewHolder.date.setTextColor(Color.parseColor("#ffff8800"));
            String s = mNews.get(position - ad_count).getCommentCount();
            if (!s.equals("0")) {
                viewHolder.commentsCount.setText(mContext.getString(R.string.comments) + "(" + s + ")");
            }
        }
        viewHolder.image.setImageBitmap(null);
        ImageUtils.getUIL(mContext).displayImage(mNews.get(position - ad_count).getImageUrl(), viewHolder.image);
        return convertView;
    }
}
