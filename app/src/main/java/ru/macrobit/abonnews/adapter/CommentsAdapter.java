package ru.macrobit.abonnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.model.Comments;

public class CommentsAdapter extends BaseExpandableListAdapter {

    private ArrayList<ArrayList<Comments>> mGroups;
    private Context mContext;

    public CommentsAdapter (Context context, ArrayList<ArrayList<Comments>> groups){
        mContext = context;
        mGroups = groups;
    }

    class ViewHolder {
        TextView title;
        TextView body;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(0).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_view, null);
        }

//        if (isExpanded) {
//        } else {
//        }

        TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
        textGroup.setText(mContext.getString(R.string.comments));
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.comments_item, parent, false);
            viewHolder.title = ((TextView) convertView.findViewById(R.id.com_author));
            viewHolder.body = ((TextView) convertView.findViewById(R.id.com_body));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(mGroups.get(groupPosition).get(childPosition).getAuthorName());
        viewHolder.body.setText(mGroups.get(groupPosition).get(childPosition).getContent());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

