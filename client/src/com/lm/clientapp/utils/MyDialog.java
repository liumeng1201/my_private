package com.lm.clientapp.utils;

import com.lm.clientapp.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MyDialog {
	private Context mContext;
	private AlertDialog.Builder dialog;

	public MyDialog(Context context) {
		this.mContext = context;
		dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(R.string.tishi);
	}

	public void show() {
		dialog.create().show();
	}

	public void setMessage(int resId) {
		dialog.setMessage(resId);
	}

	public void setMessage(String str) {
		dialog.setMessage(str);
	}

	public void setNegativeButton(int resId,
			DialogInterface.OnClickListener listener) {
		dialog.setNegativeButton(resId, listener);
	}

	public void setNegativeButton(String str,
			DialogInterface.OnClickListener listener) {
		dialog.setNegativeButton(str, listener);
	}

	public void setPositiveButton(int resId,
			DialogInterface.OnClickListener listener) {
		dialog.setPositiveButton(resId, listener);
	}

	public void setPositiveButton(String str,
			DialogInterface.OnClickListener listener) {
		dialog.setPositiveButton(str, listener);
	}
}
