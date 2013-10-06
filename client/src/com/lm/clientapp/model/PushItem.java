package com.lm.clientapp.model;

import java.util.List;

public class PushItem {
	// 推送学生列表
	private List<String> studentList;
	// 当前课程流id
	private long currentCourse;
	// 当前老师
	private String currentTeacher;
	// 当前模式:1推送,2自主
	private int currentMode;
	// 当前案例
	private CaseItem caseItem;
	// 当前caseSource表所对应的内容
	private CaseSourceItem caseSourceItem;

	public void setStudentList(List<String> studentList) {
		this.studentList = studentList;
	}

	public List<String> getStudentList() {
		return studentList;
	}

	public void setCurrentCourse(long currentCourse) {
		this.currentCourse = currentCourse;
	}

	public long getCurrentCourse() {
		return currentCourse;
	}

	public void setCurrentTeacher(String currentTeacher) {
		this.currentTeacher = currentTeacher;
	}

	public String getCurrentTeacher() {
		return currentTeacher;
	}

	public void setCurrentMode(int currentMode) {
		this.currentMode = currentMode;
	}

	public int getCurrentMode() {
		return currentMode;
	}

	public void setCaseItem(CaseItem caseItem) {
		this.caseItem = caseItem;
	}

	public CaseItem getCaseItem() {
		return caseItem;
	}

	public void setCurrentCaseSource(CaseSourceItem caseSourceItem) {
		this.caseSourceItem = caseSourceItem;
	}

	public CaseSourceItem getCurrentCaseSource() {
		return caseSourceItem;
	}
}
