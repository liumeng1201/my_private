package com.lm.clientapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lm.clientapp.listtree.ExpandeAdapter;
import com.lm.clientapp.listtree.ListItem;
import com.lm.clientapp.listtree.TreeBtnOnClickListener;
import com.lm.clientapp.pushnotification.ServiceManager;
import com.lm.clientapp.utils.MyDialog;
import com.lm.clientapp.utils.Utils;
import com.lm.clientapp.videoplay.Player;
import com.lm.clientapp.videoplay.SeekBarChangeEvent;
import com.lm.clientapp.videoplay.VideoPlayControlEvent;

public class MainActivity extends Activity {
	private String LOGTAG = "MainActivity";
	private Context mContext;

	// 学生头像
	private ImageView user_avatar;
	// 学生名
	private TextView user_name;
	// 学生班级
	private TextView user_class;
	// 设置按钮
	private ImageButton btnSettings;

	private Button tree_btn1;
	private Button tree_btn2;
	private ExpandableListView tree_list1;
	private ExpandableListView tree_list2;
	private ExpandeAdapter tree_list_adapter1;
	private ExpandeAdapter tree_list_adapter2;
	private String[] groups1;
	private List<List<ListItem>> list1;
	private String[] groups2;
	private List<List<ListItem>> list2;

	// 用来显示swf/图片的webview
	private WebView content_WebView;

	// 用来放置视频播放及控制按钮的布局
	private FrameLayout content_Video;
	// 用来显示视频
	private SurfaceView video_surfaceview;
	// 用来放置surfaceview的容器
	private FrameLayout video_surfaceview_content;
	// 视频播放控制
	private RelativeLayout video_control;
	// 视频播放进度条
	private SeekBar video_seekbar;
	// 视频播放暂停按钮
	private Button video_btnpause;
	// 关闭视频按钮
	private Button content_video_close;
	// 视频播放器类实例
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

		Intent intent = getIntent();
		Toast.makeText(mContext, intent.getStringExtra("userid"),
				Toast.LENGTH_LONG).show();

