package ru.macrobit.abonnews.adapter;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.utils.NewsUtils;
import ru.macrobit.abonnews.model.Comments;

public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private ArrayList<Comments> childtems;
    private LayoutInflater inflater;
    private ArrayList<String> parentItems, child;


    class ViewHolder {
        TextView title;
        TextView date;
        TextView body;
    }

    // constructor
    public MyExpandableAdapter(ArrayList<String> parents, ArrayList<Comments> children) {
        this.parentItems = parents;
        this.childtems = children;
    }

    public void setInflater(LayoutInflater inflater, Activity activity) {
        this.inflater = inflater;
        this.activity = activity;
    }

    // method getChildView is called automatically for each child view.
    //  Implement this method as per your requirement
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_comments, parent, false);
            viewHolder.title = ((TextView) convertView.findViewById(R.id.com_author));
            viewHolder.body = ((TextView) convertView.findViewById(R.id.com_body));
            viewHolder.date = ((TextView) convertView.findViewById(R.id.com_date));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Spanned span = Html.fromHtml(childtems.get(childPosition).getContent());
        viewHolder.title.setText(childtems.get(childPosition).getAuthorName());
        viewHolder.body.setText(span);
        viewHolder.date.setText(NewsUtils.parseDate(childtems.get(childPosition).getDate()));
        return convertView;
    }

    // method getGroupView is called automatically for each parent item
    // Implement this method as per your requirement
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_view, null);
        }
        ((TextView) convertView.findViewById(R.id.textGroup)).setText(parentItems.get(groupPosition));
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childtems.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return  childtems.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}