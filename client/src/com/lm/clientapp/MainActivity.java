package com.lm.clientapp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

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
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lm.clientapp.listtree.SimpleExpandeAdapter;
import com.lm.clientapp.listtree.SimpleListItem;
import com.lm.clientapp.model.CourseItem;
import com.lm.clientapp.model.PushItem;
import com.lm.clientapp.pushnotification.ServiceManager;
import com.lm.clientapp.tools.CourseHandler;
import com.lm.clientapp.utils.HttpTool;
import com.lm.clientapp.utils.MyDialog;
import com.lm.clientapp.utils.Utils;
import com.lm.clientapp.videoplay.Player;
import com.lm.clientapp.videoplay.SeekBarChangeEvent;
import com.lm.clientapp.videoplay.VideoPlayControlEvent;

public class MainActivity extends Activity {
	private String LOGTAG = "MainActivity";
	private Context mContext;
	private ClientApp clientApp;

	// 学生头像
	private ImageView user_avatar;
	// 学生名
	private TextView user_name;

	private Button tree_btn1;
	private ExpandableListView tree_list1;
	private SimpleExpandeAdapter tree_list_adapter1;
	/*
	 * private Button tree_btn2; private ExpandableListView tree_list2; private
	 * ExpandeAdapter tree_list_adapter2; private String[] groups1; private
	 * List<List<ListItem>> list1; private String[] groups2; private
	 * List<List<ListItem>> list2;
	 */

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
		clientApp = (ClientApp) getApplicationContext();

