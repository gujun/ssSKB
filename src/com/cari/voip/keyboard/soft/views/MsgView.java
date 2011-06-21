package com.cari.voip.keyboard.soft.views;


import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;
//import org.eclipse.jface.viewers.TextCellEditor;
import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.adapter.switchUsers.SwitchUsersAdapterFactory;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersManager;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;
import com.cari.voip.keyboard.stack.CCKPConnection;
import com.cari.voip.keyboard.stack.events.Packet;
import com.cari.voip.keyboard.stack.events.PacketFilter;
import com.cari.voip.keyboard.stack.events.TrapEventListener;
import com.cari.voip.keyboard.stack.events.XMLBody;
import com.cari.voip.keyboard.stack.events.XMLNode;

public class MsgView extends ViewPart {
	public static final String ID_VIEW = 
		"com.cari.voip.keyboard.soft.views.MsgView";
	private Text display;
	private Group compb;
	private Shell userShell;
	private Display displayDevice;
	private Shell shell;
	private TreeViewer treeViewer;
	
	private IAdapterFactory adapterFactory = new SwitchUsersAdapterFactory();
	private SwitchUsersSession session;
	
	private Table table;
	private Text input;
	private Text msgto;
	private Button send;
	private Action actionResetDisplay;
	private TrapEventListener listener;
	private final int MaxItemsNum = 10;
	
