package com.lm.clientapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.lm.clientapp.utils.HttpTool;
import com.lm.clientapp.utils.MyDialog;
import com.lm.clientapp.utils.Utils;

public class LoginActivity extends Activity {
	private String TAG = "LoginActivity";

	// 存放全局变量
	ClientApp clientApp;
	// 登录按钮
	private Button btnLogin;
	// 用户名、密码、服务器IP输入框
	private EditText edtUsername, edtPassword, edtServerIP;
	// 记住用户名密码、服务器IP复选框
	private CheckBox chbRememberUser, chbRememberIP;
	// 是否记住用户名密码标志
	private boolean rmbUser = false;
	// 是否记住服务器IP标志
	private boolean rmbIP = false;
	// 提示安装FlashPlayer的dialog
	private MyDialog fpiDialog;

	// 登陆提示对话框
	private ProgressDialog loginDialog;
	private static final int SHOW_LOGIN_DIALOG = 1000;
	private static final int CANCEL_LOGIN_DIALOG = 1001;

	private Context mContext;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOW_LOGIN_DIALOG:
				loginDialog.show();
				break;
			case CANCEL_LOGIN_DIALOG:
				loginDialog.dismiss();
				break;
			}
		}
	};

	// 登录按钮点击事件监听
	private OnClickListener loginClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final String username = edtUsername.getText().toString();
			final String password = edtPassword.getText().toString();
			final String serverip = edtServerIP.getText().toString();
			rmbUser = chbRememberUser.isChecked();
			rmbIP = chbRememberIP.isChecked();

			if ((username.equals("")) || (password.equals(""))
					|| (serverip.equals(""))) {
				MyDialog dialog = new MyDialog(mContext);
				dialog.setMessage(R.string.edittext_null);
				dialog.setNegativeButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				dialog.show();
			} else {
				// 用户登陆操作http请求地址
				final String url = "http://" + serverip
						+ getResources().getString(R.string.loginAction);
				// 用户登录操作http传输数据
				final List<NameValuePair> datas = new ArrayList<NameValuePair>();
				// 添加数据
				datas.add(new BasicNameValuePair("username", username));
				datas.add(new BasicNameValuePair("password", password));
				// 登陆提示进度条对话框
				mHandler.sendEmptyMessage(SHOW_LOGIN_DIALOG);
				// 在新线程中进行登陆操作
				new Thread() {
					@Override
					public void run() {
						super.run();
						if (login(url, datas)) {
							// 登陆成功,保存当前的用户输入以方便下次登陆

							if (saveInfo(username, password, serverip, rmbUser,
									rmbIP)) {
								Log.d(TAG, "save success");
							}

							// 将serverIP,userName保存至全局变量中
							clientApp.setServerIP(serverip);
							clientApp.setUserName(username);

							// 启动主界面Activity
							Intent intent = new Intent(mContext,
									MainActivity.class);
							intent.putExtra("userid", clientApp.getUserId());
							intent.putExtra("userrealname",
									clientApp.getUserRealName());
							startActivity(intent);

							// 取消登陆提示进度条对话框
							mHandler.sendEmptyMessage(CANCEL_LOGIN_DIALOG);
							LoginActivity.this.finish();
						} else {
							// 登陆失败,弹出对应的错误提示并取消登陆提示进度条对话框
							mHandler.sendEmptyMessage(CANCEL_LOGIN_DIALOG);
						}
					}
				}.start();

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mContext = LoginActivity.this;

		init();
		isFlashPlayerInstalled();
	}

	// 初始化各个组件实例
	private void init() {
		clientApp = (ClientApp) getApplicationContext();

		btnLogin = (Button) findViewById(R.id.btn_login);
		edtUsername = (EditText) findViewById(R.id.input_username);
		edtPassword = (EditText) findViewById(R.id.input_password);
		edtServerIP = (EditText) findViewById(R.id.input_serverip);
		chbRememberUser = (CheckBox) findViewById(R.id.chb_rememberuser);
		chbRememberIP = (CheckBox) findViewById(R.id.chb_rememberip);
		loginDialog = new ProgressDialog(mContext);
		loginDialog.setCancelable(false);
		loginDialog.setMessage(getResources().getString(R.string.login_tishi));

		initfpiDialog();

		loadSavedInfo();

		btnLogin.setOnClickListener(loginClickListener);
	}

	// 登陆
	private boolean login(String url, List<NameValuePair> datas) {
		// 发送http请求,获得服务器返回数据
		InputStream is = HttpTool.sendDataByPost(url, datas);
		if (is != null) {
			// 获取返回的数据
			String result = HttpTool.convertStreamToString(is);
			// 根据返回的数据获取用户ID
			String userId = getUserIdFromResult(result);
			// 验证用户身份
			if (("null".equals(userId)) || ("0".equals(userId))) {
				// 验证失败
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mContext, R.string.user_error,
								Toast.LENGTH_SHORT).show();
					}
				});
				return false;
			} else {
				// 验证成功
				String userRealName = getRealNameFromResult(result);
				Bitmap userPortrait = getProfileImageFromResult(result);
				int userTypeId = getUserTypeIdFromResult(result);
				// 保存userId,userRealName,userPortrait到全局变量中
				clientApp.setUserId(userId);
				clientApp.setUserRealName(userRealName);
				clientApp.setUserPortrait(userPortrait);
				clientApp.setUserTypeId(userTypeId);
				return true;
			}
		} else {
			// 网络链接失败
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(mContext, R.string.net_error,
							Toast.LENGTH_SHORT).show();
				}
			});
			return false;
		}
	}

	// 从服务器返回的数据中获取用户ID
	private String getUserIdFromResult(String result) {
		if (result != null) {
			String array[] = result.split("\\$");
			return array[0];
		} else {
			return null;
		}
	}

	// 从服务器返回的数据中获取用户真实名字
	private String getRealNameFromResult(String result) {
		String array[] = result.split("\\$");
		return array[1];
	}

	// 从服务器返回的数据中获取用户头像
	private Bitmap getProfileImageFromResult(String result) {
		String array[] = result.split("\\$");
		// 用户头像对应的url
		String portraits[] = (array[2].replace('\\', '/')).split("/");
		// 转换为assets目录下对应的文件名
		String portrait = portraits[1] + "/" + portraits[2] + "/"
				+ portraits[3];
		Bitmap image = null;
		AssetManager am = getResources().getAssets();
		try {
			InputStream is = am.open(portrait);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 如果没有设置用户头像或用户头像为空则使用默认的用户头像
			if (image == null) {
				image = BitmapFactory.decodeResource(getResources(),
						R.drawable.default_portrait);
			}
		}
		return image;
	}

	// 从服务器返回的数据中获取用户类型标识
	private int getUserTypeIdFromResult(String result) {
		String array[] = result.split("\\$");
		// userTypeId对应的string
		return Integer.parseInt(array[3]);
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
			fpiDialog.show();
		}
	}

	// 初始化flash player install Dialog
	private void initfpiDialog() {
		fpiDialog = new MyDialog(mContext);
		fpiDialog.setMessage(R.string.fpidialog_install);
		fpiDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
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
						dialog.dismiss();
					}
				});
	}
}
