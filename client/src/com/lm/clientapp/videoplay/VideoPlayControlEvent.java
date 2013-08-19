package com.lm.clientapp.videoplay;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.lm.clientapp.R;
import com.lm.clientapp.utils.Utils;

public class VideoPlayControlEvent implements OnClickListener {
	private String url;
	private Player video_player;
	private Handler handler;

	public VideoPlayControlEvent(Handler handler, String url, Player mp) {
		this.url = url;
		this.video_player = mp;
		this.handler = handler;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.video_btnpause) {
			video_player.pause();
		} else if (view.getId() == R.id.content_video_close) {
			handler.sendEmptyMessage(Utils.SHOW_WEB_VIEW);
		}
	}
}