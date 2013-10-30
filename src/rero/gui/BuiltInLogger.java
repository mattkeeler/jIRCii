package rero.gui;

import rero.config.ClientDefaults;
import rero.config.ClientState;
import rero.config.ClientStateListener;
import rero.net.SocketInformation;
import rero.util.ClientUtils;
import rero.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

public class BuiltInLogger {
	protected static boolean isEnabled = ClientState.getInstance().isOption("log.enabled", ClientDefaults.log_enabled);
	protected static boolean timeStamp = ClientState.getInstance().isOption("log.timestamp", ClientDefaults.log_timestamp);
	protected static boolean stripColors = ClientState.getInstance().isOption("log.strip", ClientDefaults.log_strip);
	protected static HashMap logHandles = new HashMap();

	protected static ClientStateListener listener = null;

	protected SocketInformation socket = null;
	protected IRCSession client = null;

	public BuiltInLogger(IRCSession _client) {
		client = _client;

		if (listener == null) {
			listener = new LoggerPropListener();

			ClientState.getInstance().addClientStateListener("log.enabled", listener);
			ClientState.getInstance().addClientStateListener("log.timestamp", listener);
			ClientState.getInstance().addClientStateListener("log.strip", listener);
			ClientState.getInstance().addClientStateListener("log.saveto", listener);
		}
	}

	public void logMessage(String window, String text) {
		try {
			if (socket == null && client.getCapabilities() != null && client.getCapabilities().getSocketConnection() != null)
				socket = client.getCapabilities().getSocketConnection().getSocketInformation();

			PrintWriter writer = getFileHandle(socket, window);

			if (writer != null) {
				if (stripColors)
					text = StringUtils.strip(text);

				if (timeStamp)
					text = ClientUtils.TimeDateStamp(ClientUtils.ctime()) + " " + text;

//           synchronized (writer)
//           {
				writer.println(text);
//           }
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	private static class LoggerPropListener implements ClientStateListener {
		public void propertyChanged(String prop, String parm) {
			if (prop.equals("log.enabled"))
				isEnabled = ClientState.getInstance().isOption("log.enabled", ClientDefaults.log_enabled);

			if (prop.equals("log.saveto")) {
				Iterator i = logHandles.values().iterator();
				while (i.hasNext()) {
					PrintWriter temp = (PrintWriter) i.next();
					try {
						temp.flush();
						temp.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				logHandles.clear();
			}

			timeStamp = ClientState.getInstance().isOption("log.timestamp", ClientDefaults.log_timestamp);
			stripColors = ClientState.getInstance().isOption("log.strip", ClientDefaults.log_strip);
		}
	}

	public static String getLogFileName(SocketInformation socket, String window) {
		String server = "Unknown";

		if (socket != null && socket.network != null && socket.network.length() > 0)
			server = socket.network;

		if (window.length() == 0)
			window = rero.gui.windows.StatusWindow.STATUS_NAME;

		if (window.charAt(0) == '=') {
			server = "dcc_chat";
			window = window.substring(1, window.length());
		}

		File logDir = new File(ClientState.getInstance().getString("log.saveto", ClientDefaults.log_saveto));
		logDir = new File(logDir, server);

		String filename = window.replaceAll("[^\\w\\#\\!\\$\\(\\)\\@\\^\\`\\{\\}\\']", "_"); // replace all non-word characters in the window name with the _ character...

		File output = new File(logDir, filename + ".log");
		return output.getAbsolutePath();
	}

	private PrintWriter getFileHandle(SocketInformation socket, String window) {
		String server = "Unknown";

		if (socket != null && socket.network != null && socket.network.length() > 0)
			server = socket.network;

		if (window.length() == 0)
			window = rero.gui.windows.StatusWindow.STATUS_NAME;

		if (window.charAt(0) == '=') {
			server = "dcc_chat";
			window = window.substring(1, window.length());
		}

		if (logHandles.containsKey(server + window))
			return (PrintWriter) logHandles.get(server + window);

		try {
			File logDir = new File(ClientState.getInstance().getString("log.saveto", ClientDefaults.log_saveto));
			logDir = new File(logDir, server);

			if (!logDir.exists())
				logDir.mkdirs();

			String filename = window.replaceAll("[^\\w\\#\\!\\$\\(\\)\\@\\^\\`\\{\\}\\']", "_"); // replace all non-word characters in the window name with the _ character...

			File output = new File(logDir, filename + ".log");

			PrintWriter rv = new PrintWriter(new FileOutputStream(output, true), true);

			logHandles.put(server + window, rv);

			return rv;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
