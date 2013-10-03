package com.geolo.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

@SuppressLint("NewApi")
public class flashtest extends Activity {
	Button btn = null;
	FrameLayout fl = null;
	Handler mHandler = new Handler();
	WebView webview = null;
	EditText ip;
	Handler handler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler();
		setContentView(R.layout.testw2);
		btn = (Button) findViewById(R.id.test2btn);
		fl = (FrameLayout) findViewById(R.id.test2fl);
		webview = new WebView(getApplicationContext());
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setPluginState(PluginState.ON);
		webview.setWebChromeClient(new WebChromeClient());
		fl.addView(webview);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				webview.loadUrl("file:///android_asset/sample/tt.html");
				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								webview.loadUrl("javascript:initFlash('" + "1" + "')");
							}
						});
					}
				}.start();
			}
		});

	}
	
	private final class CallJava {
		public void consoleFlashProgress(float progressSize) {
		}
	}
}
