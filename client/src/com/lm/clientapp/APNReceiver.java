package com.lm.clientapp;

import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lm.clientapp.pushnotification.Constants;
import com.lm.clientapp.utils.Utils;

public class APNReceiver extends BroadcastReceiver {
	private String LOGTAG = "apnReceiver";

	private String notificationId;
	private String apiKey;
	private String title;
	private String message;
	private String uri;

	private Handler mHandler;

	APNReceiver(Handler handler) {
		this.mHandler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		notificationId = intent.getStringExtra(Constants.NOTIFICATION_ID);
		apiKey = intent.getStringExtra(Constants.NOTIFICATION_API_KEY);
		title = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
		message = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
		uri = intent.getStringExtra(Constants.NOTIFICATION_URI);

		Map map = new HashMap<String, String>();
		map.put("notificationId", notificationId);
		map.put("apiKey", apiKey);
		map.put("title", title);
		map.put("message", message);
		map.put("uri", uri);

		Message msg = new Message();
		msg.what = Utils.REFRESH_CONTENT;
		msg.obj = map;
		mHandler.sendMessage(msg);
	}

}
