package com.cari.voip.keyboard.soft.views;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.core.widgets.internal.GraphLabel;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.adapter.switchUsers.SwitchUsersAdapterFactory;
import com.cari.voip.keyboard.soft.image.MenuImage;
import com.cari.voip.keyboard.soft.image.NodeImage;
import com.cari.voip.keyboard.soft.model.Presence;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchEntity;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;
import com.cari.voip.keyboard.soft.views.zest.LooseGridLayoutAlgorithm;
import com.cari.voip.keyboard.soft.views.zest.SwitchGraph;
import com.cari.voip.keyboard.soft.views.zest.SwitchGroupGraphNode;
import com.cari.voip.keyboard.soft.views.zest.SwitchUserComparatorbyFirst;
import com.cari.voip.keyboard.soft.views.zest.SwitchUserGraphNode;

public class GraphNodeView extends ViewPart implements PropertyChangeListener{
	public static final String ID_VIEW = 
		"com.cari.voip.keyboard.soft.views.GraphNodeView";
	private SwitchGraph graph;
	private GridLayoutAlgorithm gridLayout = 
		new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	private TreeLayoutAlgorithm treeLayout =
		new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	private HorizontalTreeLayoutAlgorithm horizontalLayout = 
		new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	private SpringLayoutAlgorithm springLayout = 
		new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	private LooseGridLayoutAlgorithm looseLayout=
		new LooseGridLayoutAlgorithm(LayoutStyles.NONE);
	private AbstractLayoutAlgorithm layout;
	private Label label;
	private SwitchUserGraphNode selectedNode = null;
	private SwitchUsersSession session;
	private Menu contextMenu;
	private MenuItem itemCall;
	private MenuItem itemMsg;
	private MenuItem itemDisplayResort;
	
	private IAdapterFactory adapterFactory=null;
	private SwitchEntity model = null;
	
	private Image image = NodeImage.SwitchUserOnLine;
	
	
	public GraphNodeView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		//Composite comp= new Composite(parent, SWT.NONE);
		//comp.setLayout(new FillLayout(SWT.VERTICAL));
		
		this.graph = new SwitchGraph(parent,SWT.BORDER|SWT.LEFT_TO_RIGHT|SWT.V_SCROLL);
		this.graph.animation = false;
		this.graph.setDragable(true);
		this.graph.setBackground(ColorConstants.listBackground);
		this.graph.LIGHT_BLUE = ColorConstants.listBackground;//new Color(null, 216, 228, 248);
		this.graph.DARK_BLUE = ColorConstants.black;//new Color(null, 1, 70, 122);
		//this.graph.HIGHLIGHT_COLOR = ColorConstants.yellow;
		//.graph.
		makeContextMenu(this.graph);
		this.graph.setMenu(this.contextMenu);
		/*this.graph.addListener(SWT.Resize, new Listener(){
			public void handleEvent (Event event){
				if(layout != null){
					graph.setLayoutAlgorithm(layout, true);
				}
			}
		});*/
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

