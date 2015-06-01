package ru.macrobit.abonnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import ru.macrobit.abonnews.model.ShortNews;

public class NewsAdapter extends ArrayAdapter<ShortNews> {

    private Context mContext;
    private ArrayList<ShortNews> mNews;
    ImageLoader mImageLoader = ImageLoader.getInstance();

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
        TextView title;
        TextView date;
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.news_item, parent, false);
            viewHolder.title = ((TextView) convertView.findViewById(R.id.textBody));
            viewHolder.date = ((TextView) convertView.findViewById(R.id.date));
            viewHolder.image = ((ImageView) convertView.findViewById(R.id.imageView));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(mNews.get(position).getTitle());
        viewHolder.date.setText(mNews.get(position).getDate());
        mImageLoader.init(new ImageLoaderConfiguration.Builder(mContext).build());
        mImageLoader.displayImage(mNews.get(position).getImageUrl(), viewHolder.image);
        return convertView;
    }
}
