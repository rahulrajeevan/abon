package ru.macrobit.abonnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.controller.ImageUtils;
import ru.macrobit.abonnews.model.ShortNews;

/**
 * Created by Comp on 17.06.2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<ShortNews> mNews;
    Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        ImageView image;
        public ViewHolder(View v) {
            super(v);
        }
    }

    public RecyclerViewAdapter(ArrayList<ShortNews> arrayList, Context context) {
        mNews = arrayList;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.title = ((TextView) v.findViewById(R.id.textBody));
        viewHolder.date = ((TextView) v.findViewById(R.id.det_date));
        viewHolder.image = ((ImageView) v.findViewById(R.id.det_imageView));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.title.setText(mNews.get(position).getTitle());
        viewHolder.date.setText(mNews.get(position).getDate());
        viewHolder.image.setImageBitmap(null);
        ImageUtils.getUIL(mContext).displayImage(mNews.get(position).getImageUrl(), viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }
}