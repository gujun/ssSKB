package com.cari.voip.keyboard.soft.views;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.actions.recordingActionGroup;
import com.cari.voip.keyboard.soft.adapter.recordingsContentProvider;
import com.cari.voip.keyboard.soft.adapter.recordingsLabelProvider;
import com.cari.voip.keyboard.soft.adapter.switchUsers.SwitchUsersAdapterFactory;
import com.cari.voip.keyboard.soft.controller.localSave;
import com.cari.voip.keyboard.soft.dialogs.mnProgressMonitorDialog;
import com.cari.voip.keyboard.soft.image.MenuImage;
import com.cari.voip.keyboard.soft.model.recording;
import com.cari.voip.keyboard.soft.model.recordingsFactory;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersManager;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;

public class VRView extends ViewPart {
	public static final String ID_VIEW = 
		"com.cari.voip.keyboard.soft.views.VRView";
	
	private Browser browser;
	private IStatusLineManager statusLine;
	private Display display;
	private Shell shell;
	private Shell popupShell;
	private DateTime cal;
	
	private Shell userShell;
	private TreeViewer treeViewer;
	//private IAdapterFactory adapterFactory = new SwitchUsersTreeAdapterFactory();
	private IAdapterFactory adapterFactory = new SwitchUsersAdapterFactory();
	private SwitchUsersSession session;
	
	private Composite startArea;
	private DateTime startDate;
	private DateTime startTime;
	
	private Composite endArea;
	private DateTime endDate;
	private DateTime endTime;
	
	private DateTime selectDateObj;
	
	private Composite userArea;
	private Text userId;
	private Button exportXls;
	private Button deldeled;
	
	private TableViewer tv;
	private Table tb;
	
	private String start_time;
	private String end_time;
	private String user_id;
	
	private String sql;
	private String lastUrl;
	private Menu context_menu;
	
	private Shell tipShell;
	private Composite tipcomp;
	private Label tipLabel;
	