		init();
		startAPNService();
		initAPNReceiver();
	}

	// 初始化各个组件实例及各个变量
	private void init() {
		user_avatar = (ImageView) findViewById(R.id.userinfo_useravatar);
		user_name = (TextView) findViewById(R.id.userinfo_username);
		user_class = (TextView) findViewById(R.id.userinfo_userclass);
		setUserInfo();

		initTreeLayout();

		content_WebView = (WebView) findViewById(R.id.content_webview);
		initWebView(content_WebView);

		video_surfaceview_content = (FrameLayout) findViewById(R.id.video_surfaceview_content);
		content_Video = (FrameLayout) findViewById(R.id.content_video);
		video_control = (RelativeLayout) findViewById(R.id.video_control);
		video_seekbar = (SeekBar) findViewById(R.id.video_seekbar);
		video_btnpause = (Button) findViewById(R.id.video_btnpause);
		content_video_close = (Button) findViewById(R.id.content_video_close);

		btnSettings = (ImageButton) findViewById(R.id.userinfo_setting);
		btnSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转至消息推送设置界面
				ServiceManager.viewNotificationSettings(mContext);
			}
		});

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
				case Utils.REFRESH_CONTENT:
					// 接收到服务器端推动的信息之后更新内容
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
					break;
				case Utils.SHOW_WEB_VIEW:
					showWebView();
					break;
				default:
					break;
				}
			}
		};
	}

	// 初始化tree_layout布局
	private void initTreeLayout() {
		tree_btn1 = (Button) findViewById(R.id.tree_btn1);
		tree_btn2 = (Button) findViewById(R.id.tree_btn2);
		tree_list1 = (ExpandableListView) findViewById(R.id.tree_listview1);
		tree_list2 = (ExpandableListView) findViewById(R.id.tree_listview2);

		String[] groups1 = getResources().getStringArray(R.array.province);
		int[] province = new int[] { R.array.beijing, R.array.shanghai,
				R.array.tianjing, R.array.chongqing };
		int[] province_des = new int[] { R.array.beijing_des,
				R.array.shanghai_des, R.array.tianjing_des,
				R.array.chongqing_des };
		String[] groups2 = getResources().getStringArray(R.array.fruit);
		int[] fruit = new int[] { R.array.apple, R.array.banana };
		int[] fruit_des = new int[] { R.array.apple_des, R.array.banana_des };
		list1 = initData(province, province_des);
		list2 = initData(fruit, fruit_des);

		tree_list_adapter1 = new ExpandeAdapter(mContext, groups1, list1);
		tree_list_adapter2 = new ExpandeAdapter(mContext, groups2, list2);

		tree_list1.setAdapter(tree_list_adapter1);
		tree_list2.setAdapter(tree_list_adapter2);

		tree_btn1.setOnClickListener(new TreeBtnOnClickListener(mContext,
				tree_btn1, tree_btn2, tree_list1, tree_list2));
		tree_btn2.setOnClickListener(new TreeBtnOnClickListener(mContext,
				tree_btn1, tree_btn2, tree_list1, tree_list2));
	}

	private List<List<ListItem>> initData(int[] grouparray, int[] grouparray_des) {
		List<List<ListItem>> data = new ArrayList<List<ListItem>>();
		for (int i = 0; i < grouparray.length; i++) {
			List<ListItem> list = new ArrayList<ListItem>();
			String[] childs = getResources().getStringArray(grouparray[i]);
			String[] details = getResources().getStringArray(grouparray_des[i]);
			for (int j = 0; j < childs.length; j++) {
				ListItem item = new ListItem(null, childs[j], details[j]);
				list.add(item);
			}
			data.add(list);
		}
		return data;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (serviceManager != null) {
			// 退出程序时停止接收推送的消息
			serviceManager.stopService();
		}

		if (apnReceiver != null) {
			// 解除APNReceiver广播
			unregisterReceiver(apnReceiver);
		}

		stopVideoPlayer();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyDialog dialog = new MyDialog(mContext);
			dialog.setMessage(R.string.exit);
			dialog.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MainActivity.this.finish();
						}
					});
			dialog.show();
		}
		return super.onKeyDown(keyCode, event);
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
			showVideoView(url);
		} else {
			// 显示WebView
			showWebView(url);
		}
	}

	// 显示content_WebView
	private void showWebView() {
		stopVideoPlayer();
		content_Video.setVisibility(View.GONE);
		content_WebView.setVisibility(View.VISIBLE);
	}

	private void showWebView(String url) {
		showWebView();
		content_WebView.loadUrl(url);
	}

	// 停止视频播放
	private void stopVideoPlayer() {
		if (video_player != null) {
			video_player.stop();
		}
		// 如果surfaceview不为空则将surfaceview的容器中的surfaceview移除并销毁该surfaceview
		if (video_surfaceview != null) {
			video_surfaceview_content.removeAllViews();
			video_surfaceview.destroyDrawingCache();
			video_surfaceview = null;
		}
	}

	// 显示并设置content_Video
	private void showVideoView(String url) {
		content_WebView.setVisibility(View.GONE);
		content_Video.setVisibility(View.VISIBLE);
		video_control.setVisibility(View.VISIBLE);

		// 每次播放视频时均重新创建一个surfaceview来使用
		video_surfaceview = new SurfaceView(mContext);
		video_surfaceview_content.addView(video_surfaceview,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		video_player = new Player(video_surfaceview, video_seekbar);
		video_seekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent(
				video_player));
		video_btnpause.setOnClickListener(new VideoPlayControlEvent(mHandler,
				url, video_player));
		content_video_close.setOnClickListener(new VideoPlayControlEvent(
				mHandler, url, video_player));

		video_player.playUrl(url);
	}

	private void setUserInfo() {
		// TODO 获取网络资源之后设置用户信息
	}
}
