package com.lm.clientapp;

import android.app.Application;

//用来保存全局变量的类
public class ClientApp extends Application {
	// 全局变量ServerIP
	private String serverIP;
	// 全局变量userId
	private String userId;

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverip) {
		this.serverIP = serverip;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userid) {
		this.userId = userid;
	}
}
