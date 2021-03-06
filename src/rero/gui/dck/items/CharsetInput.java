package rero.gui.dck.items;

import rero.config.Config;
import rero.gui.dck.SuperInput;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.Iterator;

public class CharsetInput extends SuperInput {
	public static final String DEFAULT_CHARSET = "Platform Default";

	protected JComboBox name;
	protected boolean listing = true;
	protected JLabel label;

	public CharsetInput(String _variable, String aLabel, char mnemonic, int rightGap) {
		variable = _variable;

		setLayout(new BorderLayout());

		name = new JComboBox();
		name.addItem("Loading Charsets...");

		add(name, BorderLayout.CENTER);

		if (rightGap > 0) {
			JPanel temp = new JPanel();
			temp.setPreferredSize(new Dimension(rightGap, 0));

			add(temp, BorderLayout.EAST);
		}

		label = new JLabel("  " + aLabel + " ");
		label.setDisplayedMnemonic(mnemonic);

		add(label, BorderLayout.WEST);
	}

	public void setAlignWidth(int width) {
		label.setPreferredSize(new Dimension(width, 0));
		revalidate();
	}

	public void save() {
		Config.getInstance().setString(getVariable(), name.getSelectedItem().toString());
	}

	public JComponent getComponent() {
		return this;
	}

	public int getEstimatedWidth() {
		return (int) label.getPreferredSize().getWidth();
	}

	public void refresh() {
		if (!listing) {
			name.setSelectedItem(Config.getInstance().getString(getVariable(), DEFAULT_CHARSET));
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					name.addItem(DEFAULT_CHARSET);

					Iterator i = Charset.availableCharsets().keySet().iterator();

					while (i.hasNext()) {
						name.addItem(i.next().toString());
					}

					name.removeItemAt(0);
					listing = false;
					refresh();
					revalidate();
				}
			});
		}
	}
}


