package com.geolo.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Button;

@SuppressLint("NewApi")
public class flashtest extends Activity {
	WebView wb = null;
	Button btn, btnpause = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		wb = (WebView) findViewById(R.id.webView02);
		btn = (Button) findViewById(R.id.testbtnplay);
		btnpause = (Button) findViewById(R.id.testbtnpause);

		(wb.getSettings()).setJavaScriptEnabled(true);
		(wb.getSettings()).setPluginState(PluginState.ON);
		wb.setWebChromeClient(new WebChromeClient());
		wb.addJavascriptInterface(new CallJava(), "CallJava");
		wb.loadUrl("file:///android_asset/sample/default.html");

		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// wb.loadUrl("about:blank");
				// wb.loadUrl("file:///android_asset/sample/default.html");
				wb.loadUrl("javascript:setFlashPath('" + "flash.swf" + "')");
//				wb.loadUrl("javascript:Play()");
			}
		});
		
		btnpause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wb.loadUrl("javascript:Pause()");
			}
		});
	}

	private final class CallJava {
		public void consoleFlashProgress(float progressSize) {
		}
	}
}
