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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

@SuppressLint("NewApi")
public class flashtest extends Activity {
	Button btn = null;
	Handler mHandler = new Handler();
	WebView webview = null;
	EditText ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		btn = (Button) findViewById(R.id.testbtnplay);

		ip = (EditText) findViewById(R.id.testinput);

		webview = (WebView) findViewById(R.id.webView02);

		(webview.getSettings()).setJavaScriptEnabled(true);
		(webview.getSettings()).setPluginState(PluginState.ON);
		webview.setWebChromeClient(new WebChromeClient());
		webview.addJavascriptInterface(new CallJava(), "CallJava");
//		webview.loadUrl("file:///android_asset/sample/Console.html");
		webview.loadUrl("http://192.168.1.106:10000/flow/console/Console.html");

		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				for (int i = 0; i < 2; i++) {
//					webview.loadUrl("file:///android_asset/sample/tt.html");
//					String message = ip.getText().toString();
					webview.loadUrl("javascript:setValue('" + "1" + "')");
//				}
			}
		});

	}

	private final class CallJava {
		public void consoleFlashProgress(float progressSize) {
		}
	}
}
