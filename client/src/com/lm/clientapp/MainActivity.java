package com.lm.clientapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
	private String TAG = "MainActivity";
	private Context mContext;

	private WebView content_WebView;
	private EditText edtInputUrl;
	private Button btnLoadUrl;
	private ImageButton btnSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = MainActivity.this;

		init();
		startAPNService();
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 启动消息推送服务
	private void startAPNService() {
		ServiceManager serviceManager = new ServiceManager(this);
		serviceManager.setNotificationIcon(R.drawable.ic_launcher);
		serviceManager.startService();
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
