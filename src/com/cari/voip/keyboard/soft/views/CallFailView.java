package com.cari.voip.keyboard.soft.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.actions.CallFailActionGroup;
import com.cari.voip.keyboard.soft.adapter.callFailContentProvider;
import com.cari.voip.keyboard.soft.adapter.callFailLabelProvider;
import com.cari.voip.keyboard.soft.dialogs.RegisterDialog;
import com.cari.voip.keyboard.soft.model.cdr;
import com.cari.voip.keyboard.soft.model.cdrFactory;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;
import com.cari.voip.keyboard.soft.model.system.SoftR;
import com.cari.voip.keyboard.soft.model.system.SystemId;
import com.cari.voip.keyboard.stack.CCKPConnection;
import com.cari.voip.keyboard.stack.events.Packet;
import com.cari.voip.keyboard.stack.events.PacketFilter;
import com.cari.voip.keyboard.stack.events.ReplyListener;
import com.cari.voip.keyboard.stack.events.TrapEventListener;

public class CallFailView extends ViewPart implements PropertyChangeListener{

	public static final String ID_VIEW = 
		"com.cari.voip.keyboard.soft.views.CallFailView";
	
	private TrapEventListener listener;
	private ReplyListener replyListener;
	private TableViewer tv;
	private Table tb;
	private SwitchUsersSession session;
	private String sql;
	//private Menu context_menu;
	//private Action callBack;
	private CallFailActionGroup actiongroup;
	private MenuManager menuManager;
	
