package com.lm.clientapp.listtree;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lm.clientapp.R;
import com.lm.clientapp.utils.ImageUtils;

public class SimpleExpandeAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private LayoutInflater mInflater = null;
	private String[] mGroupStrings = null;
	private List<List<SimpleListItem>> mData = null;

	public SimpleExpandeAdapter(Context ctx, String[] groups,
			List<List<SimpleListItem>> list) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroupStrings = groups;
		mData = list;
	}

	public void setData(List<List<SimpleListItem>> list) {
		mData = list;
	}

	@Override
	public SimpleListItem getChild(int groupPosition, int childPosition) {
		return mData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.simple_child_item_layout,
					null);
		}
		ChildViewHolder holder = new ChildViewHolder();
		holder.mName = (TextView) convertView
				.findViewById(R.id.simple_item_name);
		holder.mName.setText(getChild(groupPosition, childPosition).getName());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mData.get(groupPosition).size();
	}

	@Override
	public List<SimpleListItem> getGroup(int groupPosition) {
		return mData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.group_item_layout, null);
		}
		GroupViewHolder holder = new GroupViewHolder();
		holder.mGroupName = (TextView) convertView
				.findViewById(R.id.group_name);
		holder.mGroupName.setText(mGroupStrings[groupPosition]);
		holder.mGroupCount = (TextView) convertView
				.findViewById(R.id.group_count);
		holder.mGroupCount.setText("[" + mData.get(groupPosition).size() + "]");
		return convertView;
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