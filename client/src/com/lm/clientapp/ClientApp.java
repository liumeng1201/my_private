package com.lm.clientapp;

import android.app.Application;
import android.graphics.Bitmap;

import com.lm.clientapp.model.CourseItem;

//用来保存全局变量的类
public class ClientApp extends Application {
	// 全局变量ServerIP
	private String serverIP;
	// 全局变量userId
	private String userId;
	// 用户名
	private String userName;
	// 用户真实名
	private String userRealName;
	// 用户头像
	private Bitmap userPortrait;
	// 用类类型标识
	private int userTypeId;
	// 当前课程流信息
	private CourseItem courseItem;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userrealname) {
		this.userRealName = userrealname;
	}

	public Bitmap getUserPortrait() {
		return userPortrait;
	}

	public void setUserPortrait(Bitmap up) {
		this.userPortrait = up;
	}

	public int getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}

	public CourseItem getCourseItem() {
		return courseItem;
	}

	public void setCourseItem(CourseItem courseItem) {
		this.courseItem = courseItem;
	}
}
