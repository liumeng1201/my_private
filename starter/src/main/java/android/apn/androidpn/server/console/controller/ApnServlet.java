package android.apn.androidpn.server.console.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.apn.androidpn.server.xmpp.push.NotificationManager;

public class ApnServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doGet(req, resp);
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doPost(req, resp);
		String pushinfo = req.getParameter("pushinfo");

		String strApiKey = "1234567890";
		String strTitle = "test";
		String strMessage = pushinfo;
		String strUri = "test";

		NotificationManager nm = new NotificationManager();
		nm.sendBroadcast(strApiKey, strTitle, strMessage, strUri);
	}

}
