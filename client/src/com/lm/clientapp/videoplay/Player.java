package com.lm.clientapp.videoplay;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

public class Player implements OnBufferingUpdateListener, OnCompletionListener,
		MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {
	private static final int UPDATE_PROGRESSBAR = 10;
	private static final int PLAYER_START = 11;
	private int videoWidth;
	private int videoHeight;
	public MediaPlayer mediaPlayer;
	private SurfaceHolder surfaceHolder;
	private SeekBar skbProgress;
	private Timer mTimer = new Timer();

	public Player(SurfaceView surfaceView, SeekBar skbProgress) {
		this.skbProgress = skbProgress;
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mTimer.schedule(mTimerTask, 0, 1000);

		mediaPlayer = new MediaPlayer();
	}

	// 通过定时器和Handler来更新进度条
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
				playerhandle.sendEmptyMessage(UPDATE_PROGRESSBAR);
			}
		}
	};

	Handler playerhandle = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_PROGRESSBAR:
				int position = mediaPlayer.getCurrentPosition();
				int duration = mediaPlayer.getDuration();

				if (duration > 0) {
					long pos = skbProgress.getMax() * position / duration;
					skbProgress.setProgress((int) pos);
				}
				break;
			case PLAYER_START:
				Log.d("mediaPlayer", "start player");
				play();
				break;
			default:
				break;
			}

		};
	};

	// 播放视频
	public void play() {
		if (!mediaPlayer.isPlaying()) {
			mediaPlayer.start();
		}
	}

	public void playUrl(String videoUrl) {
		// mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepare();// prepare之后自动播放
			// mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pause() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		} else {
			mediaPlayer.start();
		}
	}

	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		Log.d("mediaPlayer", "stop player");
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceholder, int arg1, int arg2,
			int arg3) {
		Log.d("mediaPlayer", "surface changed");
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceholder) {
		try {
			// mediaPlayer = new MediaPlayer();
			mediaPlayer.setDisplay(surfaceHolder);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}
		Log.d("mediaPlayer", "surface created");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		Log.d("mediaPlayer", "surface destroyed");
	}

	@Override
	// 通过onPrepared播放
	public void onPrepared(MediaPlayer arg0) {
		videoWidth = mediaPlayer.getVideoWidth();
		videoHeight = mediaPlayer.getVideoHeight();
		if (videoHeight != 0 && videoWidth != 0) {
			arg0.start();
			arg0.pause();
		}
		Log.d("mediaPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		Log.d("mediaPlayer", "onCompletion");
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		skbProgress.setSecondaryProgress(bufferingProgress);
		int currentProgress = skbProgress.getMax()
				* mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
		// Log.e(currentProgress + "% play", bufferingProgress + "% buffer");

		if ((bufferingProgress >= 5) && (bufferingProgress < 6)) {
			playerhandle.sendEmptyMessage(PLAYER_START);
			Log.d("mediaPlayer", "ready to send msg");
		}
	}
}