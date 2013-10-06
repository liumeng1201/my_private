package com.lm.clientapp.tools;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lm.clientapp.model.CaseItem;
import com.lm.clientapp.model.CaseSourceItem;
import com.lm.clientapp.model.CourseItem;

//解析当前课程
public class CurrentCourseHandler extends DefaultHandler {
	private static final String TAG = "CurrentCourseHandler";
	// 用于存放解析出来的当前课程信息
	private CourseItem currentCourse;
	private List<CaseItem> caseList;
	private CaseItem caseitem;
	private List<CaseSourceItem> caseSourceList;

	// 获取当前课程
	public CourseItem getCurrentCourse() {
		return currentCourse;
	}

	// 初始化工作
	public void startDocument() throws SAXException {
		super.startDocument();
		currentCourse = new CourseItem();
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
			currentCourse = new CourseItem();
			// 用于存放当前课程的caselist信息
			caseList = new ArrayList<CaseItem>();
			currentCourse.setCourseId(Long.parseLong(attributes.getValue("courseId")));
			currentCourse.setCourseName(attributes.getValue("courseName"));
			currentCourse.setCaseList(caseList);
		}
		if (localName.equals("case")) {
			caseitem = new CaseItem();
			// 用于存放当前case的sourcelist信息
			caseSourceList = new ArrayList<CaseSourceItem>();
			caseitem.setCaseId(Long.parseLong(attributes.getValue("caseId")));
			caseitem.setCaseName(attributes.getValue("caseName"));
			caseitem.setCaseKindId(Integer.parseInt(attributes.getValue("caseKindId")));
			caseitem.setSourceUrl(attributes.getValue("sourceUrl"));
			caseitem.setEngineId(Long.parseLong(attributes.getValue("engineId")));
			caseitem.setCaseSourceList(caseSourceList);
			caseList.add(caseitem);
		}
		if (localName.equals("casesource")) {
			CaseSourceItem casesourceitem = new CaseSourceItem();
			casesourceitem.setSourceId(Long.parseLong(attributes.getValue("sourceId")));
			casesourceitem.setSourceName(attributes.getValue("sourceName"));
			casesourceitem.setSourceUrl(attributes.getValue("sourceUrl"));
			casesourceitem.setMediaTypeId(Integer.parseInt(attributes.getValue("mediaTypeId")));
			casesourceitem.setSourceTypeId(Integer.parseInt(attributes.getValue("sourceTypeId")));
			casesourceitem.setSeq(Integer.parseInt(attributes.getValue("seq")));
			casesourceitem.setCaseId(Long.parseLong(attributes.getValue("caseId")));
			caseSourceList.add(casesourceitem);
		}
	}
}
