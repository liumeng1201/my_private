package com.lm.clientapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lm.clientapp.pushnotification.ServiceManager;

//This is an android push notification demo application.
public class apnActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("apnActivity", "onCreate()...");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Settings
		Button okButton = (Button) findViewById(R.id.btn_settings);
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				ServiceManager.viewNotificationSettings(apnActivity.this);
			}
		});

		// Start the service
		ServiceManager serviceManager = new ServiceManager(this);
		serviceManager.setNotificationIcon(R.drawable.ic_launcher);
		serviceManager.startService();
	}

}