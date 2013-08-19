package com.lm.clientapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.lm.clientapp.utils.Utils;

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
	// 提示安装FlashPlayer的dialog
	private AlertDialog.Builder fpiDialog;

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

			if ((username != "") && (password != "") && (serverip != "")) {
				if (saveInfo(username, password, serverip, rmbUser, rmbIP)) {
					Log.d(TAG, "save success");
				}

				// 将服务器保存至全局变量中
				ClientApp clientApp = (ClientApp) getApplication();
				clientApp.setServerIP(serverip);

				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			} else {
				new AlertDialog.Builder(mContext)
						.setTitle(R.string.tishi)
						.setMessage(R.string.edittext_null)
						.setNegativeButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								}).create().show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mContext = LoginActivity.this;

		init();
		isFlashPlayerInstalled();
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

		initfpiDialog();

		loadSavedInfo();

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginActivity.this.finish();
				// Intent intent = new Intent(mContext, apnActivity.class);
				// startActivity(intent);
			}
		});

		btnLogin.setOnClickListener(loginClickListener);
	}

	// 载入之前保存的用户名、密码、服务器IP信息
	private void loadSavedInfo() {
		SharedPreferences info = mContext.getSharedPreferences(
				Utils.LOGIN_SHAREPREFERENCES, MODE_PRIVATE);
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
		SharedPreferences info = mContext.getSharedPreferences(
				Utils.LOGIN_SHAREPREFERENCES, MODE_PRIVATE);
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
}
