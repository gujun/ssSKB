package com.cari.voip.keyboard.soft.dialogs;

import java.util.Date;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.cari.voip.keyboard.soft.image.NodeImage;

public class AboutDialog extends Dialog {
	public AboutDialog(Shell parent){
		super(parent);
	}
	protected  Image getImage(){
		return null;
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("���� ���");
		//newShell.setBackground(ColorConstants.white);
	}
	
	protected void setButtonLayoutData(Button button) {
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		//int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		//Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		//data.widthHint = Math.max(widthHint, minSize.x);
		button.setLayoutData(data);
	}
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, "  ȷ  ��  ",
				true);//IDialogConstants.CANCEL_LABEL
		//createButton(parent, IDialogConstants.CANCEL_ID,
		//		"ȡ��", false);//IDialogConstants.CANCEL_LABEL
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2,false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		applyDialogFont(composite);
		
		Label imageLable = new Label(composite,SWT.NONE);
		//imageLable.setText("        �����ַ��");
		imageLable.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		imageLable.setImage(NodeImage.Aboutus);
		
		Composite rightcomp = new Composite(composite, SWT.NONE);
		GridLayout rlayout = new GridLayout(1,true);
		rightcomp.setLayout(rlayout);
		rightcomp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		
		Label software = new Label(rightcomp,SWT.NONE);
		software.setText("IP�绰����ͨ��ϵͳ����̨ ");
		software.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
		
		Label versionLable = new Label(rightcomp,SWT.NONE);
		//Date d = new Date();
		//versionLable.setText("����汾��1.0.4("+d.toString()+")");
		versionLable.setText("����汾��1.0.6.2");
		versionLable.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
		
		Label author = new Label(rightcomp,SWT.NONE);
		author.setText("������ߣ� �� �� , Email��kin.r.gu@gmail.com");
		author.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
		Label rightsLable = new Label(rightcomp,SWT.NONE);
		
		rightsLable.setText("��أ����ݣ��Զ����ɷ����޹�˾��ͨ���о�����  ��Ȩ���� 2009-2010");
		rightsLable.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		//textLable.setImage(NodeImage.Aboutus);

		Label tmp = new Label(rightcomp,SWT.NONE);
		tmp.setText("��ַ��http://www.cari.com.cn , ��ϵ�绰�� 0519-86998975");
		tmp.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
		
		
		return composite;
		
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
	
}