		init();
		startAPNService();
		initAPNReceiver();
	}

	// 初始化各个组件实例及各个变量
	private void init() {
		user_avatar = (ImageView) findViewById(R.id.userinfo_useravatar);
		user_name = (TextView) findViewById(R.id.userinfo_username);
		setUserInfo();

		loadTreeLayout();

		content_WebView = (WebView) findViewById(R.id.content_webview);
		initWebView(content_WebView);

		video_surfaceview_content = (FrameLayout) findViewById(R.id.video_surfaceview_content);
		content_Video = (FrameLayout) findViewById(R.id.content_video);
		video_control = (RelativeLayout) findViewById(R.id.video_control);
		video_seekbar = (SeekBar) findViewById(R.id.video_seekbar);
		video_btnpause = (Button) findViewById(R.id.video_btnpause);
		content_video_close = (Button) findViewById(R.id.content_video_close);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
				case Utils.REFRESH_CONTENT:
					// 接收到服务器端推动的信息之后更新内容
					PushItem pi = (PushItem) msg.obj;
					Log.d(LOGTAG, "mode = " + pi.getCurrentMode()
							+ " teacher = " + pi.getCurrentTeacher()
							+ " course = " + pi.getCurrentCourse() + " caseName = "
							+ pi.getCaseItem().getCaseName());
					Log.d(LOGTAG, "sourceName = "
							+ pi.getCurrentCaseSource().getSourceName()
							+ " sourceUrl = "
							+ pi.getCurrentCaseSource().getSourceUrl()
							+ " caseId = "
							+ pi.getCurrentCaseSource().getCaseId());

					loadRes(pi);
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

	// 载入tree_layout布局
	private void loadTreeLayout() {
		tree_btn1 = (Button) findViewById(R.id.tree_btn1);
		tree_list1 = (ExpandableListView) findViewById(R.id.tree_listview1);

		final String[] groups1 = { "当前课程" };

		new Thread() {
			@Override
			public void run() {
				super.run();
				String url = "http://" + clientApp.getServerIP();
				final List<NameValuePair> datas = new ArrayList<NameValuePair>();
				switch (clientApp.getUserTypeId()) {
				case 10:
					// 学生
					datas.add(new BasicNameValuePair("stuid", clientApp.getUserId()));
					url = url + getResources().getString(R.string.courseStuAction);
					break;
				case 20:
					// 老师
					datas.add(new BasicNameValuePair("teaid", clientApp.getUserId()));
					url = url + getResources().getString(R.string.courseTeaAction);
					break;
				}
				List<CourseItem> list = getCourseList(url, datas);
				List<List<SimpleListItem>> listgroup = new ArrayList<List<SimpleListItem>>();
				List<SimpleListItem> lists = new ArrayList<SimpleListItem>();
				for (CourseItem item : list) {
					lists.add(new SimpleListItem(item.getCourseName()));
				}
				listgroup.add(lists);
				tree_list_adapter1 = new SimpleExpandeAdapter(mContext,
						groups1, listgroup);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// 将adapter与listview绑定以显示数据
						tree_list1.setAdapter(tree_list_adapter1);
					}
				});
			}
		}.start();

		tree_list1.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO 用户点击当前课程列表时需要进行的操作
				Toast.makeText(
						mContext,
						"you click group: " + groupPosition + " child: "
								+ childPosition, Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		/*
		 * //第一个ExpandableListView String[] groups1 =
		 * getResources().getStringArray(R.array.province); int[] province = new
		 * int[] { R.array.beijing, R.array.shanghai, R.array.tianjing,
		 * R.array.chongqing }; int[] province_des = new int[] {
		 * R.array.beijing_des, R.array.shanghai_des, R.array.tianjing_des,
		 * R.array.chongqing_des }; list1 =
		 * TreeViewUtils.convertDataForListview(mContext, province,
		 * province_des);
		 * 
		 * //第二个ExpandableListView tree_btn2 = (Button)
		 * findViewById(R.id.tree_btn2); tree_list2 = (ExpandableListView)
		 * findViewById(R.id.tree_listview2);
		 * 
		 * String[] groups2 = getResources().getStringArray(R.array.fruit);
		 * int[] fruit = new int[] { R.array.apple, R.array.banana }; int[]
		 * fruit_des = new int[] { R.array.apple_des, R.array.banana_des };
		 * list2 = TreeViewUtils.convertDataForListview(mContext, fruit,
		 * fruit_des);
		 * 
		 * tree_list_adapter2 = new ExpandeAdapter(mContext, groups2, list2);
		 * tree_list2.setAdapter(tree_list_adapter2);
		 * 
		 * tree_btn1.setOnClickListener(new TreeBtnOnClickListener(mContext,
		 * tree_btn1, tree_btn2, tree_list1, tree_list2));
		 * tree_btn2.setOnClickListener(new TreeBtnOnClickListener(mContext,
		 * tree_btn1, tree_btn2, tree_list1, tree_list2));
		 */
	}

	// 解析获取的课程树列表xml信息
	private List<CourseItem> getCourseList(String url, List<NameValuePair> datas) {
		InputStream xmlStream = HttpTool.sendDataByPost(url, datas);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlreader = parser.getXMLReader();
			CourseHandler handler = new CourseHandler();
			xmlreader.setContentHandler(handler);
			InputSource source = new InputSource(xmlStream);
			xmlreader.parse(source);
			List<CourseItem> items = handler.getCourseList();
			return items;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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

		userLogout();
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

		webview.setWebChromeClient(new WebChromeClient());
//		webview.setWebViewClient(new WebViewClient() {
//			// 在当前WebView中加载所有url
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(url);
//				return true;
//			}
//		});
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
	private void showRAFReader(final WebView wb, final String url) {
		showWebView();
		wb.loadUrl("file:///android_asset/sample/RAFReader.html");
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				wb.loadUrl("javascript:setParams('" + url + "')");
			}
		}.start();
	}

	private void showConsole(final WebView wb, final long caseId,
			final long userId) {
		showWebView();
		wb.loadUrl("http://" + clientApp.getServerIP()
				+ getResources().getString(R.string.consoleurl));
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				wb.loadUrl("javascript:setParams('" + userId + ","
						+ getEngineIdByCaseId(caseId) + "')");
			}
		}.start();
	}

	private void showPaper(final WebView wb, final long caseId,
			final long userId) {
		showWebView();
		wb.loadUrl("http://" + clientApp.getServerIP()
				+ getResources().getString(R.string.paperurl));
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				wb.loadUrl("javascript:setParams('" + userId + ","
						+ getEngineIdByCaseId(caseId) + "')");
			}
		}.start();
	}

	private void showFake(final WebView wb, final long userId) {
		showWebView();
		wb.loadUrl("http://" + clientApp.getServerIP()
				+ getResources().getString(R.string.fakeurl));
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				wb.loadUrl("javascript:setParams('" + userId + "')");
			}
		}.start();
	}

	//根据推送的资源信息加载相应的内容
	private void loadRes(PushItem pi) {
		long caseId = pi.getCurrentCaseSource().getCaseId();
		int caseKindId = pi.getCaseItem().getCaseKindId();
		long engineId = pi.getCaseItem().getEngineId();
		int mediaTypeId = pi.getCurrentCaseSource().getMediaTypeId();
		String sourceUrl = pi.getCurrentCaseSource().getSourceUrl();
		String sourceUrlLoad = "http://" + clientApp.getServerIP()
				+ getResources().getString(R.string.server_port) + sourceUrl;
		// 根据caseKindId的类型来调用不同的播放器
		switch (caseKindId) {
		case 10:
			// TODO 课程实验
			break;
		case 20:
			// TODO 课程设计
			break;
		case 30:
			// 单一资源
			break;
		case 40:
			// 游戏引擎
			String caseName = pi.getCaseItem().getCaseName();
			if (caseName.equals("开户申请书")) {
				// TODO
			}
			if (caseName.equals("开户材料")) {
				// TODO
			}
			if (caseName.equals("收配款")) {
				// TODO
			}
			if (caseName.equals("身份验证")) {
				// TODO
			}
			if (caseName.equals("假币识别")) {
				showFake(content_WebView, Long.parseLong(clientApp.getUserId()));
			}
			break;
		case 50:
			// TODO 临时案例
			break;
		case 60:
			// TODO 综合案例
			break;
		case 70:
			// 界面引擎
			break;
		case 80:
			// 考试
			showPaper(content_WebView, caseId, Long.parseLong(clientApp.getUserId()));
			break;
		case 110:
			break;
			/*
			case rafReader:
				showRAFReader(content_WebView, sourceUrlLoad);
				break;
			case console:
				showConsole(content_WebView, caseId, Long.parseLong(clientApp.getUserId()));
				break;
			case paper:
				showPaper(content_WebView, caseId, Long.parseLong(clientApp.getUserId()));
				break;
			case fake:
				showFake(content_WebView, Long.parseLong(clientApp.getUserId()));
				break;
			*/
		}
		// 根据mediaTypeId的类型来加载不同的资源
		switch (mediaTypeId) {
		// TODO
		case 10:
			// avi
			break;
		case 20:
			// wmv
			break;
		case 30:
			// ppt
			break;
		case 40:
			// jpg
			break;
		case 50:
			// doc
			break;
		case 60:
			// flash播放  swf
			break;
		case 70:
			// rar
			break;
		case 80:
			// swf文件  swf
			break;
		default:
			break;
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

	// 设置并显示用户个人信息
	private void setUserInfo() {
		user_name.setText(clientApp.getUserName() + ":"
				+ clientApp.getUserRealName() + ":" + clientApp.getUserId());
		user_avatar.setImageBitmap(clientApp.getUserPortrait());
	}

	// 通过caseId获取engineId
	private long getEngineIdByCaseId(long caseId) {
		long engineId = 0;
		String url = "http://" + clientApp.getServerIP()
				+ getResources().getString(R.string.getEngineIdAction);
		List<NameValuePair> datas = new ArrayList<NameValuePair>();
		datas.add(new BasicNameValuePair("caseId", String.valueOf(caseId)));
		InputStream is = HttpTool.sendDataByPost(url, datas);
		if (is != null) {
			String result = HttpTool.convertStreamToString(is);
			engineId = Long.parseLong(result);
		}
		return engineId;
	}
	
	// 用户登出
	private void userLogout() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				String url = "http://" + clientApp.getServerIP()
						+ getResources().getString(R.string.logoutAction);
				List<NameValuePair> datas = new ArrayList<NameValuePair>();
				datas.add(new BasicNameValuePair("userId", clientApp
						.getUserId()));
				HttpTool.sendDataByPost(url, datas);
			}
		}.start();
	}
}
