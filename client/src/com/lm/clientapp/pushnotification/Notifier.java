package com.lm.clientapp.pushnotification;

import java.util.Random;

import com.lm.clientapp.utils.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

//This class is to notify the user of messages with NotificationManager.
public class Notifier {

	private static final String LOGTAG = "Notifier";
	private static final Random random = new Random(System.currentTimeMillis());
	private Context context;
	private SharedPreferences sharedPrefs;
	private NotificationManager notificationManager;

	public Notifier(Context context) {
		this.context = context;
		this.sharedPrefs = context.getSharedPreferences(
				Constants.APN_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		this.notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void notify(String notificationId, String apiKey, String title,
			String message, String uri) {
		Log.d(LOGTAG, "notify()...");

		Log.d(LOGTAG, "notificationId=" + notificationId);
		Log.d(LOGTAG, "notificationApiKey=" + apiKey);
		Log.d(LOGTAG, "notificationTitle=" + title);
		Log.d(LOGTAG, "notificationMessage=" + message);
		Log.d(LOGTAG, "notificationUri=" + uri);

		if (isNotificationEnabled()) {
			// Show the toast
			if (isNotificationToastEnabled()) {
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}

			// Notification
			Notification notification = new Notification();
			notification.icon = getNotificationIcon();
			notification.defaults = Notification.DEFAULT_LIGHTS;
			if (isNotificationSoundEnabled()) {
				notification.defaults |= Notification.DEFAULT_SOUND;
			}
			if (isNotificationVibrateEnabled()) {
				notification.defaults |= Notification.DEFAULT_VIBRATE;
			}
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.when = System.currentTimeMillis();
			notification.tickerText = message;

			Intent intent = new Intent(context,
					NotificationDetailsActivity.class);
			intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
			intent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
			intent.putExtra(Constants.NOTIFICATION_TITLE, title);
			intent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
			intent.putExtra(Constants.NOTIFICATION_URI, uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);

			// 接收到服务器端推送的信息之后发送广播通知APNReceiver有更新
			Intent intent2 = new Intent(Utils.SEND_RECEIVER_MESSAGE);
			intent2.putExtra(Constants.NOTIFICATION_ID, notificationId);
			intent2.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
			intent2.putExtra(Constants.NOTIFICATION_TITLE, title);
			intent2.putExtra(Constants.NOTIFICATION_MESSAGE, message);
			intent2.putExtra(Constants.NOTIFICATION_URI, uri);
			context.sendBroadcast(intent2);

			notification.setLatestEventInfo(context, title, message,
					contentIntent);
			notificationManager.notify(random.nextInt(), notification);
		} else {
			Log.w(LOGTAG, "Notificaitons disabled.");
		}
	}

	private int getNotificationIcon() {
		return sharedPrefs.getInt(Constants.NOTIFICATION_ICON, 0);
	}

	private boolean isNotificationEnabled() {
		return sharedPrefs.getBoolean(Constants.SETTINGS_NOTIFICATION_ENABLED,
				true);
	}

	private boolean isNotificationSoundEnabled() {
		return sharedPrefs.getBoolean(Constants.SETTINGS_SOUND_ENABLED, true);
	}

	private boolean isNotificationVibrateEnabled() {
		return sharedPrefs
				.getBoolean(Constants.SETTINGS_VIBRATE_ENABLED, false);
	}

	private boolean isNotificationToastEnabled() {
		return sharedPrefs.getBoolean(Constants.SETTINGS_TOAST_ENABLED, false);
	}

}