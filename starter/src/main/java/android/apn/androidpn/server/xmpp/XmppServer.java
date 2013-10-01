/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package android.apn.androidpn.server.xmpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import android.apn.androidpn.server.container.AdminConsole;
import android.apn.androidpn.server.util.Config;
import android.apn.androidpn.server.xmpp.session.SessionManager;

/**
 * This class starts the server as a standalone application using Spring
 * configuration.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class XmppServer {

	private static final Log log = LogFactory.getLog(XmppServer.class);

	private static XmppServer instance;

	private ApplicationContext context;

	private String version = "0.5.0";

	private String serverName;

	private String serverHomeDir;

	private boolean shuttingDown;

	/**
	 * Returns the singleton instance of XmppServer.
	 * 
	 * @return the server instance.
	 */
	public static XmppServer getInstance() {
		// return instance;
		if (instance == null) {
			synchronized (XmppServer.class) {
				instance = new XmppServer();
			}
		}
		return instance;
	}

	/**
	 * Constructor. Creates a server and starts it.
	 */
	public XmppServer() {
		if (instance != null) {
			throw new IllegalStateException("A server is already running");
		}
		instance = this;
		start();
	}

	/**
	 * Starts the server using Spring configuration.
	 */
	public void start() {
		try {
			if (isStandAlone()) {
				Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
			}

			locateServer();
			serverName = Config.getString("xmpp.domain", "127.0.0.1")
					.toLowerCase();
			context = new ClassPathXmlApplicationContext("spring-config.xml");
			log.info("Spring Configuration loaded.");

			// web控制台
			AdminConsole adminConsole = new AdminConsole(serverHomeDir);
			adminConsole.startup();
			if (adminConsole.isHttpStarted()) {
				log.info("Admin console listening at http://"
						+ adminConsole.getAdminHost() + ":"
						+ adminConsole.getAdminPort());
			}
			log.info("XmppServer started: " + serverName);
			log.info("Androidpn Server v" + version);

		} catch (Exception e) {
			e.printStackTrace();
			shutdownServer();
		}
	}

	/**
	 * Stops the server.
	 */
	public void stop() {
		shutdownServer();
		Thread shutdownThread = new ShutdownThread();
		shutdownThread.setDaemon(true);
		shutdownThread.start();
	}

	/**
	 * Returns a Spring bean that has the given bean name.
	 * 
	 * @param beanName
	 * @return a Srping bean
	 */
	public Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	/**
	 * Returns the server name.
	 * 
	 * @return the server name
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * Returns true if the server is being shutdown.
	 * 
	 * @return true if the server is being down, false otherwise.
	 */
	public boolean isShuttingDown() {
		return shuttingDown;
	}

	/**
	 * Returns if the server is running in standalone mode.
	 * 
	 * @return true if the server is running in standalone mode, false
	 *         otherwise.
	 */
	public boolean isStandAlone() {
		boolean standalone;
		try {
			standalone = Class
					.forName("android.apn.androidpn.server.starter.ServerStarter") != null;
		} catch (ClassNotFoundException e) {
			standalone = false;
		}
		return standalone;
	}

	private void locateServer() throws FileNotFoundException {
		// String baseDir = System.getProperty("base.dir", "..");
		// String baseDir = "/Volumes/data/my-private/androidpn-server-0.5.0/";
		// log.debug("****base.dir=" + baseDir);

		/*******************/
		File directory = new File("");// 设定为当前文件夹
		File confDir = null;
		try {
			System.out.println(directory.getCanonicalPath());// 获取标准的路径
			System.out.println(directory.getAbsolutePath());// 获取绝对路径
			confDir = new File(directory.getCanonicalPath() + File.separator
					+ "conf");
		} catch (Exception e) {
		}
		/*******************/

		if (serverHomeDir == null) {
			try {
				// File confDir = new File(baseDir, "conf");
				log.debug("****conf.dir=" + confDir);
				if (confDir.exists()) {
					serverHomeDir = confDir.getParentFile().getCanonicalPath();
				}
			} catch (FileNotFoundException fe) {
				// Ignore
			} catch (IOException ie) {
				// Ignore
			}
		}

		if (serverHomeDir == null) {
			System.err.println("Could not locate home.");
			throw new FileNotFoundException();
		} else {
			Config.setProperty("server.home.dir", serverHomeDir);
			log.debug("server.home.dir=" + serverHomeDir);
		}
	}

	private void shutdownServer() {
		shuttingDown = true;
		// Close all connections
		SessionManager.getInstance().closeAllSessions();
		log.info("XmppServer stopped");
	}

	private class ShutdownHookThread extends Thread {
		public void run() {
			shutdownServer();
			log.info("Server halted");
			System.err.println("Server halted");
		}
	}

	private class ShutdownThread extends Thread {
		public void run() {
			try {
				Thread.sleep(5000);
				System.exit(0);
			} catch (InterruptedException e) {
				// Ignore
			}
		}
	}

}