package com.lm.clientapp;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.lm.clientapp.pushnotification.ServiceManager;

public class MainActivity extends Activity {
	private String LOGTAG = "MainActivity";
	private Context mContext;

	private WebView content_WebView;
	private EditText edtInputUrl;
	private Button btnLoadUrl;
	private ImageButton btnSettings;

	// 接收推送消息服务的manager
	private ServiceManager serviceManager;
	// APN广播
	private APNReceiver apnReceiver;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = MainActivity.this;

		init();
		startAPNService();
		initAPNReceiver();
	}

	// 初始化各个组件实例及各个变量
	private void init() {
		content_WebView = (WebView) findViewById(R.id.content_webview);
		initWebView(content_WebView);

		edtInputUrl = (EditText) findViewById(R.id.inputurl);
		btnLoadUrl = (Button) findViewById(R.id.loadurl);
		btnLoadUrl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = edtInputUrl.getText().toString();
				content_WebView.loadUrl(url);
			}
		});

		btnSettings = (ImageButton) findViewById(R.id.userinfo_setting);
		btnSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转至消息推送设置界面
				ServiceManager.viewNotificationSettings(mContext);
			}
		});

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				// 接收到服务器端推动的信息之后更新内容
				if (msg.what == Utils.REFRESH_CONTENT) {
					Map map = (Map) msg.obj;
					Log.d(LOGTAG, "notificationId="
							+ map.get("notificationId").toString());
					Log.d(LOGTAG, "notificationApiKey="
							+ map.get("apiKey").toString());
					Log.d(LOGTAG, "notificationTitle="
							+ map.get("title").toString());
					Log.d(LOGTAG, "notificationMessage="
							+ map.get("message").toString());
					Log.d(LOGTAG, "notificationUri="
							+ map.get("uri").toString());
				}
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (serviceManager != null) {
			// 退出程序时停止接收推送的消息
			serviceManager.stopService();
		}

		if (apnReceiver != null) {
			// 解除APNReceiver广播
			unregisterReceiver(apnReceiver);
		}
	}

	// 启动消息推送服务
	private void startAPNService() {
		serviceManager = new ServiceManager(this);
		serviceManager.setNotificationIcon(R.drawable.ic_launcher);
		serviceManager.startService();
	}

	// 初始化APNReceiver广播
	private void initAPNReceiver() {
		apnReceiver = new APNReceiver(mHandler);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Utils.SEND_RECEIVER_MESSAGE);
		registerReceiver(apnReceiver, filter);
	}

	// 初始化WebView设置
	private void initWebView(WebView webview) {
		WebSettings setting = webview.getSettings();
		// 设置支持flash插件
		setting.setPluginState(PluginState.ON);
		setting.setJavaScriptEnabled(true);
		// 设置根据页面宽度自动进行缩放
		setting.setLoadWithOverviewMode(true);
		setting.setUseWideViewPort(true);
		// 设置支持多点触摸并显示缩放按钮
		setting.setSupportZoom(true);
		setting.setBuiltInZoomControls(true);

		webview.setWebViewClient(new WebViewClient() {
			// 在当前WebView中加载所有url
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}
}
