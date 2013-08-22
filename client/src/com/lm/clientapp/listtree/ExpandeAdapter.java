package com.lm.clientapp.listtree;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lm.clientapp.R;
import com.lm.clientapp.utils.ImageUtils;

public class ExpandeAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private LayoutInflater mInflater = null;
	private String[] mGroupStrings = null;
	private List<List<ListItem>> mData = null;

	public ExpandeAdapter(Context ctx, String[] groups,
			List<List<ListItem>> list) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroupStrings = groups;
		mData = list;
	}

	public void setData(List<List<ListItem>> list) {
		mData = list;
	}

	@Override
	public ListItem getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.child_item_layout, null);
		}
		ChildViewHolder holder = new ChildViewHolder();
		holder.mIcon = (ImageView) convertView.findViewById(R.id.item_img);
		if (getChild(groupPosition, childPosition).getIcon() != null) {

			holder.mIcon.setImageBitmap(ImageUtils.getRoundCornerBitmap(
					getChild(groupPosition, childPosition).getIcon(), 10));
		} else {
			holder.mIcon.setImageBitmap(ImageUtils.getRoundCornerBitmap(
					ImageUtils.drawable2Bitmap(mContext.getResources()
							.getDrawable(R.drawable.ic_launcher)), 10));
		}
		holder.mName = (TextView) convertView.findViewById(R.id.item_name);
		holder.mName.setText(getChild(groupPosition, childPosition).getName());
		holder.mDetail = (TextView) convertView.findViewById(R.id.item_detail);
		holder.mDetail.setText(getChild(groupPosition, childPosition)
				.getDetail());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mData.get(groupPosition).size();
	}

	@Override
	public List<ListItem> getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
}