package com.cari.voip.keyboard.soft.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cari.voip.keyboard.stack.ConnectionConfiguration;

public class RegisterDialog extends Dialog {
	private String key;
	private String value;
	private String msg;
	private Text msgLabel;
	private Text RegText;
	
	public RegisterDialog(Shell parent,String key){
		this(parent,key,null,null);
	}
	public RegisterDialog(Shell parent,String key,String msg,String value){
		super(parent);
		this.key = key;
		this.msg = msg;
		this.value = value;
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Èí¼þ×¢²á");
	}
	protected void setButtonLayoutData(Button button) {
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		
		button.setLayoutData(data);
	}
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, "  ×¢  ²á  ",
				true);//IDialogConstants.CANCEL_LABEL
		createButton(parent, IDialogConstants.CANCEL_ID,
				"  È¡  Ïû  ", false);//IDialogConstants.CANCEL_LABEL
	}
	protected Control createDialogArea(Composite root) {
		// create a composite with standard margins and spacing
		Composite parent = new Composite(root, SWT.NONE);
		GridLayout toplayout = new GridLayout(1,true);
		toplayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		toplayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		toplayout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		toplayout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		parent.setLayout(toplayout);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		if(this.msg != null){
		Composite info = new Group(parent, SWT.SHADOW_ETCHED_IN);
		GridLayout infolayout = new GridLayout(1,true);
		infolayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		infolayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		infolayout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		infolayout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		info.setLayout(infolayout);
		info.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(info);
		
			this.msgLabel = new Text(info,SWT.READ_ONLY|SWT.CENTER);
			this.msgLabel.setText(this.msg);
			this.msgLabel.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,false,false));
		}
		
		Composite composite = new Composite(parent,SWT.SHADOW_ETCHED_IN);
		GridLayout layout = new GridLayout(2,false);
		layout.marginHeight = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(2*IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(2*IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,false,false));
		applyDialogFont(composite);
		
		
		
		Label keyPre = new Label(composite,SWT.NONE);
		keyPre.setText("»úÆ÷Âë£º");
		keyPre.setLayoutData(new GridData(SWT.END,SWT.CENTER,false,false));
		

		Text keyLabel  = new Text(composite,SWT.READ_ONLY|SWT.SINGLE | SWT.BORDER);
		//keyLabel.setTextLimit(36);
		keyLabel.setText(this.key);
		GridData keyGridData = new GridData(SWT.FILL,SWT.FILL,false,false);
		keyGridData.widthHint = 200;
		keyLabel.setLayoutData(keyGridData);
		
		Label regPre = new Label(composite,SWT.NONE);
		regPre.setText("×¢²áÂë£º");
		regPre.setLayoutData(new GridData(SWT.END,SWT.CENTER,false,false));
		
		this.RegText = new Text(composite,SWT.SINGLE | SWT.BORDER);
		//this.RegText.setTextLimit(36);
		if(this.value != null){
			this.RegText.setText(this.value);
		}
		GridData regGridData = new GridData(SWT.FILL,SWT.FILL,false,false);
		regGridData.widthHint = 200;
		this.RegText.setLayoutData(regGridData);
		
		this.RegText.setFocus();
		
		return parent;
	}
	protected Control createButtonBar(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		// create a layout with spacing and margins appropriate for the font
		// size.
		GridLayout layout = new GridLayout(3,true);
		layout.numColumns = 0; // this is incremented by createButton
		layout.makeColumnsEqualWidth = true;
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		composite.setLayout(layout);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END
				| GridData.VERTICAL_ALIGN_CENTER);
		composite.setLayoutData(data);
		composite.setFont(parent.getFont());
		
		// Add the buttons to the button bar.
		createButtonsForButtonBar(composite);
		return composite;
	}
	protected void okPressed() {
		
		String regCode = this.RegText.getText();
		if(regCode == null || regCode.length() ==0){
			this.RegText.setFocus();
			return;
		}
		
		this.value = regCode;

		super.okPressed();
	}
	
	public String getRegCode(){
		return this.value;
	}
}
