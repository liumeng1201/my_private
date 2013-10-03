package com.lm.clientapp.model;

import java.util.List;
import java.util.Map;

public class PushItem {
	// 推送学生列表
	private List<String> studentList;
	// 当前课程流
	private String currentCourse;
	// 当前老师
	private String currentTeacher;
	// 当前模式,1推送,2自主
	private String currentMode;
	// 当前案例
	private String currentCase;
	// 当前caseSource表所对应的内容
	// private List<Map<String, String>> currentCaseSource;
	private CaseSourceItem caseSourceItem;

	public void setStudentList(List<String> studentList) {
		this.studentList = studentList;
	}

	public List<String> getStudentList() {
		return studentList;
	}

	public void setCurrentCourse(String currentCourse) {
		this.currentCourse = currentCourse;
	}

	public String getCurrentCourse() {
		return currentCourse;
	}

	public void setCurrentTeacher(String currentTeacher) {
		this.currentTeacher = currentTeacher;
	}

	public String getCurrentTeacher() {
		return currentTeacher;
	}

	public void setCurrentMode(String currentMode) {
		this.currentMode = currentMode;
	}

	public String getCurrentMode() {
		return currentMode;
	}

	public void setCurrentCase(String currentCase) {
		this.currentCase = currentCase;
	}

	public String getCurrentCase() {
		return currentCase;
	}

	// public void setCurrentCaseSource(List<Map<String, String>>
	// currentCaseSource) {
	// this.currentCaseSource = currentCaseSource;
	// }
	//
	// public List<Map<String, String>> getCurrentCaseSource() {
	// return currentCaseSource;
	// }

	public void setCurrentCaseSource(CaseSourceItem caseSourceItem) {
		this.caseSourceItem = caseSourceItem;
	}

	public CaseSourceItem getCurrentCaseSource() {
		return caseSourceItem;
	}
}
