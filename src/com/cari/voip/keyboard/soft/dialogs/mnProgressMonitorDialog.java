package com.cari.voip.keyboard.soft.dialogs;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

public class mnProgressMonitorDialog extends ProgressMonitorDialog {
	private String title;
	public mnProgressMonitorDialog(Shell parent,String t){
		super(parent);
		this.title = t;
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(this.title);
	}
}
