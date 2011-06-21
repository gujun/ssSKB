package com.cari.voip.keyboard.soft.views;


import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.dialogs.dispatDtDialog;
import com.cari.voip.keyboard.soft.image.MenuImage;
import com.cari.voip.keyboard.soft.image.NodeImage;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchDispatchCtrl;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;
import com.cari.voip.keyboard.soft.views.zest.ControlKeyGraphNode;
import com.cari.voip.keyboard.soft.views.zest.LooseGridLayoutAlgorithm;
import com.cari.voip.keyboard.soft.views.zest.SwitchUserGraphNode;
import com.cari.voip.keyboard.stack.CCKPConnection;
import com.cari.voip.keyboard.stack.CCKPConnectionException;
import com.cari.voip.keyboard.stack.events.Packet;
import com.cari.voip.keyboard.stack.events.PacketFilter;
import com.cari.voip.keyboard.stack.events.ReplyListener;
import com.cari.voip.keyboard.stack.events.TrapEventListener;
import com.cari.voip.keyboard.stack.events.XMLBody;
import com.cari.voip.keyboard.stack.events.XMLNode;

public class ControllerView extends ViewPart {

	public static final String ID_VIEW = 
		"com.cari.voip.keyboard.soft.views.ControllerView";
	
	private Graph graph;
	private HorizontalTreeLayoutAlgorithm horizontalLayout = 
		new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	private LooseGridLayoutAlgorithm looseLayout=
		new LooseGridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	private AbstractLayoutAlgorithm layout;
	private ControlKeyGraphNode selectedNode = null;
	private SwitchDispatchCtrl[] models = null;
	private SwitchUsersSession session = null;
	
	private Action actionDt;
	
	private TrapEventListener trapDispatDtCfglistener;
	private ReplyListener replyDispatDtSetListener;
	private ReplyListener replyDispatDtShowListener;
	
	private boolean bShowDtDialog = false;
	private boolean bShowDtMsg = false;
	
	//private boolean bDtEnable = false;
	private boolean bDrTraped = false;
	
	private String dt_en = null;
	private String dt = null;
	private String dt_p = null;
	
	public ControllerView() {
		super();
		this.trapDispatDtCfglistener = new TrapEventListener(){
			public void processTrap(final Packet event){
				//revealListeners.add(event);
				Activator.getDisplay().asyncExec(new Runnable() {
				      public void run() {
				    	  bDrTraped = true;
				    	  dealXMLBody(event);
				    	  refleshDtLabel();
				      }
				   });
			}
		};
		
		
		this.replyDispatDtShowListener = new ReplyListener(){

			@Override
			public void processReply(Packet command, final Packet reply) {
				Activator.getDisplay().asyncExec(new Runnable() {
				      public void run() {
				    	  bDrTraped= true;
				    	  dealXMLBody(reply);
				    	  refleshDtLabel();
				    	  
				    	  if(bShowDtDialog){
				    		  showDtDialog();
				    	  }
				      }
				   });
			}
			
		};
		this.replyDispatDtSetListener = new ReplyListener(){

			@Override
			public void processReply(Packet command, final Packet reply) {
				Activator.getDisplay().asyncExec(new Runnable() {
				      public void run() {
				    	  //messagebox
				      }
				   });
			}
			
		};
	}

