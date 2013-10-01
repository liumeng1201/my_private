package com.geolo.android;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.geolo.android.flash.FlashActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

public class FileBrowser extends ListActivity {
	private static final FileFilter FILTER = new FileFilter() {
		public boolean accept(File f) {
			//return f.isDirectory() || f.getName().matches("^.*?\\.(jpg|png|bmp|gif)$");
			return true;
		}
	};
	private FileListAdapter fileList;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		File sdcard = android.os.Environment.getExternalStorageDirectory();
		fill(sdcard);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && !fileList.isRoot()) {
			fill(fileList.getItem(1));
			Intent intent = new Intent();
			intent.setClass(FileBrowser.this, FlashActivity.class);
			startActivity(intent);
			//return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void fill(File folder) {
		List<File> files = new ArrayList<File>();
		files.add(folder);
		if (folder.getParentFile() != null){
			files.add(folder.getParentFile());
		}
		for (File file : folder.listFiles(FILTER)) {
			files.add(file);
		}
		fileList = new FileListAdapter(this, android.R.layout.simple_list_item_1, files);
		setListAdapter(fileList);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		File file = fileList.getItem(position);
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		if (file.isDirectory()){
			fill(file);
		}else if(file.getName().matches("^.*?\\.(jpg|png|bmp|gif)$")){
			intent.setDataAndType(Uri.fromFile(file), "image/*");
			startActivity(intent);
		}else if(file.getName().matches("^.*?\\.(swf)$")){
			intent.setClass(FileBrowser.this, FlashActivity.class);
			intent.putExtra("fileName", file.getAbsolutePath().replace("/mnt", ""));
			startActivity(intent);
			FileBrowser.this.finish();
		}
	}
}
