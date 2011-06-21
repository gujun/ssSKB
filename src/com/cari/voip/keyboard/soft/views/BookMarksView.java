package com.cari.voip.keyboard.soft.views;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.adapter.conf.ConfAdapterFactory;
import com.cari.voip.keyboard.soft.model.Presence;
import com.cari.voip.keyboard.soft.model.conf.APsEntry;
import com.cari.voip.keyboard.soft.model.conf.APsGroup;
import com.cari.voip.keyboard.soft.model.conf.ConfSession;
import com.cari.voip.keyboard.soft.model.conf.Contact;
import com.cari.voip.keyboard.soft.model.conf.ContactsEntry;
import com.cari.voip.keyboard.soft.model.conf.ContactsGroup;
import com.cari.voip.keyboard.soft.model.conf.IADsEntry;
import com.cari.voip.keyboard.soft.model.conf.IADsGroup;
import com.cari.voip.keyboard.soft.model.conf.SwitchsEntry;
import com.cari.voip.keyboard.soft.model.conf.SwitchsGroup;

public class BookMarksView extends ViewPart {

	public static final String ID_VIEW = 
		BookMarksView.class.getName();
	
	private static int webViewIndex = 1;
	
	//private Label label;
	
	private Action actionAddBookmark;
	
	private TreeViewer treeViewer;
	private IAdapterFactory adapterFactory = new ConfAdapterFactory();
	private ConfSession session;
	
	public BookMarksView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite comp= new Composite(parent, SWT.NONE);
		comp.setLayout(new FillLayout(SWT.VERTICAL));
		/*
		label.addMouseListener(new MouseListener(){
			public void mouseDown(MouseEvent e){
				
			}
			public void mouseUp(MouseEvent e){
				
			}
			public void mouseDoubleClick(MouseEvent e){
				try{
					WebBrowserView webView = (WebBrowserView)
					getViewSite().getWorkbenchWindow().getActivePage()
					.showView(WebBrowserView.ID_VIEW, Integer.toString(webViewIndex), IWorkbenchPage.VIEW_ACTIVATE);
					webViewIndex++;
					webView.navigateTo(label.getText());
				}catch(PartInitException ex){
					ex.printStackTrace();
					
				}
			}
		});*/
		initSession();
		initTreeViewer(comp);
		
		//label = new Label(comp,SWT.NONE);
		//label.setText(Integer.toString(this.session.getRoot().getEntries().length));
		
		makeActions();
		contributeToActionBars();
		
		
		
		
	}

	private void initTreeViewer(Composite parent) {
		this.treeViewer = new TreeViewer(parent,SWT.BORDER|SWT.MULTI|SWT.V_SCROLL);
		
		Platform.getAdapterManager().registerAdapters(this.adapterFactory, Contact.class);
		
		getSite().setSelectionProvider(this.treeViewer);
		this.treeViewer.setLabelProvider(new WorkbenchLabelProvider());
		this.treeViewer.setContentProvider(new BaseWorkbenchContentProvider());
		this.treeViewer.setInput(this.session.getRoot());
		this.treeViewer.addDoubleClickListener(new IDoubleClickListener(){
				public void doubleClick(DoubleClickEvent event){
					ISelection selection = event.getSelection();
					if(selection instanceof IStructuredSelection){
						Object obj = ((IStructuredSelection)selection).getFirstElement();
							if(obj instanceof ContactsEntry ){
								ContactsEntry entry = (ContactsEntry)obj;
								tryOpenWeb(entry.getUrl());
							}
							
					}
				}
			}
		);
	}

	private void initSession() {
		this.session = new ConfSession();
		this.session.init();
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager menuManager) {
		
		menuManager.add(actionAddBookmark);
		
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager toolBarManager) {
		
		toolBarManager.add(actionAddBookmark);
		
		toolBarManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void makeActions() {
		actionAddBookmark = new Action(){
			public void run(){
				
			}
		};
			
		actionAddBookmark.setText("新增");
		actionAddBookmark.setToolTipText("新增");
		actionAddBookmark.setImageDescriptor(Activator.getImageDescriptor("icons/add.png"));
		
		
	}

	@Override
	public void setFocus() {
		this.treeViewer.getControl().setFocus();
	}
	public void addBookmark(final String url){
		//label.setText(url);
	}
	
	 public void dispose() {
		 Platform.getAdapterManager().unregisterAdapters(this.adapterFactory);
		 super.dispose();
		 
	 }
	 public void tryOpenWeb(String url){
		 if(url == null || url.length() == 0){
			 return;
		 }
		 try{
				WebBrowserView view = (WebBrowserView)getViewSite().getWorkbenchWindow().getActivePage()
				.showView(WebBrowserView.ID_VIEW, Integer.toString(webViewIndex), IWorkbenchPage.VIEW_ACTIVATE);
				view.navigateTo(url);
				webViewIndex++;
			}catch(PartInitException ex){
				ex.printStackTrace();
				
			}
	 }
}
