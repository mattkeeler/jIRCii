package rero.gui.toolbar;

import rero.gui.SessionManager;

import java.awt.event.MouseEvent;

public class ScriptAction implements ToolAction {
	public void actionPerformed(MouseEvent ev) {
		SessionManager.getGlobalCapabilities().showOptionDialog("Script Manager");
	}

	public String getDescription() {
		return "Script Manager";
	}

	public int getIndex() {
		return 10;
	}
}
