package com.cari.voip.keyboard.soft.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.adapter.switchUsers.SwitchUsersAdapterFactory;
import com.cari.voip.keyboard.soft.editors.edit.SwitchUserEditPartFactory;
import com.cari.voip.keyboard.soft.editors.graphicalView.SwitchUserGraphicalViewer;
import com.cari.voip.keyboard.soft.image.MenuImage;
import com.cari.voip.keyboard.soft.image.NodeImage;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchEntity;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersManager;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;
import com.cari.voip.keyboard.soft.views.zest.LooseGridLayoutAlgorithm;
import com.cari.voip.keyboard.soft.views.zest.SwitchGraph;
import com.cari.voip.keyboard.soft.views.zest.SwitchGroupGraphNode;
import com.cari.voip.keyboard.soft.views.zest.SwitchUserGraphNode;

public class GraphGroupView extends ViewPart  implements PropertyChangeListener{

	public static final String ID_VIEW = 
		"com.cari.voip.keyboard.soft.views.GraphGroupView";
	
	/*
	//private GraphicalViewer graphicalViewer = null;
	//private EditDomain editDomain = null;
	
	//private SwitchUsersGroup diagram = null;
	*/
	
	private SwitchGraph graph;
	private LooseGridLayoutAlgorithm looseLayout=
		new LooseGridLayoutAlgorithm(LayoutStyles.NONE);
	private AbstractLayoutAlgorithm layout;
	
	private Menu contextMenu;
	private MenuItem itemCall;
	private MenuItem itemMsg;
	private MenuItem itemDisplayResort;
	
	private SwitchGroupGraphNode selectedNode= null;
	private SwitchEntity model = null;
	private IAdapterFactory adapterFactory=null;
	private SwitchUsersSession session;
	
	private Image image = NodeImage.groupImage;
	
	public GraphGroupView() {
		super();
		//this.editDomain = new EditDomain();
	}

	@Override
	public void createPartControl(Composite parent) {
		//createGraphicalViewer(parent);

		this.graph = new SwitchGraph(parent,SWT.BORDER|SWT.LEFT_TO_RIGHT|SWT.V_SCROLL);
		this.graph.setDragable(true);
		this.graph.setBackground(ColorConstants.listBackground);
		this.graph.LIGHT_BLUE = ColorConstants.listBackground;//new Color(null, 216, 228, 248);
		this.graph.DARK_BLUE = ColorConstants.black;//new Color(null, 1, 70, 122);
		
		makeContextMenu(this.graph);
		this.graph.setMenu(this.contextMenu);
		
		this.graph.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent event){
					/*if (selectedNode != null) {
					
					//if(!selectedNode.isEnabled()){
					//	return;
					//}
					
					Activator.showViewFromModel(getSite().getPage(),selectedNode.getModel());

				}*/
			}
			
			public void mouseDown(MouseEvent e){
				
			}
			
			public void mouseUp(MouseEvent e){
					if (selectedNode != null) {
					
					
					if(session != null){
						Activator.showUserViewFromModel(getSite().getPage(),selectedNode.getModel());
						session.input(SwitchUsersSession.INPUT_TYPE_GROUP,
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
						if(obj instanceof SwitchGroupGraphNode){
						
						selectedNode = (SwitchGroupGraphNode)obj;
			
						}
					}
					
				}
			}
		
		   public void widgetDefaultSelected(SelectionEvent event){}
	   });
		
		layout = this.looseLayout;
		
		makeGraphNodesFromModel();
	}
