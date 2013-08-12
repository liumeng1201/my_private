package com.lm.clientapp.pushnotification;

//Static constants for this package.
public class Constants {

	public static final String APN_SHARED_PREFERENCE_NAME = "client_apn_preferences";

	// PREFERENCE KEYS
	public static final String CALLBACK_ACTIVITY_PACKAGE_NAME = "CALLBACK_ACTIVITY_PACKAGE_NAME";
	public static final String CALLBACK_ACTIVITY_CLASS_NAME = "CALLBACK_ACTIVITY_CLASS_NAME";
	public static final String API_KEY = "API_KEY";
	public static final String VERSION = "VERSION";
	public static final String XMPP_HOST = "XMPP_HOST";
	public static final String XMPP_PORT = "XMPP_PORT";
	public static final String XMPP_USERNAME = "XMPP_USERNAME";
	public static final String XMPP_PASSWORD = "XMPP_PASSWORD";

	// public static final String USER_KEY = "USER_KEY";
	public static final String DEVICE_ID = "DEVICE_ID";
	public static final String EMULATOR_DEVICE_ID = "EMULATOR_DEVICE_ID";
	public static final String NOTIFICATION_ICON = "NOTIFICATION_ICON";
	public static final String SETTINGS_NOTIFICATION_ENABLED = "SETTINGS_NOTIFICATION_ENABLED";
	public static final String SETTINGS_SOUND_ENABLED = "SETTINGS_SOUND_ENABLED";
	public static final String SETTINGS_VIBRATE_ENABLED = "SETTINGS_VIBRATE_ENABLED";
	public static final String SETTINGS_TOAST_ENABLED = "SETTINGS_TOAST_ENABLED";

	// NOTIFICATION FIELDS
	public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
	public static final String NOTIFICATION_API_KEY = "NOTIFICATION_API_KEY";
	public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
	public static final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";
	public static final String NOTIFICATION_URI = "NOTIFICATION_URI";

	// INTENT ACTIONS
	public static final String ACTION_SHOW_NOTIFICATION = "com.lm.clientapp.pushnotification.SHOW_NOTIFICATION";
	public static final String ACTION_NOTIFICATION_CLICKED = "com.lm.clientapp.pushnotification.NOTIFICATION_CLICKED";
	public static final String ACTION_NOTIFICATION_CLEARED = "com.lm.clientapp.pushnotification.NOTIFICATION_CLEARED";

}