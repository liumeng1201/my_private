package com.lm.clientapp.listtree;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class TreeViewUtils {

	public static List<List<ListItem>> convertDataForListview(Context context,
			int[] grouparray, int[] grouparray_des) {
		List<List<ListItem>> data = new ArrayList<List<ListItem>>();
		for (int i = 0; i < grouparray.length; i++) {
			List<ListItem> list = new ArrayList<ListItem>();
			String[] childs = context.getResources().getStringArray(
					grouparray[i]);
			String[] details = context.getResources().getStringArray(
					grouparray_des[i]);
			for (int j = 0; j < childs.length; j++) {
				ListItem item = new ListItem(null, childs[j], details[j]);
				list.add(item);
			}
			data.add(list);
		}
		return data;
	}

	public static List<List<SimpleListItem>> convertSimpleDataForListview(
			Context context, int[] grouparray) {
		List<List<SimpleListItem>> data = new ArrayList<List<SimpleListItem>>();
		for (int i = 0; i < grouparray.length; i++) {
			List<SimpleListItem> list = new ArrayList<SimpleListItem>();
			String[] childs = context.getResources().getStringArray(
					grouparray[i]);
			for (int j = 0; j < childs.length; j++) {
				SimpleListItem item = new SimpleListItem(childs[j]);
				list.add(item);
			}
			data.add(list);
		}
		return data;
	}
}
