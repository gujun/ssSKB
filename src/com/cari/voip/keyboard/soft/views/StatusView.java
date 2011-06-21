package com.cari.voip.keyboard.soft.views;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.ViewPart;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.adapter.switchUsers.SwitchUsersAdapterFactory;
import com.cari.voip.keyboard.soft.editors.SwitchUserGraphicalEditor;
import com.cari.voip.keyboard.soft.editors.input.GatewayInput;
import com.cari.voip.keyboard.soft.editors.input.SwitchUserInput;
//import com.cari.voip.keyboard.soft.adapter.switchUsers.SwitchUsersTreeAdapterFactory;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchEntity;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersManager;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;
//import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersTree;
//import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersTreeEntry;
//import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersTreeGroup;

public class StatusView extends ViewPart {
	public static final String ID_VIEW = 
		"com.cari.voip.keyboard.soft.views.StatusView";
	
	private TreeViewer treeViewer;
	//private IAdapterFactory adapterFactory = new SwitchUsersTreeAdapterFactory();
	private IAdapterFactory adapterFactory = new SwitchUsersAdapterFactory();
	private SwitchUsersSession session;
	private  int viewIndex = 0;
	
	public StatusView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		//Composite comp= new Composite(parent, SWT.NONE);
		//comp.setLayout(new FillLayout(SWT.VERTICAL));
		
		initSession();
		initTreeViewer(parent);
		//Label label = new Label(parent,SWT.CENTER);
		//label.setText("hello");

	}

	private void initTreeViewer(Composite parent) {
		this.treeViewer = new TreeViewer(parent,SWT.BORDER|SWT.MULTI|SWT.V_SCROLL);
		/*
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUsersTreeGroup.class);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUsersTreeEntry.class);
		*/
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUsersManager.class);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUsersGroup.class);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUser.class);
		Platform.getAdapterManager().registerAdapters(this.adapterFactory,SwitchUsersSession.class);
		
		getSite().setSelectionProvider(this.treeViewer);
		this.treeViewer.setLabelProvider(new WorkbenchLabelProvider());
		this.treeViewer.setContentProvider(new BaseWorkbenchContentProvider());
		//this.treeViewer.setInput(this.session.getTreeRoot());
		this.treeViewer.setInput(this.session.getRoot());
		
		this.treeViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event){
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection){
					Object obj = ((IStructuredSelection)selection).getFirstElement();
					//activeEditorFromModel(obj);
					if(obj instanceof SwitchUsersGroup){
						showViewFromModel(obj);
					}
					else if(obj instanceof SwitchUsersManager){
						showGroupViewFromModel(obj);
					}
					
					
					
				}//if(selection instanceof IStructuredSelection){
			}//public void doubleClick(DoubleClickEvent event){
		});
	
		
	}
	
	public void showGroupViewFromModel(Object obj) {
		if(obj instanceof SwitchUsersManager){
			IWorkbenchPage page = getSite().getPage();
			GraphGroupView view = null;
			IViewReference[] viewRefs = page.getViewReferences();
			for(IViewReference viewRef:viewRefs){
				if(viewRef.getId().equals(GraphGroupView.ID_VIEW)){
					GraphGroupView viewTmp = (GraphGroupView)viewRef.getView(true);
					if(viewTmp.getModel().equals(obj)){
						view = viewTmp;
					}
				}
			}
			if(view == null){
				try{
				view =
				(GraphGroupView)page.showView(GraphGroupView.ID_VIEW, 
						Integer.toString(viewIndex++), IWorkbenchPage.VIEW_ACTIVATE);
				view.setName(((SwitchEntity)obj).getName());
				view.setAdapterFactory(new SwitchUsersAdapterFactory());
				view.setModel(obj);
				view.makeGraphNodesFromModel();
				
				}catch(Exception e){
				if(view != null){
					view.dispose();
				}
				e.printStackTrace();
				}
			}else{
				page.activate(view);
			}
		}
	}

	public void showViewFromModel(Object obj){
		if(obj instanceof SwitchUsersGroup){
			IWorkbenchPage page = getSite().getPage();
			GraphNodeView view = null;
			IViewReference[] viewRefs = page.getViewReferences();
			for(IViewReference viewRef:viewRefs){
				if(viewRef.getId().equals(GraphNodeView.ID_VIEW)){
					GraphNodeView viewTmp = (GraphNodeView)viewRef.getView(true);
					if(viewTmp.getModel().equals(obj)){
						view = viewTmp;
					}
				}
			}
			if(view == null){
				try{
				view =
				(GraphNodeView)page.showView(GraphNodeView.ID_VIEW, 
						Integer.toString(viewIndex++), IWorkbenchPage.VIEW_ACTIVATE);
				view.setName(((SwitchEntity)obj).getName());
				view.setAdapterFactory(new SwitchUsersAdapterFactory());
				view.setModel(obj);
				view.makeGraphNodesFromModel();
				
				}catch(Exception e){
				if(view != null){
					view.dispose();
				}
				e.printStackTrace();
				}
			}else{
				page.activate(view);
			}
		}
	}
	protected void activeEditorFromModel(Object obj){
		if(obj instanceof SwitchUsersGroup){
			IWorkbenchPage page = getSite().getPage();
			IEditorPart editor = null;
			IEditorInput input = null;
			IEditorReference[] editorRefs = page.getEditorReferences();
			for(IEditorReference editorRef:editorRefs){
				IEditorInput inputTmp = null;
				try {
					inputTmp = editorRef.getEditorInput();
				} catch (PartInitException e) {
					inputTmp = null;
				}
				if(inputTmp == null){
					continue;
				}
				if(inputTmp instanceof SwitchUserInput){
					if(((SwitchUserInput)inputTmp).getSwitchUsersGroup().equals(obj)){
						editor = editorRef.getEditor(true);
						break;
					}
				}
			}
			if(editor == null){
				input = new SwitchUserInput((SwitchUsersGroup)obj);
			
				try{
					editor = page.openEditor(input, SwitchUserGraphicalEditor.ID, true);
					if(editor instanceof SwitchUserGraphicalEditor){
						((SwitchUserGraphicalEditor)editor).setName(
							((SwitchUsersGroup)obj).getName());
					}
				
				
				}catch(Exception e){
					if(editor != null){
						editor.dispose();
					}
					e.printStackTrace();
				
				}
			}//if(editor == null)
			else{
				page.activate(editor);
			}
		}//if(obj instanceof SwitchUsersGroup)
	}
	private void initSession() {
		this.session = Activator.getSwitchUsersSession();
		//this.session.init();
	}

	@Override
	public void setFocus() {
		this.treeViewer.getControl().setFocus();
	}
	public void dispose() {
		Platform.getAdapterManager().unregisterAdapters(this.adapterFactory);
		 super.dispose();
	 }
}
