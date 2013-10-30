package rero.dck.items;

import rero.config.Config;
import rero.dck.FileLink;
import rero.dck.SuperInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DirectoryInput extends SuperInput implements ActionListener {
	protected JLabel label;
	protected FileLink text;
	protected String value;
	protected JFileChooser chooser;

	protected boolean directory;

	public DirectoryInput(String _variable, String _value, String _label, char mnemonic, int inset) {
		text = new FileLink();
		label = new JLabel(_label);

		text.addActionListener(this);

		setLayout(new BorderLayout(2, 2));

		add(label, BorderLayout.WEST);
		add(text, BorderLayout.CENTER);

		label.setDisplayedMnemonic(mnemonic);

		variable = _variable;
		value = _value;

		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, inset));
	}

	public void actionPerformed(ActionEvent ev) {
		if (chooser == null) {
			chooser = new JFileChooser();
		}

		chooser.setApproveButtonText("Select Directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (chooser.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {
			text.setText(chooser.getSelectedFile().getAbsolutePath());
		}

		notifyParent();
	}

	public void save() {
		Config.getInstance().setString(getVariable(), text.getText());
	}

	public int getEstimatedWidth() {
		return (int) label.getPreferredSize().getWidth();
	}

	public void setAlignWidth(int width) {
		label.setPreferredSize(new Dimension(width, 0));
		revalidate();
	}

	public JComponent getComponent() {
		return this;
	}

	public void refresh() {
		text.setText(Config.getInstance().getString(getVariable(), value));
	}
}