				/*
				if (selectedNode != null) {
					
					if(!selectedNode.isEnabled()){
						return;
					}
					selectedNode.changePresence();

				}*/
			
			}

			/**
			 * Sent when a mouse button is pressed.
			 *
			 * @param e an event containing information about the mouse button press
			 */
			public void mouseDown(MouseEvent e){

				
			}

			/**
			 * Sent when a mouse button is released.
			 *
			 * @param e an event containing information about the mouse button release
			 */
			public void mouseUp(MouseEvent e){
				if (selectedNode != null) {
					
					if(!selectedNode.isEnabled()){
						return;
					}
					
					if(session != null){
						session.input(SwitchUsersSession.INPUT_TYPE_USER,
								selectedNode.getModel());
					}
				}
			}
		});
		this.graph.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent event){
				if (event.item == null) {
					selectedNode = null;
				}else{
					
					if(event.item.getData() != null){
						Object obj = event.item.getData();
						if(obj instanceof SwitchUserGraphNode){
						
						selectedNode = (SwitchUserGraphNode)obj;
			
						}
				
					}
				}
			
				
			}
			public void widgetDefaultSelected(SelectionEvent event){}
		});
		
		
		/*this.graph.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e){
				label.setText(e.getSource().getClass().getName());
			}
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				label.setText(e.getSource().getClass().getName());
			}
		});*/
		
		/*GraphNode b = new GraphNode(graph,SWT.NONE,"b",image);
		b.setData(b);
		GraphNode c = new GraphNode(graph,SWT.NONE,"c",image);
		c.setData(c);*/
		this.looseLayout.setComparator(new SwitchUserComparatorbyFirst<LayoutEntity>());
		layout = this.looseLayout;
		/*if(a != null){
			Dimension size = a.getSize();
			Dimension p = graph.getViewport().getSize();
			
			int colNum = p.width/size.width;
			colNum = Math.max(1,colNum);
			int rowNum = (nodeNum+colNum -1)/colNum;
			graph.setPreferredSize(colNum*size.width, rowNum*size.height);
		}*/
		
		//graph.setPreferredSize(2000, 2000);
		if(Activator.usersFirstShow){
			Activator.usersFirstShow = false;
			if(this.model == null && this.adapterFactory == null){
				
				SwitchUsersSession session = Activator.getSwitchUsersSession();
				if(session != null){
					this.model = session.getMembersGroup();
				}
				
				this.adapterFactory = new SwitchUsersAdapterFactory();
			}
			makeGraphNodesFromModel();
		}
		
		

	}

	private void  makeContextMenu(Composite parent) {
		this.contextMenu = new Menu(parent);
		this.contextMenu.addMenuListener(new MenuListener(){
			public void menuShown(MenuEvent e){
				if(graph.getSelection().size() < 1){
					if(itemCall != null){
						itemCall.setEnabled(false);
					}
					if(itemMsg != null){
						itemMsg.setEnabled(false);
					}
					if(itemDisplayResort != null){
						itemDisplayResort.setEnabled(true);
					}
				}
				else{
					if(itemDisplayResort != null){
						itemDisplayResort.setEnabled(false);
					}
					if(itemCall != null){
						itemCall.setEnabled(true);
					}
					if(itemMsg != null){
						boolean enable = true;
						Object m = getModel();
						if(session != null && m != null && m instanceof SwitchUsersGroup){
							SwitchUsersGroup g = (SwitchUsersGroup)m;
							if(g.getParent() == session.getgatewayUserManager()){
								enable = false;
							}
						}
						itemMsg.setEnabled(enable);
					}
				}
			}
			public void menuHidden(MenuEvent e){
				
			}
		});
/*		
		this.itemCall = new MenuItem(this.contextMenu,SWT.PUSH);
		this.itemCall.setText("ºô½Ð");
		this.itemCall.setImage(MenuImage.SwitchUserCall);
		//itemCall.setData(a);
		
		this.itemCall.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent event){

				List selectList = graph.getSelection();
				if(selectList.size() > 0){
					Iterator iter = selectList.iterator();
					while(iter.hasNext()){
						GraphItem item = (GraphItem)iter.next();
						if(item != null &&
								item instanceof SwitchUserGraphNode){
							//((SwitchUserGraphNode)item).changePresence();

								
								if(!((SwitchUserGraphNode)item).isEnabled()){
									continue;
								}
								
								if(session != null){
									session.input(SwitchUsersSession.INPUT_TYPE_USER,
											((SwitchUserGraphNode)item).getModel());
								}
							
						}
					}
				}
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e){

			}
		});
*/	
		this.itemMsg = new MenuItem(this.contextMenu,SWT.PUSH);
		this.itemMsg.setText("¶ÌÐÅ");
		this.itemMsg.setImage(MenuImage.SwitchUserMsg);
		//itemCall.setData(a);
		
		this.itemMsg.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent event){

				List selectList = graph.getSelection();
				
				int listSize;
				if(selectList != null && (listSize = selectList.size()) > 0){
					StringBuilder tob = new StringBuilder(listSize*10);
					Iterator iter = selectList.iterator();
					while(iter.hasNext()){
						GraphItem item = (GraphItem)iter.next();
						if(item != null &&
								item instanceof SwitchUserGraphNode){
							//((SwitchUserGraphNode)item).changePresence();

								
								if(!((SwitchUserGraphNode)item).isEnabled()){
									continue;
								}
								
								SwitchEntity m = ((SwitchUserGraphNode)item).getModel();
								if(m != null && m instanceof SwitchUser){
									tob.append(((SwitchUser)m).getUserId()+";");
								}
							
						}
					}
					showMsgView(tob.toString());
				}
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e){

			}
		});
		
		this.itemDisplayResort = new MenuItem(this.contextMenu,SWT.PUSH);
		this.itemDisplayResort.setText("¶ÔÆë");
		this.itemDisplayResort.setImage(MenuImage.GraphNodeLayout);
		//itemCall.setData(a);
		
		this.itemDisplayResort.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent event){
				
				graph.setLayoutAlgorithm(layout, true);
			}
			
			public void widgetDefaultSelected(SelectionEvent e){

			}
		});
		
		
	}
	public void showMsgView(String to){
		
		IWorkbenchPage page = getSite().getPage();
		MsgView view = null;
		IViewReference[] viewRefs = page.getViewReferences();
		for(IViewReference viewRef:viewRefs){
			if(viewRef.getId().equals(MsgView.ID_VIEW)){
				MsgView viewTmp = (MsgView)viewRef.getView(true);
				//if(viewTmp.getModel().equals(obj)){
					view = viewTmp;
				//}
			}
		}
		if(view == null){
			try{
			view =
			(MsgView)page.showView(MsgView.ID_VIEW, 
					"", IWorkbenchPage.VIEW_ACTIVATE);
			
			view.setTo(to);
			
			}catch(Exception e){
				if(view != null){
					view.dispose();
				}
				e.printStackTrace();
			}
		}else{
			view.setTo(to);
			page.activate(view);
		}
	
	}
	
	protected void addUser(Object newSwitchUser){
		if(newSwitchUser != null &&
				newSwitchUser instanceof SwitchUser){
			IWorkbenchAdapter childAdapter = (IWorkbenchAdapter)
			this.adapterFactory.getAdapter(newSwitchUser, IWorkbenchAdapter.class);
			if(childAdapter == null){
				return;
			}
			
			image = NodeImage.getImageFromSwitchUserPresence((SwitchUser)newSwitchUser);
			if(image == null){
				if(childAdapter.getImageDescriptor(newSwitchUser) != null){
					image = childAdapter.getImageDescriptor(newSwitchUser).createImage();
				}
				else{
					image = NodeImage.SwitchUserOffLine;
				}
			}
			SwitchUserGraphNode a = new SwitchUserGraphNode((SwitchEntity)newSwitchUser,this.graph,SWT.NO_BACKGROUND,
					childAdapter.getLabel(newSwitchUser),
					image);
			a.setData(a);
			
			this.graph.setLayoutAlgorithm(this.layout, true);
		}
	}
	public void propertyChange(final PropertyChangeEvent evt) {
		Activator.getDisplay().asyncExec(new Runnable() {
		      public void run() {
		    	  if(evt.getPropertyName().equals(SwitchUsersGroup.PROP_USER_ADD)){
		    		  addUser(evt.getNewValue());
		    		 
		    	  }
		    	 
			}
		   });
	}
	public void makeGraphNodesFromModel(){
			if(this.session == null){
				this.session = Activator.getSwitchUsersSession();
			}
			if(this.model == null || this.adapterFactory == null){
				return;
			}
			IWorkbenchAdapter adapter = (IWorkbenchAdapter)
				this.adapterFactory.getAdapter(this.model, IWorkbenchAdapter.class);
			
			if(adapter == null){
				return;
			}
			this.graph.setModel(this.model);
			Object[] children = adapter.getChildren(this.model);
			if(children.length < 1){
				this.model.addPropertyChangeListener(this);
				return;
			}
			this.model.addPropertyChangeListener(this);
			IWorkbenchAdapter childAdapter = (IWorkbenchAdapter)
			this.adapterFactory.getAdapter(children[0], IWorkbenchAdapter.class);
			if(childAdapter == null){
				return;
			}
			for(Object child:children){
				if(child instanceof SwitchEntity){
					if(child instanceof SwitchUser){
						image = NodeImage.getImageFromSwitchUserPresence((SwitchUser)child);
					}
					if(image == null){
						if(childAdapter.getImageDescriptor(child) != null){
							image = childAdapter.getImageDescriptor(child).createImage();
						}
						else{
							image = NodeImage.SwitchUserOffLine;
						}
					}
					SwitchUserGraphNode a = new SwitchUserGraphNode((SwitchEntity)child,this.graph,SWT.NO_BACKGROUND,
							childAdapter.getLabel(child),
							image);
					a.setData(a);
				}
				
				
			}
			
			this.graph.setLayoutAlgorithm(this.layout, true);
			
	}

	public void setAdapterFactory(IAdapterFactory adapterFactory){
		this.adapterFactory = adapterFactory;
	}
	public IAdapterFactory getAdapterFactory(){
		return this.adapterFactory;
	}
	public void setModel(Object obj){
		if(obj instanceof SwitchEntity){
			this.model = (SwitchEntity)obj;
		}
		
	}
	public Object getModel(){
		return this.model;
	}
	
	
	public void setName(String partName){
		this.setPartName(partName);
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		this.graph.setFocus();
	}
	public void dispose() {
		if(this.model != null){
			try{
				this.model.removePropertyChangeListener(this);
			}catch(Exception e){
				
			}
		}
		 super.dispose();
	 }
}
