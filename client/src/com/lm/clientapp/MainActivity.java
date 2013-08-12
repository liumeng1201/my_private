package com.lm.clientapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

public class MainActivity extends Activity {
	private String TAG = "MainActivity";
	private Context mContext;
	// 提示安装FlashPlayer的dialog
	private AlertDialog.Builder fpiDialog;

	private WebView content_WebView;
	private EditText edtInputUrl;
	private Button btnLoadUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = MainActivity.this;

		init();
		isFlashPlayerInstalled();
	}

	// 初始化各个组件实例及各个变量
	private void init() {
		initfpiDialog();

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 判断FlashPlayer是否已经安装，未安装则提示进行安装
	private void isFlashPlayerInstalled() {
		if (Utils.checkIfFlashPlayerInstall(mContext)) {
			Log.d(TAG, "already install FlashPlayer");
		} else {
			Log.d(TAG, "not install FlashPlayer");
			fpiDialog.create().show();
		}
	}

	// 初始化flash player install Dialog
	private void initfpiDialog() {
		fpiDialog = new AlertDialog.Builder(mContext);
		fpiDialog.setTitle(R.string.tishi);
		fpiDialog.setMessage(R.string.fpidialog_install);
		fpiDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.d(TAG, "install flashplayer");

						if (Utils.copyApkFromAssets(mContext,
								"flashplayer.apk", Environment
										.getExternalStorageDirectory()
										.getAbsolutePath()
										+ "/flashplayer.apk")) {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setDataAndType(Uri.parse("file://"
									+ Environment.getExternalStorageDirectory()
											.getAbsolutePath()
									+ "/flashplayer.apk"),
									"application/vnd.android.package-archive");
							mContext.startActivity(intent);
						}
					}
				});
		fpiDialog.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.d(TAG, "cancel install");
					}
				});
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