	public VRView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		this.display = Activator.getDisplay();
		this.shell = parent.getShell();
		this.popupShell = new Shell(display,SWT.ON_TOP);
		this.popupShell.setLayout(new FillLayout());
		this.cal = new DateTime(popupShell,SWT.CALENDAR);
		this.cal.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent event){
				refreshDate();
				popupShell.setVisible(false);
			}
			
			public void mouseDown(MouseEvent e){
				
			}
			
			public void mouseUp(MouseEvent e){
				
			}

		});
		this.cal.addFocusListener(new FocusListener (){
			public void focusGained(FocusEvent e){
				
			}

			
			public void focusLost(FocusEvent e){
				if(popupShell.isVisible()){
					popupShell.setVisible(false);
					
				}
			}
		});
		this.popupShell.pack();
		
		this.userShell = new Shell(display,SWT.ON_TOP);
		this.userShell.setLayout(new FillLayout());
		this.treeViewer = new TreeViewer(this.userShell,SWT.BORDER|SWT.MULTI|SWT.V_SCROLL);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUsersManager.class);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUsersGroup.class);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUser.class);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUsersSession.class);
		this.session = Activator.getSwitchUsersSession();
		getSite().setSelectionProvider(this.treeViewer);
		this.treeViewer.setLabelProvider(new WorkbenchLabelProvider());
		this.treeViewer.setContentProvider(new BaseWorkbenchContentProvider());
		//this.treeViewer.setInput(this.session.getTreeRoot());
		this.treeViewer.setInput(this.session.getlocalUserManager());
		this.treeViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event){
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection){
					Object obj = ((IStructuredSelection)selection).getFirstElement();
					//activeEditorFromModel(obj);
					if(obj instanceof SwitchUser){
						if(userId != null){
							userId.setText(((SwitchUser)obj).getUserId());
							userShell.setVisible(false);
						}
					}
					
					
					
					
				}//if(selection instanceof IStructuredSelection){
			}//public void doubleClick(DoubleClickEvent event){
		});
		this.treeViewer.getControl().addFocusListener(new FocusListener (){
			public void focusGained(FocusEvent e){
				
			}

			
			public void focusLost(FocusEvent e){
				if(userShell.isVisible()){
					
					userShell.setVisible(false);
					
				}
			}
		});
		this.userShell.pack();
		
		this.tipShell = new Shell(display,SWT.ON_TOP);
		this.tipShell.setLayout(new FillLayout());
		this.tipcomp= new Composite(tipShell, SWT.NONE);
		tipcomp.setLayout(new FillLayout());
		//Label LoadingImage = new  Label(tipcomp,SWT.NONE);
		//LoadingImage.setImage(NodeImage.LoadingImage);
		
		this.tipLabel = new Label(tipcomp,SWT.NONE);
		
		this.tipLabel.setText("正在查询，请稍候...");
		tipcomp.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(tipShell.isVisible()){
					tipShell.setVisible(false);
					
				}
			}
			
		});
		this.tipShell.pack();
		Composite comp= new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1,true));
		Composite querryBoard = new Composite(comp,SWT.NONE);
		querryBoard.setLayout(new RowLayout());
		
		this.startArea = new Composite(querryBoard,SWT.NONE);
		this.startArea.setLayout(new GridLayout(3,false));
		
		Label startLable = new Label(startArea,SWT.NONE);
		startLable.setText("开始时刻：");
		this.startDate = new DateTime(startArea,SWT.DATE); 
		this.startTime = new DateTime(startArea,SWT.TIME); 
		
		
		this.startDate.setToolTipText("双击弹出日历");
		this.startDate.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent event){
				selectDateObj = startDate;
				cal.setYear(startDate.getYear());
				cal.setMonth(startDate.getMonth());
				cal.setDay(startDate.getDay());
				Rectangle dateRect= display.map(startArea,null,startDate.getBounds());
				Rectangle calRect= popupShell.getBounds();
				popupShell.setBounds(dateRect.x, dateRect.y+dateRect.height, calRect.width, calRect.height);
				popupShell.setVisible(true);
				cal.setFocus();
				//cal.setVisible(true);
			}
			
			public void mouseDown(MouseEvent e){
				
			}
			
			public void mouseUp(MouseEvent e){
				
			}

		});
		
		this.endArea = new Composite(querryBoard,SWT.NONE);
		this.endArea.setLayout(new GridLayout(3,false));
		
		Label endLable = new Label(endArea,SWT.NONE);
		endLable.setText("结束时刻：");
		this.endDate = new DateTime(endArea,SWT.DATE); 
		this.endTime = new DateTime(endArea,SWT.TIME); 
		this.endDate.setToolTipText("双击弹出日历");
		
		this.endDate.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent event){
				selectDateObj = endDate;
				cal.setYear(endDate.getYear());
				cal.setMonth(endDate.getMonth());
				cal.setDay(endDate.getDay());
				Rectangle dateRect= display.map(endArea,null,endDate.getBounds());
				Rectangle calRect= popupShell.getBounds();
				popupShell.setBounds(dateRect.x, dateRect.y+dateRect.height, calRect.width, calRect.height);
				popupShell.setVisible(true);
				cal.setFocus();
				//cal.setVisible(true);
			}
			
			public void mouseDown(MouseEvent e){
				
			}
			
			public void mouseUp(MouseEvent e){
				
			}

		});
		
		
		
		
		this.userArea = new Composite(querryBoard,SWT.NONE);
		this.userArea.setLayout(new GridLayout(2,false));
		
		Label userLable = new Label(userArea,SWT.NONE);
		userLable.setText("用户：");
		
		this.userId = new Text(userArea,SWT.SINGLE | SWT.BORDER);
		this.userId.setToolTipText("为空表示全部用户,双击弹出用户列表");
		this.userId.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent event){
				treeViewer.refresh();
				Rectangle dateRect= display.map(userArea,null,userId.getBounds());
				Rectangle calRect= userShell.getBounds();
				userShell.setBounds(dateRect.x, dateRect.y+dateRect.height, calRect.width, calRect.height<100?100:calRect.height);
				userShell.setVisible(true);
				treeViewer.getControl().setFocus();
			}
			
			public void mouseDown(MouseEvent e){
				
			}
			
			public void mouseUp(MouseEvent e){
				
			}

		});
		
		Button qerry = new Button(querryBoard,SWT.NONE);
		qerry.setText("查询录音记录");
		qerry.setImage(MenuImage.querry);
		qerry.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent event){
				onQuerryClick();
			}
			
			public void mouseDown(MouseEvent e){
				
			}
			
			public void mouseUp(MouseEvent e){
				onQuerryClick();
			}

		});
		
		
		
		exportXls = new Button(querryBoard,SWT.NONE);
		exportXls.setText("XLS导出录音记录");
		exportXls.setImage(MenuImage.export);
		exportXls.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent event){
				onSaveAsXLS();
			}
			
			public void mouseDown(MouseEvent e){
				
			}
			
			public void mouseUp(MouseEvent e){
				onSaveAsXLS();
			}

		});
		exportXls.setEnabled(false);
		
		
		deldeled = new Button(querryBoard,SWT.NONE);
		deldeled.setText("清除下述录音已删记录");
		deldeled.setImage(MenuImage.cut);
		deldeled.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent event){
				onDeldeledClick();
			}
			
			public void mouseDown(MouseEvent e){
				
			}
			
			public void mouseUp(MouseEvent e){
				onDeldeledClick();
			}

		});
		deldeled.setEnabled(false);
		
		/*Button delete = new Button(querryBoard,SWT.NONE);
		delete.setText("删除录音记录");
		
		Button deleteFile = new Button(querryBoard,SWT.NONE);
		deleteFile.setText("删除录音文件");*/
		
		try{
			browser = new Browser(comp, SWT.NONE);
		}catch(SWTError e){
			return;
		}
		this.browser.setBounds(0, 0, 1, 1);
		this.browser.setJavascriptEnabled(true);
		this.browser.setVisible(true);
		
		this.browser.addLocationListener(new LocationListener(){
			public void changing(LocationEvent event){
				
			}
			public void changed(LocationEvent event){
				lastUrl = event.location;
			}
		});
		//this.browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		/*this.browser.addProgressListener(new ProgressListener(){
			public void changed(ProgressEvent event){
				onProgress(event);
			}
			
			public void completed(ProgressEvent event){
				
			}
		});*/
	
		//下载、试听为 context menu
		this.tv = new TableViewer(comp,SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		this.tb = this.tv.getTable();
		this.tb.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.tb.setHeaderVisible(true);
		this.tb.setLinesVisible(true);
		TableLayout layout = new TableLayout();
		this.tb.setLayout(layout);
		
		
		layout.addColumnData(new ColumnWeightData(30));
		new TableColumn(this.tb,SWT.NONE).setText("号码");
		
		layout.addColumnData(new ColumnWeightData(30));
		new TableColumn(this.tb,SWT.NONE).setText("名称");
		
		layout.addColumnData(new ColumnWeightData(30));
		new TableColumn(this.tb,SWT.NONE).setText("主/被");
		
		layout.addColumnData(new ColumnWeightData(60));
		new TableColumn(this.tb,SWT.NONE).setText("开始时刻");
		
		layout.addColumnData(new ColumnWeightData(20));
		new TableColumn(this.tb,SWT.NONE).setText("录音类型");
		
		layout.addColumnData(new ColumnWeightData(30));
		new TableColumn(this.tb,SWT.NONE).setText("录音时长(s)");
		
		layout.addColumnData(new ColumnWeightData(30));
		new TableColumn(this.tb,SWT.NONE).setText("标识");
		
		this.tv.setLabelProvider(new recordingsLabelProvider());
		this.tv.setContentProvider(new recordingsContentProvider());
		this.tv.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event){
				onRecordingDoubleClick(event);
			}
		});
		
		recordingActionGroup actiongroup = new recordingActionGroup(this);
		context_menu= actiongroup.fillContextMemu(new MenuManager());
		context_menu.addMenuListener(new MenuListener(){
			public void menuShown(MenuEvent e){
				IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
				Object o = selection.getFirstElement();
				if(o instanceof recording){
					for(MenuItem item:context_menu.getItems()){
						item.setEnabled(true);
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
		});

		
		
		
	}
	protected SwitchUsersSession getSession(){
		if(this.session == null){
			this.session = Activator.getSwitchUsersSession();
		}
		return this.session;
	}
	protected void onRecordingDoubleClick(DoubleClickEvent event){
		/*IStructuredSelection selection = (IStructuredSelection)event.getSelection();
		Object o = selection.getFirstElement();
		if(o instanceof recording){
			recording rec = (recording)o;
			String tip = "号码："+rec.caller_id_number+"\n"+
						"名称："+rec.caller_id_name+"\n"+
						"文件："+rec.file_path;
			MessageDialog.openInformation(null, "提示", tip);
		}*/
	}
	public Table getTable(){
		return this.tb;
	}
	public void onTryClick(){
		IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
		Object o = selection.getFirstElement();
		if(o instanceof recording){
			recording rec = (recording)o;
			try{
				String url = rec.getTryUrl(this.getSession());
				if(this.lastUrl != null && this.lastUrl.equals(url)){
					this.browser.refresh();
				}
				else{
					this.browser.setUrl(url);
					
				}
			}
			catch (Exception e) {
				MessageDialog.openInformation(null, "异常", e.getMessage());
			}
			
		}
	}
	public void onDeleteClick(){
		IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
		Object o = selection.getFirstElement();
		if(o instanceof recording){
			recording rec = (recording)o;
			String comfirmId = rec.caller_id_number;;
			if(rec.call_name != null && !rec.call_name.equals(rec.caller_id_number)){
				comfirmId = rec.call_name;
			} 
			
			if(MessageDialog.openConfirm(this.shell, "确认", "删除 "+ comfirmId +
					" 在 "+rec.start_stamp +" 时刻的录音？")){
			try{
				String url = rec.getDeleteUrl(this.getSession());
				if(this.lastUrl != null && this.lastUrl.equals(url)){
					this.browser.refresh();
				}
				else{
					this.browser.setUrl(url);
					
				}
				rec.setDeletedFalg(this.getSession());
				doQuerry();
			}
			catch (Exception e) {
				MessageDialog.openInformation(null, "异常", e.getMessage());
			}
			}
		}
	}
	public void onDownloadClick(){
		//MessageDialog.openInformation(null, "提示", "downlaodclick！");
		IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
		Object o = selection.getFirstElement();
		if(o instanceof recording){
			recording rec = (recording)o;
			try{
				String url = rec.getDownloadUrl(this.getSession());
				if(this.lastUrl != null && this.lastUrl.equals(url)){
					this.browser.refresh();
				}
				else{
					this.browser.setUrl(url);
					
				}
			}
			catch (Exception e) {
				MessageDialog.openInformation(null, "异常", e.getMessage());
			}
		}
	}
	protected void refreshTable(List recs){
		this.tv.setInput(recs);
		this.tv.refresh();
	}
	protected void doQuerry(){
		if(this.sql == null || this.sql.length() == 0 ){
			MessageDialog.openInformation(null, "错误", "查询语句为空！");
			return;
			
		}
		this.tipLabel.setText("正在查询，请稍候...");
		this.tipShell.pack();
		Rectangle tbRect= display.map(tb.getParent(),null,tb.getBounds());
		Rectangle tipRect= tipShell.getBounds();
		this.tipShell.setBounds(tbRect.x+(tbRect.width-tipRect.width)/2, tbRect.y+(tbRect.height-tipRect.height)/2, 
				tipRect.width, tipRect.height);
		this.tipShell.setVisible(true);
		this.tipcomp.setFocus();
		try {
			List recs = recordingsFactory.getRecordings(this.getSession(), sql);
			tipShell.setVisible(false);
			refreshTable(recs);
			if(recs.size() > 0){
				this.exportXls.setEnabled(true);
				this.deldeled.setEnabled(true);
				this.context_menu.setEnabled(true);
				this.tb.setToolTipText("选择某记录,右键选择 试听、下载 和 删除 其录音文件");
				
			} else {
				this.exportXls.setEnabled(false);
				this.deldeled.setEnabled(false);
				this.context_menu.setEnabled(false);
				this.tb.setToolTipText("");
				//MessageDialog.openInformation(null, "提示", "查询结果为空！");
				this.tipLabel.setText("查询结果为空！");
				this.tipShell.pack();
				tipRect = tipShell.getBounds();
				this.tipShell.setBounds(tbRect.x+(tbRect.width-tipRect.width)/2, tbRect.y+(tbRect.height-tipRect.height)/2, 
						tipRect.width, tipRect.height);
				this.tipShell.setVisible(true);
				this.tipcomp.setFocus();
			}
		} catch (Exception e) {
			MessageDialog.openInformation(null, "异常", e.getMessage()+"\n"+sql);
		}
		
	}
	
	protected String getStartTime(){
		int y = this.startDate.getYear();
		int mo = this.startDate.getMonth();
		int d = this.startDate.getDay();
		
		int h = this.startTime.getHours();
		int mi = this.startTime.getMinutes();
		int s = this.startTime.getSeconds();
		
		String start_time = String.valueOf(y)+"-"+
							String.valueOf(mo+1)+"-"+
							String.valueOf(d)+" "+
								String.valueOf(h)+":"+
								String.valueOf(mi)+":"+
								String.valueOf(s);
		return start_time;
	}
	protected String getEndTime(){
		int y = this.endDate.getYear();
		int mo = this.endDate.getMonth();
		int d = this.endDate.getDay();
		int h = this.endTime.getHours();
		int mi = this.endTime.getMinutes();
		int s = this.endTime.getSeconds();
		
		String end_time = String.valueOf(y)+"-"+
		String.valueOf(mo+1)+"-"+
		String.valueOf(d)+" "+
			String.valueOf(h)+":"+
			String.valueOf(mi)+":"+
			String.valueOf(s);
		
		return end_time;
	}
	protected String getUserId(){
		return this.userId.getText();
	}
	protected void onQuerryClick(){

		this.start_time =this.getStartTime();
		
		this.end_time = this.getEndTime();
		
		this.user_id = this.getUserId();
		
		this.sql = recordingsFactory.getQuerryString(this.start_time,
				this.end_time, this.user_id);
		
		doQuerry();
		
	}
	
	protected void onDeldeledClick(){
		if(MessageDialog.openConfirm(this.shell, "确认", "清除所列记录中录音文件已经被删除的记录？")){
		try {
			
			recordingsFactory.deldeled(this.getSession(), 
					recordingsFactory.getDeldeledString(this.start_time, this.end_time, this.user_id));
			doQuerry();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		}
	}
	protected void onSaveAsXLS(){
		
		//MessageDialog.openInformation(null, "保存", "保存");
		FileDialog fileDialog = new FileDialog(this.shell,SWT.SAVE);
		fileDialog.setFilterExtensions(new String[]{"*.xls","*.XLS"});
		
		final String filename =  fileDialog.open();
		if(filename != null && filename.length() > 0){
			try {
				mnProgressMonitorDialog progress = new mnProgressMonitorDialog(this.shell,"导出进程");
				progress.setCancelable(false);
			    progress.run(false, false, new IRunnableWithProgress() {
			      public void run(IProgressMonitor monitor) throws InvocationTargetException{
			       
			          try {
						localSave.asXLS(getTable(), filename,monitor);
					} catch (Exception e) {
						
						throw new InvocationTargetException(e, e.getMessage());
					}
			        
			      }
			    });
			  
				
				//MessageDialog.openInformation(null, "提示",  "导出成功！");
			} catch (Exception e) {
				MessageDialog.openInformation(null, "异常", e.getMessage());
			}
		}
	}
	
	
	protected void refreshDate(){
		if(cal != null && selectDateObj != null){
			selectDateObj.setYear(cal.getYear());
			selectDateObj.setMonth(cal.getMonth());
			selectDateObj.setDay(cal.getDay());
		}
	}
	protected void onProgress(ProgressEvent event) {
		
		if(event.total == 0){
			return;
		}
		int ratio = event.current*100/event.total;
		
		try{
		statusLine = getViewSite().getActionBars().getStatusLineManager();
		statusLine.getProgressMonitor().worked(ratio);
		}
		catch(Exception e){
			
		}
	
	}

@Override
	public void setFocus() {
		if(this.tb != null){
			this.tb.setFocus();
		}
	}

	public void dispose() {
		super.dispose();
		if(this.browser != null){
		  this.browser.dispose();
		}
	
	}
}
