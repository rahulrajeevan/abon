package ru.macrobit.abonnews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.controller.NewsUtils;
import ru.macrobit.abonnews.model.MyComment;

/**
 * Created by Ghost Surfer on 24.06.2015.
 */
public class MyCommentsAdapter extends ArrayAdapter<MyComment> {

    private Context mContext;
    private ArrayList<MyComment> myComments;

    public MyCommentsAdapter(Context context, int resource, ArrayList<MyComment> arrayList) {
        super(context, resource, arrayList);
        mContext = context;
        myComments = arrayList;
    }

    @Override
    public int getCount() {
        return myComments.size();
    }

    @Override
    public MyComment getItem(int index) {
        return myComments.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView title;
        TextView date;
        TextView comment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.my_comments_item, parent, false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title = ((TextView) convertView.findViewById(R.id.mycom_title));
        viewHolder.date = ((TextView) convertView.findViewById(R.id.mycom_date));
        viewHolder.comment = ((TextView) convertView.findViewById(R.id.mycom_comment));
        viewHolder.title.setText(myComments.get(position).getPost().getPostTitle());
        Spanned spanned = Html.fromHtml(myComments.get(position).getContent());
        viewHolder.comment.setText(spanned);
        viewHolder.date.setText(NewsUtils.parseDate(myComments.get(position).getDate()));
        viewHolder.date.setTextColor(Color.parseColor("#ffff8800"));
        return convertView;
    }
}