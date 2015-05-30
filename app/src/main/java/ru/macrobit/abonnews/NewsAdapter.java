package ru.macrobit.abonnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.macrobit.abonnews.model.ShortNews;

/**
 * Created by Comp on 30.05.2015.
 */
public class NewsAdapter extends ArrayAdapter<ShortNews> {

    private Context mContext;
    private ArrayList<ShortNews> mNews;

    public NewsAdapter(Context context, int resource, ArrayList<ShortNews> arrayList )  {
        super(context, resource, arrayList);
        mContext = context;
        mNews = arrayList;
    }

    @Override
    public int getCount() {
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
        TextView textBody;
        TextView time;
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.news_item, parent, false);
            viewHolder.textBody = ((TextView) convertView.findViewById(R.id.textBody));
            viewHolder.time= ((TextView) convertView.findViewById(R.id.time));
            viewHolder.image = ((ImageView) convertView.findViewById(R.id.imageView));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textBody.setText(mNews.get(position).getBody());
        viewHolder.time.setText(mNews.get(position).getTime());
        return convertView;
    }
}
