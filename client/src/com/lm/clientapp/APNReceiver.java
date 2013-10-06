package com.lm.clientapp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.lm.clientapp.model.CaseItem;
import com.lm.clientapp.model.CaseSourceItem;
import com.lm.clientapp.model.CourseItem;
import com.lm.clientapp.model.PushItem;
import com.lm.clientapp.pushnotification.Constants;
import com.lm.clientapp.tools.CurrentCourseHandler;
import com.lm.clientapp.utils.HttpTool;
import com.lm.clientapp.utils.Utils;

public class APNReceiver extends BroadcastReceiver {
	private String LOGTAG = "apnReceiver";

	private String notificationId;
	private String apiKey;
	private String title;
	private String message;
	private String uri;

	private ClientApp clientApp;
	// 当前courseItem
	private CourseItem currentCourseItem;
	// 临时存放当前courseId
	private long courseTemp = 0;

	private Handler mHandler;
	private Context context;

	APNReceiver(Handler handler) {
		this.mHandler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		clientApp = (ClientApp) context.getApplicationContext();
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
		long currentCourse = 0;
		// b当前老师
		String currentTeacherId = null;
		// c当前模式,1推送,2自主
		int currentMode = 0;
		// d当前案例
		CaseItem caseItem = new CaseItem();
		// e当前caseSource表所对应的内容
		CaseSourceItem currentCaseSource = new CaseSourceItem();

		// strcompare = {sid,a,b,c,d1,d2,d3,d4,e1,e2,e3,e4,e5,e6,e7}
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
						// courseId
						String[] aarray = strarray[i].split("-");
						currentCourse = Long.parseLong(aarray[1]);
						if (currentCourse != courseTemp) {
							setCourseObject(currentCourse);
							courseTemp = currentCourse;
						}
					} else if (strcompare[j].equals("b")) {
						// teacherId
						String[] barray = strarray[i].split("-");
						currentTeacherId = barray[1];
					} else if (strcompare[j].equals("c")) {
						// mode
						String[] carray = strarray[i].split("-");
						currentMode = Integer.parseInt(carray[1]);
					} else if (strcompare[j].equals("d1")) {
						// caseId
						String[] darray = strarray[i].split("-");
						caseItem.setCaseId(Long.parseLong(darray[1]));
					} else if (strcompare[j].equals("d2")) {
						// caseName
						String[] darray = strarray[i].split("-");
						caseItem.setCaseName(darray[1]);
					} else if (strcompare[j].equals("d3")) {
						// caseKindId
						String[] darray = strarray[i].split("-");
						caseItem.setCaseKindId(Integer.parseInt(darray[1]));
					} else if (strcompare[j].equals("d4")) {
						// engineId
						String[] darray = strarray[i].split("-");
						caseItem.setEngineId(Long.parseLong(darray[1]));
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
		pi.setCurrentTeacher(currentTeacherId);
		pi.setCurrentMode(currentMode);
		pi.setCaseItem(caseItem);
		pi.setCurrentCaseSource(currentCaseSource);

		return pi;
	}

	// 通过courseId获取当前courseItem对象,并将其保存至全局变量中
	private void setCourseObject(final long courseId) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				currentCourseItem = new CourseItem();

				String url = "http://" + clientApp.getServerIP()
						+ context.getResources().getString(R.string.server_port)
						+ context.getResources().getString(R.string.currentCourseAction);
				List<NameValuePair> datas = new ArrayList<NameValuePair>();
				datas.add(new BasicNameValuePair("courseid", String.valueOf(courseId)));

				InputStream xmlStream = HttpTool.sendDataByPost(url, datas);
				try {
					SAXParserFactory factory = SAXParserFactory.newInstance();
					SAXParser parser = factory.newSAXParser();
					XMLReader xmlreader = parser.getXMLReader();
					CurrentCourseHandler handler = new CurrentCourseHandler();
					xmlreader.setContentHandler(handler);
					InputSource source = new InputSource(xmlStream);
					xmlreader.parse(source);
					currentCourseItem = handler.getCurrentCourse();
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							clientApp.setCourseItem(currentCourseItem);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
