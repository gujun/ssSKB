package com.cari.voip.keyboard.soft.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.cari.voip.keyboard.soft.Activator;

public class WebBrowserView extends ViewPart {

	public static final String ID_VIEW =
		WebBrowserView.class.getName();
	
	//View widgets
	private Combo urlCombo;
	private Browser browser;
	
	//Local view actions
	private Action actionBack;
	private Action actionForward;
	private Action actionHome;
	private Action actionAddBookmark;
	
	private  String startUrl = 
		"http://www.google.com";
	
	public static ImageDescriptor ICON_HOME =
		Activator.getImageDescriptor("icons/e_home.gif");
	
	private IStatusLineManager statusLine;
	
	public WebBrowserView() {
		super();

	}
	

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite comp= new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1,true));
		
		CoolBar coolbar = new CoolBar(comp, SWT.NONE);
		coolbar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		CoolItem item = new CoolItem(coolbar, SWT.NONE);
		item.setControl(createComboView(coolbar,new GridData(
				GridData.FILL_HORIZONTAL)));
		calcSize(item);
		
		//web browser
		try{
			browser = new Browser(comp, SWT.BORDER);
		}catch(SWTError e){
			return;
		}
		
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		browser.setUrl(startUrl);
		
		browser.addLocationListener(new LocationListener(){
			public void changed(LocationEvent event){
				locChanged(event);
			}
			public void changing(LocationEvent event){
				locChanging(event);
			}
		});
		
		browser.addProgressListener(new ProgressListener(){
			public void changed(ProgressEvent event){
				onProgress(event);
			}
			
			public void completed(ProgressEvent event){
				
			}
		});
		
		makeActions();
		contributeToActionBars();
		
		statusLine = getViewSite().getActionBars().getStatusLineManager();
	}

	protected void onProgress(ProgressEvent event) {
		if(event.total == 0){
			return;
		}
		int ratio = event.current*100/event.total;
		
		statusLine.getProgressMonitor().worked(ratio);
	}


	protected void locChanging(LocationEvent event) {
		// TODO Auto-generated method stub
		
	}


	protected void locChanged(LocationEvent event) {
		urlCombo.setText(event.location);
	}


	@Override
	public void setFocus() {
		browser.setFocus();
	}

	private Control createComboView(Composite parent, Object layoutData){
		urlCombo = new Combo(parent, SWT.NONE);
		urlCombo.setLayoutData(layoutData);
		urlCombo.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e){
				final String url = ((Combo) e.getSource()).getText();
				browser.setUrl(url);
				urlCombo.add(url);
			}
			
			public void widgetSelected(SelectionEvent e){
				browser.setUrl(((Combo)e.getSource()).getText());
			}
		});
		return urlCombo;
	}
	
	private void calcSize(CoolItem item){
		Control control = item.getControl();
		Point pt = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		pt = item.computeSize(pt.x, pt.y);
		item.setSize(pt);
	}
	
	private void makeActions(){
		actionBack = new Action(){
			public void run(){
				browser.back();
			}
		};
		actionBack.setText("后退");
		actionBack.setToolTipText("后退");
		actionBack.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_BACK));

		actionForward = new Action(){
			public void run(){
				browser.forward();
			}
		};
		actionForward.setText("前进");
		actionForward.setToolTipText("前进");
		actionForward.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(
						ISharedImages.IMG_TOOL_FORWARD));
		
		actionHome = new Action(){
			public void run(){
				browser.setUrl(startUrl);
			}
		};
		actionHome.setText("主页");
		actionHome.setToolTipText("主页");
		actionHome.setImageDescriptor(ICON_HOME);
		
		actionAddBookmark = new Action(){
			public void run(){
				addBookmark(urlCombo.getText());
			}
		};
		actionAddBookmark.setText("书签");
		actionAddBookmark.setToolTipText("加为书签");
		actionAddBookmark.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE));
		
	}
	
	private void contributeToActionBars(){
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	private void fillLocalToolBar(IToolBarManager manager){
		manager.add(actionHome);
		manager.add(actionBack);
		manager.add(actionForward);
		manager.add(actionAddBookmark);
		
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalPullDown(IMenuManager manager){
		manager.add(actionAddBookmark);
		
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void addBookmark(final String url){
		BookMarksView v = (BookMarksView)Activator.getView(getViewSite().getWorkbenchWindow(),BookMarksView.ID_VIEW);
		if(v != null){
			v.addBookmark(url);
		}
	}
	
	public void navigateTo(String url){
		browser.setUrl(url);
	}
	public void dispose() {
		 browser.dispose();
		 super.dispose();
	 }
}
