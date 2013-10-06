package com.lm.clientapp.model;

import java.util.List;

public class CaseItem {
	private long caseId;
	private String caseName;
	private String caseCode;
	private int caseKindId;
	private String sourceUrl;
	private long engineId;
	// 一个case里可能会有多个caseSource
	private List<CaseSourceItem> caseSourceList;

	public void setCaseId(long caseId) {
		this.caseId = caseId;
	}

	public long getCaseId() {
		return caseId;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}

	public String getCaseCode() {
		return caseCode;
	}

	public void setCaseKindId(int caseKindId) {
		this.caseKindId = caseKindId;
	}

	public int getCaseKindId() {
		return caseKindId;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setEngineId(long engineId) {
		this.engineId = engineId;
	}

	public long getEngineId() {
		return engineId;
	}

	public void setCaseSourceList(List<CaseSourceItem> caseSourceList) {
		this.caseSourceList = caseSourceList;
	}

	public List<CaseSourceItem> getCaseSourceList() {
		return caseSourceList;
	}
}
