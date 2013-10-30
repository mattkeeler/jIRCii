package rero.gui.windows;

import rero.config.ClientDefaults;
import rero.config.ClientState;
import rero.config.Config;
import rero.config.Resources;
import rero.gui.GlobalCapabilities;
import rero.gui.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {
	public MainWindow() {
		super("jIRCii");

		GlobalCapabilities.frame = this;

		if (Config.getInstance().isOption("ui.showbar", ClientDefaults.ui_showbar)) {
			setJMenuBar(new JMenuBar());
		}

		//getContentPane().add(new ServersTree(), BorderLayout.WEST);

		getContentPane().add(new SessionManager(this), BorderLayout.CENTER);

		setIconImage(Resources.getInstance().getIcon("jirc.icon", "jicon.jpg").getImage());

		int inset = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;

		setBounds(Config.getInstance().getBounds("desktop.bounds", Toolkit.getDefaultToolkit().getScreenSize(), new Dimension(640, 480)));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				try {//exit4shure
					SessionManager.getGlobalCapabilities().QuitClient();
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
			}
		});

		addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent ev) {
				if (Config.getInstance().isOption("desktop.relative", false) ||
						Config.getInstance().isOption("window.relative", false) ||
						Config.getInstance().isOption("statusbar.relative", false)
						) {
					validate();
					ClientState.getInstance().fireChange("desktop");
					ClientState.getInstance().fireChange("window");
					ClientState.getInstance().fireChange("statusbar");
					repaint();
				}
			}
		});
	}
}
