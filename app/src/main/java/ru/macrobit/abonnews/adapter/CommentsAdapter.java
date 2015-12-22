package ru.macrobit.abonnews.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.utils.NewsUtils;
import ru.macrobit.abonnews.model.Comments;

/**
 * Created by Comp on 22.07.2015.
 */
public class CommentsAdapter extends ArrayAdapter<Comments> {

    private Context mContext;
    private ArrayList<Comments> mComments;

    public CommentsAdapter(Context context, int resource, ArrayList<Comments> arrayList) {
        super(context, resource, arrayList);
        mContext = context;
        this.mComments = arrayList;
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Comments getItem(int index) {
        return mComments.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView title;
        TextView date;
        TextView body;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_comments, parent, false);
            viewHolder.title = ((TextView) convertView.findViewById(R.id.com_author));
            viewHolder.body = ((TextView) convertView.findViewById(R.id.com_body));
            viewHolder.date = ((TextView) convertView.findViewById(R.id.com_date));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Spanned span = Html.fromHtml(mComments.get(position).getContent());
        viewHolder.title.setText(mComments.get(position).getAuthorName());
        viewHolder.body.setText(span);
        viewHolder.date.setText(NewsUtils.parseDate(mComments.get(position).getDate()));
        return convertView;
    }
}