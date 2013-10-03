package com.lm.clientapp.model;

import java.io.Serializable;

public class CourseItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private long courseId;
	private String courseName;
	private String type;
	private String icon;
	private String scoreId;

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getScore() {
		return scoreId;
	}

	public void setScoreId(String scoreid) {
		this.scoreId = scoreid;
	}
}
