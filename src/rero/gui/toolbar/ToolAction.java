package rero.gui.toolbar;

import java.awt.event.MouseEvent;

public interface ToolAction {
	public void actionPerformed(MouseEvent ev);

	public String getDescription();

	public int getIndex();
}