/*
	protected void configureGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new SwitchUserEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		
	}
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		
		((SwitchUserGraphicalViewer) viewer).hookPaintListener();
		
		if(this.diagram != null){
			viewer.setContents(this.diagram);
			((SwitchUserGraphicalViewer) viewer).applyLayout();
		}
	}
	
	protected void createGraphicalViewer(Composite parent) {
		
		GraphicalViewer viewer = new SwitchUserGraphicalViewer();
		viewer.createControl(parent);
		setGraphicalViewer(viewer);
		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();
		
		
	}


	private void hookGraphicalViewer() {
		
		getSite().setSelectionProvider(getGraphicalViewer());
	}

	protected GraphicalViewer getGraphicalViewer() {
		
		return this.graphicalViewer;
	}

	protected EditDomain getEditDomain(){
		return this.editDomain;
	}


	protected void setGraphicalViewer(GraphicalViewer viewer) {
		EditDomain dm = getEditDomain();
		if( dm != null){
			dm.addViewer(viewer);
		}
		
		this.graphicalViewer = viewer;
	}
	
	public void makeGraphNodesFromModel(){
		GraphicalViewer viewer = getGraphicalViewer();
		if(viewer != null && this.diagram != null){
			viewer.setContents(this.diagram);
			((SwitchUserGraphicalViewer) viewer).applyLayout();
		}
	}
	*/

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
						itemMsg.setEnabled(true);
					}
				}
			}
			public void menuHidden(MenuEvent e){
				
			}
		});
		
		/*this.itemCall = new MenuItem(this.contextMenu,SWT.PUSH);
		this.itemCall.setText("组呼");
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
								item instanceof SwitchGroupGraphNode){

							Activator.showViewFromModel(getSite().getPage(),selectedNode.getModel());
							//((SwitchUserGraphNode)item).changePresence();
							if(!((SwitchGroupGraphNode)item).isEnabled()){
								continue;
							}
							
							if(session != null){
								session.input(SwitchUsersSession.INPUT_TYPE_GROUP,
										((SwitchGroupGraphNode)item).getModel());
							}
						}
					}
				}
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e){

			}
		});
		*/
		if(Activator.groupsFirstShow){
		this.itemMsg = new MenuItem(this.contextMenu,SWT.PUSH);
		this.itemMsg.setText("群发短信");
		this.itemMsg.setImage(MenuImage.SwitchUserMsg);
		//itemCall.setData(a);
		
		this.itemMsg.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent event){

				List selectList = graph.getSelection();
				int listSize = selectList.size();
				if(listSize > 0){
					Iterator iter = selectList.iterator();
					StringBuilder tob = new StringBuilder(listSize*10);
					while(iter.hasNext()){
						GraphItem item = (GraphItem)iter.next();
						if(item != null &&
								item instanceof SwitchGroupGraphNode){

							//((SwitchUserGraphNode)item).changePresence();
							if(!((SwitchGroupGraphNode)item).isEnabled()){
								continue;
							}
							SwitchEntity m = ((SwitchGroupGraphNode)item).getModel();
							if(m != null && m instanceof SwitchUsersGroup){
								SwitchUser[] users = (SwitchUser[])((SwitchUsersGroup)m).getSwitchUsersArray();
								if(users != null && users.length > 0){
									for(int i = 0; i<users.length;i++){
										tob.append(users[i].getUserId()+";");
									}
									
								}
								
							}
							
						}
					}
					
					showMsgView(tob.toString());
				}
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e){

			}
		});
		}
		this.itemDisplayResort = new MenuItem(this.contextMenu,SWT.PUSH);
		this.itemDisplayResort.setText("对齐");
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
	public synchronized void makeGraphNodesFromModel(){
		
		if(this.session == null){
			this.session = Activator.getSwitchUsersSession();
		}
		
		if(session == null){
			return;
		}
		if(Activator.groupsFirstShow){
			Activator.groupsFirstShow = false;
			if(this.model == null ){
				this.model = session.getlocalUserManager();
			}if(this.adapterFactory == null){
				this.adapterFactory = new SwitchUsersAdapterFactory();
			}
		}
		else{
			if(this.model == null ){
				this.model = session.getgatewayUserManager();
			}if(this.adapterFactory == null){
				this.adapterFactory = new SwitchUsersAdapterFactory();
			}
			
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
		this.setName(adapter.getLabel(this.model));
		
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
				
				if(image == null){
					if(childAdapter.getImageDescriptor(child) != null){
						image = childAdapter.getImageDescriptor(child).createImage();
					}
					else{
						image = NodeImage.groupImage;
					}
				}
				SwitchGroupGraphNode a = new SwitchGroupGraphNode((SwitchEntity)child,this.graph,SWT.NO_BACKGROUND,
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
		/*GraphicalViewer viewer = getGraphicalViewer();
		if(viewer != null){
			viewer.getControl().setFocus();
		}*/
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

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		Activator.getDisplay().asyncExec(new Runnable() {
		      public void run() {
		    	  if(evt.getPropertyName().equals(SwitchUsersManager.PROP_GROUP_ADD)){
		    		  addgroup(evt.getNewValue());
		    		 
		    	  }
		    	 
			}
		   });
	}

	protected void addgroup(Object newValue) {
		if(newValue != null &&
				newValue instanceof SwitchUsersGroup){
			IWorkbenchAdapter childAdapter = (IWorkbenchAdapter)
			this.adapterFactory.getAdapter(newValue, IWorkbenchAdapter.class);
			if(childAdapter == null){
				return;
			}
			
			if(image == null){
				if(childAdapter.getImageDescriptor(newValue) != null){
					image = childAdapter.getImageDescriptor(newValue).createImage();
				}
				else{
					image = NodeImage.groupImage;
				}
			}
			SwitchGroupGraphNode a = new SwitchGroupGraphNode((SwitchEntity)newValue,this.graph,SWT.NO_BACKGROUND,
					childAdapter.getLabel(newValue),
					image);
			a.setData(a);
			
			this.graph.setLayoutAlgorithm(this.layout, true);
		}
	}

}
