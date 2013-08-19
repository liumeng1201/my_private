package com.lm.clientapp.listtree;

import android.graphics.Bitmap;

public class ListItem {
	private Bitmap icon;
	private String name;
	private String detail;

	public ListItem(Bitmap icon, String name, String detail) {
		this.icon = icon;
		this.name = name;
		this.detail = detail;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}

	public Bitmap getIcon() {
		return icon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDetail() {
		return detail;
	}

	public String toString() {
		return "Item[" + name + ", " + detail + "]";
	}
}
