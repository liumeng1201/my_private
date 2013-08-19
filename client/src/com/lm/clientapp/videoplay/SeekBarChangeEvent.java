package com.lm.clientapp.videoplay;

import android.widget.SeekBar;

public class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
	private int progress;
	private Player video_player;

	public SeekBarChangeEvent(Player mp) {
		this.video_player = mp;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
		this.progress = progress * video_player.mediaPlayer.getDuration()
				/ seekBar.getMax();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
		video_player.mediaPlayer.seekTo(progress);
	}
}