	@Override
	public void createPartControl(Composite parent) {
		this.graph = new Graph(parent,SWT.LEFT_TO_RIGHT|SWT.V_SCROLL);//SWT.BORDER|
		this.graph.setDragable(false);
		
		this.graph.LIGHT_BLUE = ColorConstants.buttonLightest;//new Color(null, 216, 228, 248);
		this.graph.DARK_BLUE = ColorConstants.black;//new Color(null, 1, 70, 122);
		this.graph.HIGHLIGHT_COLOR = ColorConstants.buttonDarkest;
		this.graph.BORDER_COLOR = ColorConstants.button;
		this.graph.HIGHLIGHT_FORECOLOR = ColorConstants.white;
		
		this.graph.addListener(SWT.Resize, new Listener(){
			public void handleEvent (Event event){
				if(layout != null){
					graph.setLayoutAlgorithm(layout, true);
				}
			}
		});
		
		this.graph.addMouseListener(new MouseListener(){
			/**
			 * Sent when a mouse button is pressed twice within the 
			 * (operating system specified) double click period.
			 *
			 * @param e an event containing information about the mouse double click
			 *
			 * @see org.eclipse.swt.widgets.Display#getDoubleClickTime()
			 */
			public void mouseDoubleClick(MouseEvent event){

			
			}

			/**
			 * Sent when a mouse button is pressed.
			 *
			 * @param e an event containing information about the mouse button press
			 */
			public void mouseDown(MouseEvent e){
				
				if (selectedNode != null) {
					
					if(!selectedNode.isEnabled()){
						return;
					}
					if(session != null){
						session.input(SwitchUsersSession.INPUT_TYPE_CTRL,
								selectedNode.getModel());
					}

				}
			}

			/**
			 * Sent when a mouse button is released.
			 *
			 * @param e an event containing information about the mouse button release
			 */
			public void mouseUp(MouseEvent e){
				
			}
		});
		
		this.graph.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent event){
				if (event.item == null) {
					selectedNode = null;
				}else{
					
					if(event.item.getData() != null){
						Object obj  = event.item.getData();
						if(obj instanceof ControlKeyGraphNode){
							selectedNode = (ControlKeyGraphNode)obj;
						}
						
						
						}
				
					}
				}
			
				
			public void widgetDefaultSelected(SelectionEvent event){}
		});
		
		this.looseLayout.setColPadding(2);
		this.looseLayout.setRowPadding(2);
		
		layout = this.looseLayout;
		
		//
		if(this.session == null){
			this.session = Activator.getSwitchUsersSession();
		}
		
		makeGraphNodesFromModel();
		graph.setLayoutAlgorithm(layout, true);
		
		makeActions();
		contributeToActionBars();
		attachListener();
		
		this.bShowDtDialog = false;
		sendDtShowPacket(this.replyDispatDtShowListener);
	}

	public void makeGraphNodesFromModel(){
		if(this.session == null){
			this.session = Activator.getSwitchUsersSession();
		}
		if(this.session == null){
			return;
		}
		if(this.models == null){
			this.models = this.session.getControls();
		}
		if(this.models == null){
			return;
		}
		for(SwitchDispatchCtrl m : models){
			if(m != null){
				ControlKeyGraphNode node = new  ControlKeyGraphNode(
						m,
						this.graph,SWT.NO_BACKGROUND,
						((SwitchDispatchCtrl)m).getLable(),
						NodeImage.getImageFromSwitchCtrlType(((SwitchDispatchCtrl)m).getCtrlType()));
				node.setData(node);
				
			}
		}
		/*ControlKeyGraphNode bridge = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  呼    叫  ",NodeImage.ControlBridge);
		
		ControlKeyGraphNode wiretapping = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  监    听  ",NodeImage.ControlWiretapping);
		ControlKeyGraphNode insert = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  插    话  ",NodeImage.ControlInsert);
		ControlKeyGraphNode grab = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  夺    话  ",NodeImage.ControlGrab);
		ControlKeyGraphNode unbridge = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  强    拆  ",NodeImage.ControlUnbridge);
		
		ControlKeyGraphNode conference = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  组    呼  ",NodeImage.ControlConference);
		

		ControlKeyGraphNode justHear = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  单    听  ",NodeImage.ControlJustHear);
		ControlKeyGraphNode hearAndSpeak = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  听    说  ",NodeImage.ControlHearAndSpeak);
		
		ControlKeyGraphNode addMember = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  添加成员  ",NodeImage.ControlAddMember);
		
		ControlKeyGraphNode kickMember = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  删除成员  ",NodeImage.ControlKickMember);
		
		
		ControlKeyGraphNode moderator = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  设定主席  ",NodeImage.ControlModerator);
		ControlKeyGraphNode noModerator = new  ControlKeyGraphNode(
				this.graph,SWT.NO_BACKGROUND,"  取消主席  ",NodeImage.ControlNoModerator);
		
*/
		
		//this.graph.setLayoutAlgorithm(this.layout, true);
	}
	protected void dealXMLBody(final Packet event){
		if(event == null){
			return;
		}
		XMLBody body = event.getXMLBody();
		if(body == null){
			return;
		}
		XMLNode xmlRoot = body.getXMLRoot();
		if(xmlRoot == null){
			return;
		}
		/*<configuration name="dispat_call.conf" description="dispatcher calling behavior conf">
        <settings>
                            <param name="call-delim" value=","></param>
                            <param name="direct-transfer-enable" value="false"></param>
                            <param name="direct-transfer" value=""></param>
                            <param name="direct-transfer-period" value=""></param>
  </settings>
</configuration>*/
		XMLNode settings = xmlRoot.getChild("settings");
		if(settings == null){
			return;
		}
		XMLNode param = settings.getChild("param");
		if(param == null){
			return;
		}
		while(param != null){
			String name = param.getAttr("name");
			String value = param.getAttr("value");//status
			if(name == null){
				param = param.getNext();
				continue;
			}
			name = name.toLowerCase();
			
			if(name.equals("direct-transfer-enable")){
				this.dt_en = value;
			}else if(name.equals("direct-transfer")){
				this.dt = value;
			}else if(name.equals("direct-transfer-period")){
				this.dt_p = value;
			}
			
			param = param.getNext();
		}
	}
	protected void refleshDtLabel() {
		if(this.dt_en != null && (this.dt_en = this.dt_en.trim()) != null && this.dt_en.toLowerCase().startsWith("true")){
			
			actionDt.setImageDescriptor(Activator.getImageDescriptor("icons/moon16.gif"));
		}
		else{
			
			actionDt.setImageDescriptor(Activator.getImageDescriptor("icons/moon16gray.gif"));
			
		}
	}
	protected void showDtDialog(){
		dispatDtDialog dtDialog = new dispatDtDialog(null,dt_en,dt,dt_p);
		if (dtDialog.open() == Window.OK) {
			sendDtSetPacket(dtDialog.m_dt_en,dtDialog.m_dt,dtDialog.m_dt_p,null);
			bShowDtDialog = false;
			sendDtShowPacket(replyDispatDtShowListener);
		}
	}
	protected void sendDtShowPacket(ReplyListener replyHandler){
		Packet packet = null;
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null){
			CCKPConnection connection = session.getConnection();
			if(connection != null){
				packet = new Packet(Packet.TYPE_COMMAND_API);
				packet.setProperty("cmd", "cfg");
				packet.setProperty("data","show dispat dt");
				if(replyHandler != null){
					packet.setReplyHandler(replyHandler);
				}
				try {
					connection.sendPacket(packet);
				} catch (CCKPConnectionException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		}
		
		
	}
	protected void sendDtSetPacket(String dtEanble,String dtStr,String dt_pStr,ReplyListener replyHandler){
		Packet packet = null;
		String data = "set dispat dt ";
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null){
			CCKPConnection connection = session.getConnection();
			if(connection != null){
				packet = new Packet(Packet.TYPE_COMMAND_API);
				if(dtEanble != null && (dtEanble = dtEanble.trim()) != null && dtEanble.toLowerCase().startsWith("true")){
					data = data.concat(" true ");
				}else {
					data = data.concat(" false ");
				}
					
				
				if(dtStr != null && (dtStr=dtStr.trim()) != null && dtStr.length()>0){
						data = data.concat(dtStr);
				}else {
					data = data.concat("null");
				}
				//data = data.concat(dtStr!=null?dtStr:"null");
				data = data.concat(" ");
				if(dt_pStr != null && (dt_pStr=dt_pStr.trim()) != null && dt_pStr.length()>0){
					data = data.concat(dt_pStr);
				}else {
					data = data.concat("null");
				}
				//data = data.concat(dt_pStr!=null?dt_pStr:"null");
				
				packet.setProperty("cmd", "cfg");
				packet.setProperty("data", data);
				if(replyHandler != null){
					packet.setReplyHandler(replyHandler);
				}
				try {
					connection.sendPacket(packet);
				} catch (CCKPConnectionException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		}
		
		
	}
	private void makeActions() {
		actionDt = new Action(){
			public void run(){
				if(bDrTraped){
					/*if(bDtEnable){
						bShowDtMsg = false;
						sendDtSetPacket(false,dt,dt_p,null);
						bShowDtDialog = false;
						sendDtShowPacket(replyDispatDtShowListener);
						
					}else{
						
						//bShowDtDialog = true;
						//sendDtShowPacket(replyDispatDtShowListener);
						
						//dialog
						
						bShowDtMsg = false;
						sendDtSetPacket(true,dt,dt_p,null);
						bShowDtDialog = false;
						sendDtShowPacket(replyDispatDtShowListener);
						
						
					}*/
					showDtDialog();
				}else {
					bShowDtDialog = true;
					sendDtShowPacket(replyDispatDtShowListener);
				}
			}
		};
			
		actionDt.setText("夜服");
		actionDt.setToolTipText("设置夜服");
		actionDt.setImageDescriptor(Activator.getImageDescriptor("icons/moon16gray.gif"));
		
		
	}
	private void fillLocalToolBar(IToolBarManager toolBarManager) {
		
		toolBarManager.add(actionDt);
	}
	private void contributeToActionBars() {
		
		IActionBars bars = getViewSite().getActionBars();
		
		fillLocalToolBar(bars.getToolBarManager());
	}
	private void attachListener() {
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null){
			CCKPConnection connection = session.getConnection();
			if(connection != null){
				
				connection.addTrapEventListeners(this.trapDispatDtCfglistener, new PacketFilter(){
					public boolean accept(Packet packet){
						boolean ret = false;
						if(packet.getPacketType() == Packet.TYPE_TRAP_CFG_DISPAT_CALL){
							ret = true;
						}
						return ret;
					}
				});
				
				
			}
		}
	}
	private void detachListener(){
		 SwitchUsersSession session = Activator.getSwitchUsersSession();
			if(session != null){
				CCKPConnection connection = session.getConnection();
				if(connection != null){
					
					connection.removeTrapEventListeners(trapDispatDtCfglistener);
				}
			}
	}
	@Override
	public void setFocus() {
		

	}
	public void dispose() {
		detachListener();
		 super.dispose();
	 }
}
