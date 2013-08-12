package com.lm.clientapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private String TAG = "LoginActivity";

	// 登录、取消按钮
	private Button btnLogin, btnCancel;
	// 用户名、密码、服务器IP输入框
	private EditText edtUsername, edtPassword, edtServerIP;
	// 记住用户名密码、服务器IP复选框
	private CheckBox chbRememberUser, chbRememberIP;
	// 是否记住用户名密码标志
	private boolean rmbUser = false;
	// 是否记住服务器IP标志
	private boolean rmbIP = false;

	private Context mContext;

	// 登录按钮点击事件监听
	private OnClickListener loginClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String username = edtUsername.getText().toString();
			String password = edtPassword.getText().toString();
			String serverip = edtServerIP.getText().toString();
			rmbUser = chbRememberUser.isChecked();
			rmbIP = chbRememberIP.isChecked();

			if (saveInfo(username, password, serverip, rmbUser, rmbIP)) {
				Log.d(TAG, "save success");
			}

			Intent intent = new Intent(mContext, MainActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mContext = LoginActivity.this;

		init();
	}

	// 初始化各个组件实例
	private void init() {
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		edtUsername = (EditText) findViewById(R.id.input_username);
		edtPassword = (EditText) findViewById(R.id.input_password);
		edtServerIP = (EditText) findViewById(R.id.input_serverip);
		chbRememberUser = (CheckBox) findViewById(R.id.chb_rememberuser);
		chbRememberIP = (CheckBox) findViewById(R.id.chb_rememberip);

		loadSavedInfo();

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				LoginActivity.this.finish();
				Intent intent = new Intent(mContext, apnActivity.class);
				startActivity(intent);
			}
		});

		btnLogin.setOnClickListener(loginClickListener);
	}

	// 载入之前保存的用户名、密码、服务器IP信息
	private void loadSavedInfo() {
		SharedPreferences info = getPreferences(MODE_PRIVATE);
		String username = info.getString("username", null);
		String password = info.getString("password", null);
		String serverip = info.getString("serverip", null);

		edtUsername.setText(username);
		edtPassword.setText(password);
		edtServerIP.setText(serverip);

		if (username != null) {
			chbRememberUser.setChecked(true);
			rmbUser = true;
		}

		if (serverip != null) {
			chbRememberIP.setChecked(true);
			rmbIP = true;
		}
	}

	// 保存用户名、密码、服务器IP到preference文件
	private boolean saveInfo(String username, String password, String serverip,
			boolean rmbUser, boolean rmbIP) {
		SharedPreferences info = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		if (rmbUser) {
			editor.putString("username", username);
			editor.putString("password", password);
		}
		if (rmbIP) {
			editor.putString("serverip", serverip);
		}
		return editor.commit();
	}
}
