package com.lm.clientapp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class Utils {
	public static final String LOGIN_SHAREPREFERENCES = "client_login_preferences"; 
	public static final String SEND_RECEIVER_MESSAGE = "com.lm.clientapp.action.sendReceiverMessage";
	
	public static final int REFRESH_CONTENT = 1000;
	
	// 判断flashplayer是否已经安装
	public static boolean checkIfFlashPlayerInstall(Context context) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> infoList = pm
				.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if ("com.adobe.flashplayer".equals(info.packageName)) {
				return true;
			}
		}
		return false;
	}

	// 复制assets目录下的apk文件到SD卡
	public static boolean copyApkFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open(fileName);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}
}
