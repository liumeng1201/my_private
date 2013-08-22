package com.lm.clientapp.listtree;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.lm.clientapp.R;

public class TreeBtnOnClickListener implements OnClickListener {
	private Button btn1, btn2;
	private ListView listview1, listview2;
	private Context mContext;

	public TreeBtnOnClickListener(Context context, Button btn1, Button btn2,
			ListView listview1, ListView listview2) {
		this.mContext = context;
		this.btn1 = btn1;
		this.btn2 = btn2;
		this.listview1 = listview1;
		this.listview2 = listview2;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tree_btn1:
			btn1.setBackgroundColor(mContext.getResources().getColor(
					R.color.listbtn_selected));
			btn2.setBackgroundColor(mContext.getResources().getColor(
					R.color.listbtn));
			listview1.setVisibility(View.VISIBLE);
			listview2.setVisibility(View.GONE);
			break;
		case R.id.tree_btn2:
			btn1.setBackgroundColor(mContext.getResources().getColor(
					R.color.listbtn));
			btn2.setBackgroundColor(mContext.getResources().getColor(
					R.color.listbtn_selected));
			listview1.setVisibility(View.GONE);
			listview2.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

}
