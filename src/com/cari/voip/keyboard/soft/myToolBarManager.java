package com.cari.voip.keyboard.soft;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.sf.feeling.swt.win32.internal.extension.util.ColorCache;

public class myToolBarManager extends ToolBarManager {
	public myToolBarManager() {
		super();
	}

	/**
	 * Creates a tool bar manager with the given SWT button style. Use the
	 * <code>createControl</code> method to create the tool bar control.
	 * 
	 * @param style
	 *            the tool bar item style
	 * @see org.eclipse.swt.widgets.ToolBar for valid style bits
	 */
	public myToolBarManager(int style) {
		super(style);
	}

	/**
	 * Creates a tool bar manager for an existing tool bar control. This manager
	 * becomes responsible for the control, and will dispose of it when the
	 * manager is disposed.
	 * <strong>NOTE</strong> When creating a ToolBarManager from an existing
	 * {@link ToolBar} you will not get the accessible listener provided by
	 * JFace.
	 * @see #ToolBarManager()
	 * @see #ToolBarManager(int)
	 * 
	 * @param toolbar
	 *            the tool bar control
	 */
	public myToolBarManager(ToolBar toolbar) {
		super(toolbar);
	}
	
	public ToolBar createControl(Composite parent) {
		ToolBar toolBar = super.createControl(parent);
		if(toolBar != null){
			toolBar.setForeground(ColorCache.getInstance().getColor(255, 255, 255));
		}
		return toolBar;
	}
}