	private Action checkAll;
	private Timer timer = new Timer();
	private boolean timerCancel = false;
	private TimerTask ttask;
	final Object lock = new Object();
	private boolean isOpening;
	private SoftR sr=null;
	private String mStr=null;
	private String mcode=null;
	private String rcode=null;
	private String msg=null;
	public CallFailView(){
		super();
		isOpening = false;
		this.ttask = new TimerTask(){
			public void run(){
				Activator.getDisplay().asyncExec(new Runnable() {
					 public void run() {
						 Reg(true);
					 }
				});
				
			}
		};
		this.listener = new TrapEventListener(){
			public void processTrap(final Packet event){
				//revealListeners.add(event);
				Activator.getDisplay().asyncExec(new Runnable() {
				      public void run() {
				    	  
				    	  doQuerry();
				      }
				   });
			}
		};
		this.replyListener = new ReplyListener(){

			@Override
			public void processReply(Packet command, final Packet reply) {
				Activator.getDisplay().asyncExec(new Runnable() {
				      public void run() {
				    	  String result = (String) reply.getProperty("reply-text");
				    	 if(result == null || result.startsWith("err")){
				    		 msg = "软件注册不成功，请重新输入注册码";
				    		 Reg(false);
				    	 }
				    	 else{
				    		 sr.saveReg(mStr, mcode, rcode);
				    	 }
				      }
				   });
			}
			
		};
	}
	@Override
	public void createPartControl(Composite parent) {
		timer.scheduleAtFixedRate(ttask, 10000, 10000);
		createTableViewer(parent);
		attachListener();
		 
		
		
	}
	private void createTableViewer(Composite parent) {
		this.tv = new TableViewer(parent,SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		this.tb = this.tv.getTable();
		this.tb.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.tb.setHeaderVisible(true);
		this.tb.setLinesVisible(true);
		TableLayout layout = new TableLayout();
		this.tb.setLayout(layout);
		
		
		layout.addColumnData(new ColumnWeightData(40));
		new TableColumn(this.tb,SWT.NONE).setText("号码");
		
		layout.addColumnData(new ColumnWeightData(30));
		new TableColumn(this.tb,SWT.NONE).setText("名称");
		
		layout.addColumnData(new ColumnWeightData(140));
		new TableColumn(this.tb,SWT.NONE).setText("开始时刻");
		
		layout.addColumnData(new ColumnWeightData(60));
		new TableColumn(this.tb,SWT.NONE).setText("失败类型");
		
		
		this.tv.setLabelProvider(new callFailLabelProvider());
		this.tv.setContentProvider(new callFailContentProvider());
		
		actiongroup  = new CallFailActionGroup(this);
		menuManager  = new MenuManager();
		//final Menu context_menu = 
		actiongroup.fillContextMemu(menuManager);
		menuManager.addMenuListener(new IMenuListener(){
			public void menuAboutToShow(IMenuManager manager){
				IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
				Object o = selection.getFirstElement();
				
				if(o instanceof cdr){
					actiongroup.getCallBackAction().setEnabled(false);
					SwitchUsersSession session = getSession();
					if(session != null){
						actiongroup.getCallBackAction().setEnabled(session.canDail());
					}
					actiongroup.getDeleteAction().setEnabled(true);
					
				}else{
					
					actiongroup.getCallBackAction().setEnabled(false);
					actiongroup.getDeleteAction().setEnabled(false);
				}
			}
		});
		/*context_menu.addMenuListener(new MenuListener(){
			public void menuShown(MenuEvent e){
				IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
				Object o = selection.getFirstElement();
				if(o instanceof cdr){
					for(MenuItem item:context_menu.getItems()){
						item.setEnabled(true);
						item.dispose();
					}
					
				}
				else{
					for(MenuItem item:context_menu.getItems()){
						item.setEnabled(false);
					}
				}
			}
			
			public void menuHidden(MenuEvent e){
				
			}
		});*/
		makeActions();
		contributeToActionBars();
		
		this.sql = cdrFactory.getDispatcherFailQuerryString();
		doQuerry();
	}
	public Table getTable(){
		return this.tb;
	}
	protected SwitchUsersSession getSession(){
		if(this.session == null){
			this.session = Activator.getSwitchUsersSession();
		}
		return this.session;
	}
	protected void refreshTable(List recs){
		this.tv.setInput(recs);
		this.tv.refresh();
		this.tb.setTopIndex(this.tb.getItemCount()-1);
	}
	
	private void makeActions() {
		checkAll = new Action(){
			public void run(){
				try {
					cdrFactory.checkAllDispatcherFail(getSession());
					doQuerry();
				} catch (Exception e) {
					
				}
				
			}
		};
			
		checkAll.setText("全部确认");
		checkAll.setToolTipText("确认所有调度未接来电");
		checkAll.setImageDescriptor(Activator.getImageDescriptor("icons/check_all.gif"));
		
		
	}
	private void contributeToActionBars() {
		
		IActionBars bars = getViewSite().getActionBars();
		
		fillLocalToolBar(bars.getToolBarManager());
	}
	private void fillLocalToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(checkAll);
		
		toolBarManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	protected void doQuerry(){
		if(this.sql == null || this.sql.length() == 0 ){
			//MessageDialog.openInformation(null, "错误", "查询语句为空！");
			return;
			
		}
		
		try {
			List recs = cdrFactory.getCDR(this.getSession(), sql);
			refreshTable(recs);
			
		} catch (Exception e) {
			MessageDialog.openInformation(null, "异常", e.getMessage()+"\n"+sql);
		}
		
	}
	public void onCallBack(){
		IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
		Object o = selection.getFirstElement();
		if(o instanceof cdr){
			cdr rec = (cdr)o;
			try{
				rec.callBack(this.getSession());
				
			}
			catch (Exception e) {
				MessageDialog.openInformation(null, "异常", e.getMessage());
			}
		}
	}
	public void onDeleteClick(){
		IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
		Object o = selection.getFirstElement();
		if(o instanceof cdr){
			cdr rec = (cdr)o;
			try{
				rec.setCheckFlag(this.getSession());
				doQuerry();
			}
			catch (Exception e) {
				MessageDialog.openInformation(null, "异常", e.getMessage());
			}
			
		}
	}

	private void attachListener() {
		SwitchUsersSession session = getSession();
		if(session != null){
			CCKPConnection connection = session.getConnection();
			if(connection != null){
				
				connection.addTrapEventListeners(this.listener, new PacketFilter(){
					public boolean accept(Packet packet){
						boolean ret = false;
						if(packet.getPacketType() == Packet.TYPE_TRAP_CALL_DISPATCHER_FAIL){
							ret = true;
						}
						return ret;
					}
				});

			}
			session.addPropertyChangeListener(SwitchUsersSession.PROP_DISPATCHER_ONLINE,this);
			actiongroup.getCallBackAction().setEnabled(session.canDail());
			
		}
	}
	
	private void detachListener() {
		SwitchUsersSession session = getSession();
		if(session != null){
			CCKPConnection connection = session.getConnection();
			if(connection != null){
				
				connection.removeTrapEventListeners(listener);
			}
			session.removePropertyChangeListener(SwitchUsersSession.PROP_DISPATCHER_ONLINE,this);
			
		}
	}
	
	protected void Reg(boolean firstReg) {
		if(!timerCancel){
			this.timer.cancel();
			timerCancel = true;
		}
		
		
		/*StringBuilder builder = new StringBuilder();
		 builder.append( "cpu:"+SystemId.getCPUID()+"("+String.valueOf(SystemId.getNumberOfProcessors())+")\n");
		 String[] macArray = SystemId.getMACAddresses();
		 for(String mac:macArray){
			 builder.append("Ext:"+mac+"\n");
		 }
		 String[] macArrayJava = SystemId.getMACAddressesJava();
		 for(String mac:macArrayJava){
			 builder.append("Jav:"+mac+"\n");
		 }*/
		if(sr == null){
		try{
		 sr = new SoftR();
		 if(sr.isAreadyRegOK()){
				return;
			}
		}catch(Exception e){
			MessageDialog.openInformation(null, "异常", "缺少组件");
			this.getSite().getWorkbenchWindow().getWorkbench().close();
			return;
		}
		}
		mStr = sr.getMachineString();
		mcode = sr.getMachineCode();
		if(msg == null){
			msg = "软件注册不成功，请重新输入注册码";
		}
		if(rcode == null){
			rcode = sr.getRegCode();
			if(rcode == null){
				msg = "软件还未注册，请注册本软件!";
			}
		}
		if(firstReg && (mcode != null) && (rcode != null)){
			SwitchUsersSession session = this.getSession();
			session.sendMRegister(mcode, rcode.toLowerCase(), replyListener);
			return;
		}
		/*
		boolean openAgain = true;
		while (openAgain) {
			RegisterDialog rgDlg = new RegisterDialog(null, m,msg,r);
			if (rgDlg.open() != Window.OK) {
				try {
					openAgain = false;
					this.getSite().getWorkbenchWindow().getWorkbench().close();
				} catch (Exception e) {

				}
			}
			else{
			r = rgDlg.getRegCode();

			if (sr.checkRegisterCode(m, r)) {
				openAgain = false;
				sr.saveReg(mStr, m, r);
			}
			else{
				msg="注册码输入不正确，请重新输入";
				r = sr.getRegCodeFromMachineCode(m);
			}
			}
		}
		*/
		RegisterDialog rgDlg = new RegisterDialog(null, mcode,msg,rcode);
		if (rgDlg.open() != Window.OK) {
			try {
				//openAgain = false;
				this.getSite().getWorkbenchWindow().getWorkbench().close();
			} catch (Exception e) {

			}
		}else{
			rcode = rgDlg.getRegCode();
			SwitchUsersSession session = this.getSession();
			session.sendMRegister(mcode, rcode.toLowerCase(), replyListener);
			}
		
		
	}
	public void dispose() {
		detachListener();
		 super.dispose();
		
	 }
	@Override
	public void setFocus() {
		if(this.tb != null){
			this.tb.setFocus();
		}

	}
	protected void checkEnable(Object value){
		/*boolean v = true;
		if(value instanceof Boolean){
			Boolean val = (Boolean)value;
			 v = val.booleanValue();
			
			
		}
		else if(value instanceof String){
			String val = (String)value;
			if(val.equals("end") || val.equals("none")){
				v = false;
			}
		}
		actiongroup.getCallBackAction().setEnabled(v);
		*/
		SwitchUsersSession session = getSession();
		if(session != null){
			actiongroup.getCallBackAction().setEnabled(session.canDail());
		}
		
	}
	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		Activator.getDisplay().asyncExec(new Runnable() {
		      public void run() {
		    	  if(evt.getPropertyName().equals(SwitchUsersSession.PROP_DISPATCHER_ONLINE)){
		    		  checkEnable(evt.getNewValue());
		    	  }
		    	 
			}
		   });
	}

}
