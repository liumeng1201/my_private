package com.lm.clientapp;

import android.app.Application;

//用来保存全局变量的类
public class ClientApp extends Application {
	// 全局变量ServerIP
	private String serverIP;

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverip) {
		this.serverIP = serverip;
	}
}
