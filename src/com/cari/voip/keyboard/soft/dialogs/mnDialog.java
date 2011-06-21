package com.cari.voip.keyboard.soft.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class mnDialog extends ProgressMonitorDialog {

	public mnDialog(Shell parent){
		super(parent);
		this.message = "  ";
	}
	protected  Image getImage(){
		return null;
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("µÇÂ¼½ø³Ì");
	}
	protected Control createDialogArea(Composite parent) {
		//setMessage(DEFAULT_TASKNAME, false);
		this.message = null;
		createMessageArea(parent);
		// Only set for backwards compatibility
		taskLabel = messageLabel;
		// progress indicator
		progressIndicator = new ProgressIndicator(parent);
		GridData gd = new GridData();
		gd.heightHint = convertVerticalDLUsToPixels(9);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		progressIndicator.setLayoutData(gd);
		// label showing current task
		subTaskLabel = new Label(parent, SWT.LEFT | SWT.WRAP);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = convertVerticalDLUsToPixels(21);
		gd.horizontalSpan = 2;
		subTaskLabel.setLayoutData(gd);
		subTaskLabel.setFont(parent.getFont());
		return parent;
	}
	
	protected Control createButtonBar(Composite parent) {
		/*Composite composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(0) // this is incremented
				// by createButton
				.equalWidth(true).applyTo(composite);

		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).span(2, 1)
				.applyTo(composite);
		composite.setFont(parent.getFont());
		// Add the buttons to the button bar.
		createButtonsForButtonBar(composite);*/
		return null;
	}
}