	public MsgView(){
		super();
		this.listener = new TrapEventListener(){
			public void processTrap(final Packet event){
				//revealListeners.add(event);
				Activator.getDisplay().asyncExec(new Runnable() {
				      public void run() {
				    	  if(event != null && display != null){
				    		  XMLBody xml = event.getXMLBody();
				    		  if(xml != null){
				    			  XMLNode sms = xml.getXMLRoot();
				    			  if(sms != null){
				    				  XMLNode fromNode = sms.getChild("from");
				    				  XMLNode bodyNode = sms.getChild("body");
				    				  if(fromNode != null && bodyNode != null){
				    					  String from = fromNode.getTxt();
				    					  int ai = from.indexOf('@');
				    					  if(ai >= 0){
				    						  int si = from.indexOf(':', 0);
				    						  from = from.substring(((si>0) && (si <ai))?si:0, ai);
				    					  }
				    					  String body = bodyNode.getTxt();
				    					  if(from!=null && body!=null){
				    						  
				    						  display.append(from+":\n\t"+
				    							  body.replace('\n', ' ').replace('\r', ' ')+"\n\n");
				    						  
				    						 /* if(table.getItemCount() > MaxItemsNum){
													table.remove(0,1);
												}
												TableItem row1 = new TableItem(table,SWT.NONE);
												
												row1.setText(new String[]{from+":"});
												
												TableItem row2 = new TableItem(table,SWT.NONE);
												
												row2.setText(new String[]{"",body});
												
												table.setTopIndex(table.getItemCount()-1);*/
				    					  }
				    				  }
				    			  }
				    			   
				    		  }
				    		 
							}
					}
				   });
			}
		};
	}
	@Override
	public void createPartControl(Composite parent) {
		this.displayDevice = Activator.getDisplay();
		this.shell = parent.getShell();
		
		Composite comp= new Composite(parent,SWT.NONE);
		comp.setLayout(new GridLayout(1,true));
		SashForm sash = new SashForm(comp,SWT.VERTICAL);
		sash.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Group dg = new Group(sash,SWT.SHADOW_ETCHED_IN);
		dg.setLayout(new GridLayout(1,true));
		//dg.setText("已发送和接受短消息");
		dg.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		display =new Text(dg,SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
		display.setEditable(false);
		display.setBackground(ColorConstants.white);
		display.setLayoutData(new GridData(GridData.FILL_BOTH));
		//display.setTextLimit(50);
		/*
		table = new Table(sash,
				SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		//table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setBackground(ColorConstants.white);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		TableColumn  colEventType = new TableColumn(this.table,SWT.NONE);
		colEventType.setText("      ");
		//colEventType.setWidth(this.table.getSize().x/4);
		
		TableColumn  colEventContent = new TableColumn(this.table,SWT.NONE);
		colEventContent.setText("                ");
		//colEventContent.setWidth(this.table.getSize().x/4);
		
		
		
		colEventType.pack();
		colEventContent.pack();
		*/
		Group eg = new Group(sash,SWT.SHADOW_ETCHED_IN);
		eg.setText("编辑和发送");
		eg.setLayout(new GridLayout(1,true));
		eg.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//Sash s = new Sash(comp,SWT.VERTICAL);
		//s.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		input = new Text(eg,SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
		input.setEditable(true);
		input.setBackground(ColorConstants.white);
		input.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		compb= new Group(eg,SWT.SHADOW_ETCHED_IN);
		compb.setLayout(new GridLayout(3,false));
		compb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label label = new Label(compb,SWT.CENTER);
		label.setText("To:");
		
		this.userShell = new Shell(displayDevice,SWT.ON_TOP);
		this.userShell.setLayout(new FillLayout());
		this.treeViewer = new TreeViewer(this.userShell,SWT.BORDER|SWT.MULTI|SWT.V_SCROLL);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUsersManager.class);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUsersGroup.class);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUser.class);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUsersSession.class);
		this.session = Activator.getSwitchUsersSession();
		//getSite().setSelectionProvider(this.treeViewer);
		this.treeViewer.setLabelProvider(new WorkbenchLabelProvider());
		this.treeViewer.setContentProvider(new BaseWorkbenchContentProvider());
		//this.treeViewer.setInput(this.session.getTreeRoot());
		this.treeViewer.setInput(this.session.getlocalUserManager());
		this.userShell.pack();
		
		msgto = new Text(compb,SWT.SINGLE | SWT.BORDER);
		msgto.setEditable(true);
		msgto.setBackground(ColorConstants.white);
		msgto.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		msgto.setToolTipText("双击弹出用户列表");
		msgto.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent event){
				treeViewer.refresh();
				//userShell.pack();
				Rectangle dateRect= displayDevice.map(compb,null,msgto.getBounds());
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
		
		
		this.treeViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event){
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection){
					Object obj = ((IStructuredSelection)selection).getFirstElement();
					//activeEditorFromModel(obj);
					if(obj instanceof SwitchUser){
						if(msgto != null){
							String originUsers = msgto.getText();
							if(originUsers != null && originUsers.trim().length()>0){
								msgto.append(";"+((SwitchUser)obj).getUserId());
							}else{
								msgto.setText(((SwitchUser)obj).getUserId());
							}
							
						}
					}
					
					
					
					
				}//if(selection instanceof IStructuredSelection){
				userShell.setVisible(false);
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
		

		
		send = new Button(compb,SWT.PUSH|SWT.RIGHT);
		send.setText("发送");
		//send.setImage(MenuImage.dial);
		//send.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		send.setCursor(new Cursor(parent.getDisplay(),SWT.CURSOR_HAND));
		send.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				String body = input.getText();
				String to = msgto.getText();
				if(body == null || body.length() == 0 ){
					input.setFocus();
					return;
				}
				if(to == null || to.length() == 0){
					msgto.setFocus();
					return;
				}
				SwitchUsersSession session = Activator.getSwitchUsersSession();
				if(session != null){
					//session.input(SwitchUsersSession.INPUT_TYPE_DIAL, number);
					session.message("system", to, "", body, "text/plain");
					display.append("system:\n\t"+body.replace("\n", " ").replace("\r", " ")+"\n\n");
					/*if(table.getItemCount() > MaxItemsNum){
						table.remove(0,1);
					}
					TableItem row1 = new TableItem(table,SWT.NONE);
					
					row1.setText(new String[]{"system:"});
					
					TableItem row2 = new TableItem(table,SWT.NONE);
					
					row2.setText(new String[]{"",body});
					
					table.setTopIndex(table.getItemCount()-1);
					
					*/
				}
			}
		});
		//setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		makeActions();
		contributeToActionBars();
		
		attachListener();
	}
	
	private void attachListener() {
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null){
			CCKPConnection connection = session.getConnection();
			if(connection != null){
				connection.addTrapEventListeners(this.listener, new PacketFilter(){

					@Override
					public boolean accept(Packet packet) {
						if(packet.getPacketType() == Packet.TYPE_TRAP_MESSAGE){
							return true;
						}
						return false;
					}
					
				});
				//connection.addReplyListeners(this.replyListener, null);
			}
		}
	}
	private void makeActions() {
		actionResetDisplay = new Action(){
			public void run(){
				if(display != null){
					display.setText("");
				}
			}
		};
			
		actionResetDisplay.setText("清除");
		actionResetDisplay.setToolTipText("清除短信显示");
		actionResetDisplay.setImageDescriptor(Activator.getImageDescriptor("icons/cut.png"));
		
		
	}
	private void contributeToActionBars() {
		
		IActionBars bars = getViewSite().getActionBars();
		
		fillLocalToolBar(bars.getToolBarManager());
	}
	private void fillLocalToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(actionResetDisplay);
		
		toolBarManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		if(this.input != null){
			this.input.setFocus();
		}
	}
	public void setTo(String to) {
		if(this.msgto != null){
			this.msgto.setText(to);
		}
	}

}
