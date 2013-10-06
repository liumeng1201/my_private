package com.lm.clientapp.tools;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.lm.clientapp.model.CourseItem;

//解析课程树
public class CourseHandler extends DefaultHandler {
	private static final String TAG = "CourseHandler";
	// 用于存放解析出来的课程树信息
	private List<CourseItem> courseList;

	// 获取课程树
	public List<CourseItem> getCourseList() {
		return courseList;
	}

	// 初始化工作
	public void startDocument() throws SAXException {
		super.startDocument();
		courseList = new ArrayList<CourseItem>();
	}

	// 收尾的处理工作
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals("course")) {
			CourseItem item = new CourseItem();
			item.setCourseId(Long.parseLong(attributes.getValue("courseId")));
			item.setCourseName(attributes.getValue("courseName"));
			item.setType(attributes.getValue("type"));
			item.setIcon(attributes.getValue("largeIcon"));
			if ((attributes.getValue("scoreId")).equals("")
					|| (attributes.getValue("scoreId") == null)) {
				Log.d(TAG, "scoreid not exist");
			} else {
				item.setScoreId(attributes.getValue("scoreId"));
			}
			courseList.add(item);
		}
	}

}
