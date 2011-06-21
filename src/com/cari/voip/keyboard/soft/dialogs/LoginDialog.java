package com.cari.voip.keyboard.soft.dialogs;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
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

public class LoginDialog extends Dialog {

	private String msg;
	private ConnectionConfiguration detail;
	private Text msgLabel;
	private Text user;

	private Text host;
	private Text port;
	private Text phoneId;
	private Text pwd;
	private Pattern portPattern;
	
	public LoginDialog(Shell parent){
		this(parent,null);
	}
	public LoginDialog(Shell parent,String msg){
		super(parent);
		this.msg = msg;
		this.detail = null;
	}
	public LoginDialog(Shell parent,String msg,ConnectionConfiguration detail){
		super(parent);
		this.msg = msg;
		this.detail = detail;
		
	}
	public ConnectionConfiguration getConnectionDetails(){
		
		return  this.detail;
	}
	protected  Image getImage(){
		return null;
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("登录界面");
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
		createButton(parent, IDialogConstants.OK_ID, "  登  录  ",
				true);//IDialogConstants.CANCEL_LABEL
		createButton(parent, IDialogConstants.CANCEL_ID,
				"  取  消  ", false);//IDialogConstants.CANCEL_LABEL
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
		Composite info = new Group(parent,  SWT.SHADOW_ETCHED_IN);
		GridLayout infolayout = new GridLayout(1,true);
		infolayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		infolayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		infolayout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		infolayout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		info.setLayout(infolayout);
		info.setLayoutData(new GridData(GridData.FILL_BOTH));
	    //info.setBackground(new org.eclipse.swt.graphics.Color(root.getDisplay(),new RGB(255,0,255)));
		applyDialogFont(info);
		
			this.msgLabel = new Text(info,SWT.READ_ONLY|SWT.CENTER);
			this.msgLabel.setText(this.msg+"，请重新登录");
			this.msgLabel.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,false,false));
		}
		/*else{
			
			Label top  = new Label(parent,SWT.NONE);
			top.setText("                        ");
		
		}*/
		Group header = new Group(parent,  SWT.SHADOW_ETCHED_IN);
		header.setText("用户界面名称");
		GridLayout headerlayout = new GridLayout(1,true);
		headerlayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		headerlayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		headerlayout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		headerlayout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		header.setLayout(headerlayout);
		header.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		this.user = new Text(header,SWT.SINGLE | SWT.BORDER);
		//this.user.setTextLimit(32);
		if(this.detail != null)
			this.user.setText(this.detail.getUser());
		GridData userGridData = new GridData(SWT.FILL,SWT.FILL,true,true);
		userGridData.widthHint = 240;
		this.user.setLayoutData(userGridData);
		
		Group composite = new Group(parent, SWT.SHADOW_ETCHED_IN);
		composite.setText("登录IP调度主机");
		
		GridLayout layout = new GridLayout(2,false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,false,false));
		applyDialogFont(composite);
		

		/*Label ll = new Label(composite,SWT.NONE);
		ll.setText("                   ");
		ll.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		
		Label lr = new Label(composite,SWT.NONE);
		lr.setText("               ");
		lr.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		
		*/
		
		Label hostLable = new Label(composite,SWT.NONE);
		hostLable.setText("网络地址：                ");
		///hostLable.setSize(120,30);
		hostLable.setLayoutData(new GridData(SWT.BEGINNING,SWT.FILL,false,false));
		
		Label portLable = new Label(composite,SWT.NONE);
		portLable.setText("端口号：      ");
		//portLable.setSize(80,30);
		portLable.setLayoutData(new GridData(SWT.BEGINNING,SWT.FILL,false,false));
		
		
		this.host = new Text(composite,SWT.SINGLE | SWT.BORDER);
		//this.host.setTextLimit(32);
		if(this.detail != null)
			this.host.setText(this.detail.getServerHost());
		GridData hostGridData = new GridData(SWT.FILL,SWT.FILL,false,false);
		hostGridData.widthHint = 160;
		this.host.setLayoutData(hostGridData);
		
		
		
		this.port = new Text(composite,SWT.SINGLE | SWT.BORDER);
		portPattern = Pattern.compile("[1-9]\\d*");
		
		this.port.addVerifyListener(new VerifyListener(){

			@Override
			public void verifyText(VerifyEvent e) {
				
				
				if (e.text.length() > 0){
					
					
						if(e.widget instanceof Text){
							Text t = (Text)e.widget;
							String nStr = t.getText();
							if(nStr == null || nStr.length() ==0){
								nStr = e.text;
							}else{
								nStr = nStr.concat(e.text);
							}
							Matcher match = portPattern.matcher(nStr);
							if(match.matches()){
								try{
			                        //Double.parseDouble(e.text);
			                    	int n = Integer.parseInt(nStr);
			                    	if(n > 65535){
			                    		 e.doit = false;
			                    	}
			                    	else{
			                    		 e.doit = true;
			                    	}
			                       
			                    }catch(Exception ep){
			                        e.doit = false;
			                    }
							}
							else{
								e.doit = false;
							}
								
							
						
					}
					else{
						e.doit = false;
					}
                    
                }
				
			}
			
		});
		//this.port.setTextLimit(6);
		if(this.detail != null){
			this.port.setText(String.valueOf(this.detail.getServerTcpPort()));
		}
		else{
			this.port.setText("6608");
		}
		
		//this.port.sett
		GridData portGridData = new GridData(SWT.FILL,SWT.FILL,false,false);
		portGridData.widthHint = 80;
		this.port.setLayoutData(portGridData);
		
		
		Label userLable = new Label(composite,SWT.NONE);
		userLable.setText("调度电话：");
		userLable.setLayoutData(new GridData(SWT.BEGINNING,SWT.FILL,false,false));
		
		Label pwdLable = new Label(composite,SWT.NONE);
		pwdLable.setText("口令：");
		pwdLable.setLayoutData(new GridData(SWT.BEGINNING,SWT.FILL,false,false));
		
		
		this.phoneId = new Text(composite,SWT.SINGLE | SWT.BORDER);
		//this.user.setTextLimit(32);
		if(this.detail != null)
		this.phoneId.setText(this.detail.getPhoneId());
		GridData phoneIdGridData = new GridData(SWT.FILL,SWT.FILL,true,true);
		phoneIdGridData.widthHint = 160;
		this.phoneId.setLayoutData(phoneIdGridData);
		//phoneId
		
		this.pwd = new Text(composite,SWT.PASSWORD|SWT.SINGLE | SWT.BORDER);
		//this.pwd.setTextLimit(32);
		if(this.detail != null)
		this.pwd.setText(String.valueOf(this.detail.getPwd()));
		GridData pwdGridData = new GridData(SWT.FILL,SWT.FILL,false,false);
		pwdGridData.widthHint = 80;
		this.pwd.setLayoutData(pwdGridData);
		//this.pwd.setSize(100, 30);
		
		if(this.host.getText()==null || this.host.getText().length()==0){
			this.host.setFocus();
		}
		else if(this.port.getText()==null || this.port.getText().length()==0){
			this.port.setFocus();
		}
		else if(this.user.getText()==null || this.user.getText().length()==0){
			this.user.setFocus();
		}
		else if(this.pwd.getText()==null || this.pwd.getText().length()==0){
			this.pwd.setFocus();
		}
		
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
		String userName = this.user.getText();
		if(userName == null || userName.length() ==0){
			this.user.setFocus();
			return;
		}
		
		String serverHost = this.host.getText();
		if(serverHost == null || serverHost.length() ==0){
			this.host.setFocus();
			return;
		}
		String serverPort = this.port.getText();
		if(serverPort == null || serverPort.length() ==0){
			this.port.setFocus();
			return;
		}
		String phone = this.phoneId.getText();
		if(phone == null || phone.length() ==0){
			this.phoneId.setFocus();
			return;
		}
		
		String passwd = this.pwd.getText();
		
		if(this.detail == null){
			this.detail = new ConnectionConfiguration();
		}
		this.detail.setServerHost(serverHost);

		this.detail.setServerTcpPort(serverPort);
		
		this.detail.setUser(userName);
		this.detail.setPhoneId(phone);
		this.detail.setPwd(passwd);
		/*setReturnCode(OK);
		close();*/
		super.okPressed();
	}
	
}
