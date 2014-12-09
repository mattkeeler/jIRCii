package rero.gui;

import rero.client.Capabilities;
import rero.client.DataStructures;
import rero.client.user.UserHandler;
import rero.config.Config;
import rero.gui.dialogs.AboutWindow;
import rero.gui.dialogs.HelpWindow;
import rero.gui.dialogs.OptionWindow;

import javax.swing.*;

public class GlobalCapabilities {
	public static JFrame frame;
	protected SessionManager sessions;

	public GlobalCapabilities(SessionManager _sessions) {
		sessions = _sessions;
	}

	public SessionManager getSessionManager() {
		return sessions;
	}

	public void createNewServer() {
		sessions.addSession();
	}

	public JFrame getFrame() {
		return frame;
	}

	public IRCSession getActiveSession() {
		return sessions.getActiveSession();
	}

	public void showCoolAbout() {
		getActiveSession().createAboutWindow();
	}

	public void setTabTitle(Capabilities c, String text) {
		sessions.setTabTitle(c, text);
	}

	public void showOptionDialog(String defaultItem) {
		OptionWindow.initialize(frame);
		OptionWindow.showDialog(null);
		OptionWindow.displaySpecificDialog(defaultItem);
	}

	public void QuitClient() {
		for (int x = 0; x < sessions.getTabCount(); x++) {
			IRCSession temp = sessions.getSession(sessions.getComponentAt(x));

			temp.getCapabilities().injectEvent("EXIT");

			if (temp.getCapabilities().isConnected())
				((UserHandler) temp.getCapabilities().getDataStructure(DataStructures.UserHandler)).runAlias("QUIT", "");
		}

		Config.getInstance().setBounds("desktop.bounds", frame.getBounds());
		Config.getInstance().sync();

		if (Config.getInstance().isOption("load.lame", false)) {
			System.out.println("\nThis IRC Client was made possible by:\nDr. Lipensteins Penial Enlargement Pump\nStop feeling embarrassed call 1-877-PUMP\n");
		}

		System.exit(0);
	}

	public void showHelpDialog(String defaultItem) {
		HelpWindow.initialize(frame);
		HelpWindow.showDialog(null);
	}

	public void showAboutDialog() {
		AboutWindow.initialize(frame);
		AboutWindow.showDialog(null);
	}
}
