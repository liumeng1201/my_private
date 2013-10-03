package com.lm.clientapp.model;

public class CaseSourceItem {
	private long sourceId;
	private String sourceName;
	private String sourceUrl;
	private int mediaTypeId;
	private int sourceTypeId;
	private int seq;
	private long caseId;

	public void setSourceId(long sourceId) {
		this.sourceId = sourceId;
	}

	public long getSourceId() {
		return sourceId;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setMediaTypeId(int mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
	}

	public int getMediaTypeId() {
		return mediaTypeId;
	}

	public void setSourceTypeId(int sourceTypeId) {
		this.sourceTypeId = sourceTypeId;
	}

	public int getSourceTypeId() {
		return sourceTypeId;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getSeq() {
		return seq;
	}

	public void setCaseId(long caseId) {
		this.caseId = caseId;
	}

	public long getCaseId() {
		return caseId;
	}
}
