package com.lm.clientapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.lm.clientapp.model.CaseSourceItem;
import com.lm.clientapp.model.PushItem;
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
	private Context context;

	APNReceiver(Handler handler) {
		this.mHandler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		notificationId = intent.getStringExtra(Constants.NOTIFICATION_ID);
		apiKey = intent.getStringExtra(Constants.NOTIFICATION_API_KEY);
		title = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
		message = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
		uri = intent.getStringExtra(Constants.NOTIFICATION_URI);

		// Map map = new HashMap<String, String>();
		// map.put("notificationId", notificationId);
		// map.put("apiKey", apiKey);
		// map.put("title", title);
		// map.put("message", message);
		// map.put("uri", uri);

		PushItem pi = getPushItem(message);

		Message msg = new Message();
		msg.what = Utils.REFRESH_CONTENT;
		msg.obj = pi;
		mHandler.sendMessage(msg);
	}

	// 将推送过来的信息转换为PushItem对象
	private PushItem getPushItem(String recvstr) {
		PushItem pi = new PushItem();
		// sid推送学生列表
		List<String> studentList = new ArrayList<String>();
		// a当前课程流
		String currentCourse = null;
		// b当前老师
		String currentTeacher = null;
		// c当前模式,1推送,2自主
		String currentMode = null;
		// d当前案例
		String currentCase = null;
		// e当前caseSource表所对应的内容
		// List<Map<String, String>> currentCaseSource = new
		// ArrayList<Map<String, String>>();
		CaseSourceItem currentCaseSource = new CaseSourceItem();

		// strcompare = {sid,a,b,c,d,e1,e2,e3,e4,e5,e6,e7}
		String[] strcompare = context.getResources().getStringArray(
				R.array.strcompare);
		// recvstr = sid-14-15-16-17-18$a-1$b-6$c-1$d-1014$
		// e1-842$e2-幻灯片1.JPG$e3-资源库/案例库/YG3xPPT1/幻灯片1.JPG$e4-40$e5-1$e6-1$e7-1014
		String[] strarray = recvstr.split("\\$");
		for (int i = 0; i < strarray.length; i++) {
			for (int j = 0; j < strcompare.length; j++) {
				if (strarray[i].indexOf(strcompare[j]) != -1) {
					if (strcompare[j].equals("sid")) {
						String[] sidarray = strarray[i].split("-");
						for (int k = 1; k < sidarray.length; k++) {
							studentList.add(sidarray[k]);
						}
					} else if (strcompare[j].equals("a")) {
						String[] aarray = strarray[i].split("-");
						currentCourse = aarray[1];
					} else if (strcompare[j].equals("b")) {
						String[] barray = strarray[i].split("-");
						currentTeacher = barray[1];
					} else if (strcompare[j].equals("c")) {
						String[] carray = strarray[i].split("-");
						currentMode = carray[1];
					} else if (strcompare[j].equals("d")) {
						String[] darray = strarray[i].split("-");
						currentCase = darray[1];
					} else if (strcompare[j].equals("e1")) {
						// sourceId
						String[] e1array = strarray[i].split("-");
						currentCaseSource.setSourceId(Long
								.parseLong(e1array[1]));
					} else if (strcompare[j].equals("e2")) {
						// sourceName
						String[] e2array = strarray[i].split("-");
						currentCaseSource.setSourceName(e2array[1]);
					} else if (strcompare[j].equals("e3")) {
						// sourceUrl
						String[] e3array = strarray[i].split("-");
						currentCaseSource.setSourceUrl(e3array[1]);
					} else if (strcompare[j].equals("e4")) {
						// mediaTypeId
						String[] e4array = strarray[i].split("-");
						currentCaseSource.setMediaTypeId(Integer
								.parseInt(e4array[1]));
					} else if (strcompare[j].equals("e5")) {
						// sourceTypeId
						String[] e5array = strarray[i].split("-");
						currentCaseSource.setSourceTypeId(Integer
								.parseInt(e5array[1]));
					} else if (strcompare[j].equals("e6")) {
						// seq
						String[] e6array = strarray[i].split("-");
						currentCaseSource.setSeq(Integer.parseInt(e6array[1]));
					} else if (strcompare[j].equals("e7")) {
						// caseId
						String[] e7array = strarray[i].split("-");
						currentCaseSource.setCaseId(Long.parseLong(e7array[1]));
					}
					break;
				}
			}
		}

		pi.setStudentList(studentList);
		pi.setCurrentCourse(currentCourse);
		pi.setCurrentTeacher(currentTeacher);
		pi.setCurrentMode(currentMode);
		pi.setCurrentCase(currentCase);
		pi.setCurrentCaseSource(currentCaseSource);

		return pi;
	}

}
