package com.lm.clientapp;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.lm.clientapp.pushnotification.ServiceManager;
import com.lm.clientapp.videoplay.Player;

public class MainActivity extends Activity {
	private String LOGTAG = "MainActivity";
	private Context mContext;

	private WebView content_WebView;
	private EditText edtInputUrl;
	private Button btnLoadUrl;
	private ImageButton btnSettings;

	private RelativeLayout content_Video;
	private SurfaceView video_surfaceview;
	private RelativeLayout video_control;
	private SeekBar video_seekbar;
	private Button video_btnpause;
	private Button content_video_close;
	private Player video_player;

	// 接收推送消息服务的manager
	private ServiceManager serviceManager;
	// APN广播
	private APNReceiver apnReceiver;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = MainActivity.this;

		init();
		startAPNService();
		initAPNReceiver();
	}

	// 初始化各个组件实例及各个变量
	private void init() {
		content_WebView = (WebView) findViewById(R.id.content_webview);
		initWebView(content_WebView);

		content_Video = (RelativeLayout) findViewById(R.id.content_video);
		video_surfaceview = (SurfaceView) findViewById(R.id.video_surfaceview);
		video_control = (RelativeLayout) findViewById(R.id.video_control);
		video_seekbar = (SeekBar) findViewById(R.id.video_seekbar);
		video_btnpause = (Button) findViewById(R.id.video_btnpause);
		content_video_close = (Button) findViewById(R.id.content_video_close);

		edtInputUrl = (EditText) findViewById(R.id.inputurl);
		btnLoadUrl = (Button) findViewById(R.id.loadurl);
		btnLoadUrl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = edtInputUrl.getText().toString();
				setContent(url);
				// content_WebView.loadUrl(url);
			}
		});

		btnSettings = (ImageButton) findViewById(R.id.userinfo_setting);
		btnSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转至消息推送设置界面
				ServiceManager.viewNotificationSettings(mContext);
			}
		});

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				// 接收到服务器端推动的信息之后更新内容
				if (msg.what == Utils.REFRESH_CONTENT) {
					Map map = (Map) msg.obj;
					Log.d(LOGTAG, "notificationId="
							+ map.get("notificationId").toString());
					Log.d(LOGTAG, "notificationApiKey="
							+ map.get("apiKey").toString());
					Log.d(LOGTAG, "notificationTitle="
							+ map.get("title").toString());
					Log.d(LOGTAG, "notificationMessage="
							+ map.get("message").toString());
					Log.d(LOGTAG, "notificationUri="
							+ map.get("uri").toString());

					setContent(map.get("message").toString());
					// content_WebView.loadUrl(map.get("message").toString());
				}
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (serviceManager != null) {
			// 退出程序时停止接收推送的消息
			serviceManager.stopService();
		}

		if (apnReceiver != null) {
			// 解除APNReceiver广播
			unregisterReceiver(apnReceiver);
		}

		if (video_player != null) {
			video_player.stop();
		}
	}

	// 启动消息推送服务
	private void startAPNService() {
		serviceManager = new ServiceManager(this);
		serviceManager.setNotificationIcon(R.drawable.ic_launcher);
		serviceManager.startService();
	}

	// 初始化APNReceiver广播
	private void initAPNReceiver() {
		apnReceiver = new APNReceiver(mHandler);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Utils.SEND_RECEIVER_MESSAGE);
		registerReceiver(apnReceiver, filter);
	}

	// 初始化WebView设置
	private void initWebView(WebView webview) {
		WebSettings setting = webview.getSettings();
		// 设置支持flash插件
		setting.setPluginState(PluginState.ON);
		setting.setJavaScriptEnabled(true);
		// 设置根据页面宽度自动进行缩放
		setting.setLoadWithOverviewMode(true);
		setting.setUseWideViewPort(true);
		// 设置支持多点触摸并显示缩放按钮
		setting.setSupportZoom(true);
		setting.setBuiltInZoomControls(true);

		webview.setWebViewClient(new WebViewClient() {
			// 在当前WebView中加载所有url
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	// 设置内容区域要显示的内容
	// .mp4结尾的url使用视频播放器来显示
	// 其余url使用WebView来显示
	private void setContent(String url) {
		String arraytmp[] = url.split("\\.");
		String suffix = arraytmp[arraytmp.length - 1];
		if (suffix.equalsIgnoreCase("mp4")) {
			// 显示播放器
			showAndSetVideoView(url);
		} else {
			// 显示WebView
			showWebView(url);
		}
	}

	// 显示content_WebView
	private void showWebView() {
		content_Video.setVisibility(View.GONE);
		content_WebView.setVisibility(View.VISIBLE);
	}

	private void showWebView(String url) {
		showWebView();
		content_WebView.loadUrl(url);
	}

	// 显示并设置content_Video
	private void showAndSetVideoView(String url) {
		content_WebView.setVisibility(View.GONE);
		content_Video.setVisibility(View.VISIBLE);
		video_control.setVisibility(View.VISIBLE);
		video_seekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		video_btnpause.setOnClickListener(new VideoPlayControlEvent(url));
		content_video_close.setOnClickListener(new VideoPlayControlEvent(url));
		video_player = new Player(video_surfaceview, video_seekbar);

		video_player.playUrl(url);
	}

	class VideoPlayControlEvent implements OnClickListener {
		private String url;

		VideoPlayControlEvent(String url) {
			this.url = url;
		}

		@Override
		public void onClick(View view) {
			if (view == video_btnpause) {
				video_player.pause();
			} else if (view == content_video_close) {
				video_player.stop();
				showWebView();
			}
		}
	}

	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progress;

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
}
