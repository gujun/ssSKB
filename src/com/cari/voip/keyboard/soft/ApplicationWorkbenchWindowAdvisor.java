package com.cari.voip.keyboard.soft;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.window.WindowManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.FastViewBar;
import org.eclipse.ui.internal.PerspectiveSwitcher;
import org.eclipse.ui.internal.WindowTrimProxy;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.internal.WorkbenchWindowConfigurer;
import org.eclipse.ui.internal.layout.CacheWrapper;
import org.eclipse.ui.internal.layout.IWindowTrim;
import org.eclipse.ui.internal.layout.TrimLayout;
import org.eclipse.ui.internal.menus.TrimBarManager2;
import org.eclipse.ui.internal.menus.TrimContributionManager;
import org.eclipse.ui.internal.progress.ProgressRegion;
import org.eclipse.ui.internal.util.PrefUtil;
import org.sf.feeling.swt.win32.internal.extension.util.ColorCache;

@SuppressWarnings("restriction")
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private TrimLayout defaultLayout ;
	private Composite pageComposite ;
	private WorkbenchWindowConfigurer configurer;
	private WorkbenchWindow window;
	private CBanner topBar;
	private WindowTrimProxy topBarTrim;
	private FastViewBar fastViewBar;
	private WindowTrimProxy statusLineTrim ;
	private ProgressRegion progressRegion;
	private int barX=0,barY=0;
	//private FontData fd;// = new FontData();
	 //fd.height = barY-6;
	 //fd.setHeight(barY-6);
	private Font newFont;// = new Font(getDisplay(), "Default", 10, SWT.NORMAL); // = new Font(gc.getDevice(),fd);
    private String headerText; //= Activator.getHeaderText();
    private Dimension headerTextDimension;
    private Shell shell;
    private Rectangle barRect;
    private Rectangle toolRect;
    private int X1,X2,Y1,Y2;
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
        
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        
    	Rectangle rect = Display.getCurrent().getPrimaryMonitor().getClientArea();
        configurer.setInitialSize(new Point(rect.width, rect.height));
        
        configurer.setShowMenuBar(false);
        configurer.setShowCoolBar(true);
        configurer.setShowPerspectiveBar(true);
        configurer.setShowStatusLine(false);
        
        configurer.setTitle("IP电话调度通信系统控制台");
        
    }
    @SuppressWarnings("restriction")
	public void createWindowContents(final Shell shell){
    	//Shell windowShell = new Shell(shell,shell.getStyle());
    	/*defaultLayout = new TrimLayout();
		shell.setLayout(defaultLayout);

		Menu menuBar = getMenuBarManager().createMenuBar(shell);
		if (getWindowConfigurer().getShowMenuBar()) {
			shell.setMenuBar(menuBar);
		}

		// Create the CBanner widget which parents both the Coolbar
		// and the perspective switcher, and supports some configurations
		// on the left right and bottom
		topBar = new CBanner(shell, SWT.NONE);
		topBarTrim = new WindowTrimProxy(topBar,
				"org.eclipse.ui.internal.WorkbenchWindow.topBar", //$NON-NLS-1$  
				WorkbenchMessages.TrimCommon_Main_TrimName, SWT.NONE, true);

		// the banner gets a curve along with the new tab style
		// TODO create a dedicated preference for this
		setBannerCurve(PrefUtil.getAPIPreferenceStore().getBoolean(
				IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));

		CacheWrapper coolbarCacheWrapper = new CacheWrapper(topBar);

		final Control coolBar = createCoolBarControl(coolbarCacheWrapper
				.getControl());
		// need to resize the shell, not just the coolbar's immediate
		// parent, if the coolbar wants to grow or shrink

		coolBar.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				// If the user is dragging the sash then we will need to force
				// a resize. However, if the coolbar was resized programatically
				// then everything is already layed out correctly. There is no
				// direct way to tell the difference between these cases,
				// however
				// we take advantage of the fact that dragging the sash does not
				// change the size of the shell, and only force another layout
				// if the shell size is unchanged.
				Rectangle clientArea = shell.getClientArea();

				if (lastShellSize.x == clientArea.width
						&& lastShellSize.y == clientArea.height) {
					LayoutUtil.resize(coolBar);
				}

				lastShellSize.x = clientArea.width;
				lastShellSize.y = clientArea.height;
			}
		});

		if (getWindowConfigurer().getShowCoolBar()) {
			topBar.setLeft(coolbarCacheWrapper.getControl());
		}

		createStatusLine(shell);

		fastViewBar = new FastViewBar(this);
		fastViewBar.createControl(shell);

		if (getWindowConfigurer().getShowPerspectiveBar()) {
			addPerspectiveBar(perspectiveBarStyle());
			perspectiveSwitcher.createControl(shell);
		}

		createProgressIndicator(shell);

		if (getShowHeapStatus()) {
			createHeapStatus(shell);
		}
		
		// Insert any contributed trim into the layout
		// Legacy (3.2) trim
		trimMgr2 = new TrimBarManager2(this);
		
		// 3.3 Trim contributions
		trimContributionMgr = new TrimContributionManager(this);
		
		trimDropTarget = new TrimDropTarget(shell, this);
		DragUtil.addDragTarget(shell, trimDropTarget);
		DragUtil.addDragTarget(null, trimDropTarget);

		// Create the client composite area (where page content goes).
		createPageComposite(shell);

		setLayoutDataForContents();
		// System.err.println(defaultLayout.displayTrim());*/
    	this.shell = shell;
    	 configurer = (WorkbenchWindowConfigurer)getWindowConfigurer();
    	configurer.createDefaultContents(shell);
    	
    	 window = (WorkbenchWindow)configurer.getWindow();
    	
    	 //window.setWindowManager(new WindowManager());
    	
    	/*
    	defaultLayout = new TrimLayout();
		shell.setLayout(defaultLayout);
		

		// Create the CBanner widget which parents both the Coolbar
		// and the perspective switcher, and supports some configurations
		// on the left right and bottom
		 topBar= new CBanner(shell, SWT.NONE);
		  topBarTrim= new WindowTrimProxy(topBar,
				"org.eclipse.ui.internal.WorkbenchWindow.topBar", //$NON-NLS-1$  
				WorkbenchMessages.TrimCommon_Main_TrimName, SWT.NONE, true);

		// the banner gets a curve along with the new tab style
		// TODO create a dedicated preference for this
		//window.setBannerCurve(PrefUtil.getAPIPreferenceStore().getBoolean(
		//	IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
		topBar.setSimple(PrefUtil.getAPIPreferenceStore().getBoolean(
				IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS));
		
		CacheWrapper coolbarCacheWrapper = new CacheWrapper(topBar);
		final Control coolBar = configurer.createCoolBarControl(coolbarCacheWrapper
				.getControl());
		
		// need to resize the shell, not just the coolbar's immediate
		// parent, if the coolbar wants to grow or shrink

		

		if (configurer.getShowCoolBar()) {
			topBar.setLeft(coolbarCacheWrapper.getControl());
		}

		configurer.createStatusLineControl(shell);//createStatusLine(shell);

		 fastViewBar= new FastViewBar(window);
		fastViewBar.createControl(shell);
		
		if (configurer.getShowPerspectiveBar()) {
			PerspectiveSwitcher perspectiveSwitcher = new PerspectiveSwitcher(window, topBar, SWT.FLAT | SWT.WRAP | SWT.RIGHT | SWT.HORIZONTAL);
			perspectiveSwitcher.createControl(shell);
		}

		if (configurer.getShowProgressIndicator()) {
			progressRegion= new ProgressRegion();
			progressRegion.createContents(shell, window);
		}
		//window.createProgressIndicator(shell);

		
		pageComposite = (Composite)configurer.createPageComposite(shell);
		//window.createPageComposite(shell);
		setLayoutDataForContents();
		*/
		
    }
    @SuppressWarnings("restriction")
	private void setLayoutDataForContents() {
    	if (defaultLayout == null) {
			return;
		}

		// @issue this is not ideal; coolbar and perspective shortcuts should be
		// separately configurable
		String perspectiveBarOnTheLeftString = PlatformUI.getPreferenceStore()
				.getString(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR);
		boolean perspectiveBarOnTheLeft = perspectiveBarOnTheLeftString != null
				&& perspectiveBarOnTheLeftString
						.equalsIgnoreCase(IWorkbenchPreferenceConstants.LEFT);

		if (configurer.getShowCoolBar()
				|| (configurer.getShowPerspectiveBar() && !perspectiveBarOnTheLeft)) {
			if (defaultLayout.getTrim(topBarTrim.getId()) == null) {
				defaultLayout.addTrim(SWT.TOP, topBarTrim);
			}
			topBar.setVisible(true);
		} else {
			defaultLayout.removeTrim(topBarTrim);
			topBar.setVisible(false);
		}

		if (fastViewBar != null) {
			if (configurer.getShowFastViewBars()) {
				int side = fastViewBar.getSide();

				if (defaultLayout.getTrim(fastViewBar.getId()) == null) {
					defaultLayout.addTrim(side, fastViewBar);
				}
				fastViewBar.getControl().setVisible(true);
			} else {
				defaultLayout.removeTrim(fastViewBar);
				fastViewBar.getControl().setVisible(false);
			}
		}
		
		if (configurer.getShowStatusLine()) {
			if (defaultLayout.getTrim(getStatusLineTrim().getId()) == null) {
				defaultLayout.addTrim(SWT.BOTTOM, getStatusLineTrim());
			}
			window.getStatusLineManager().getControl().setVisible(true);
		} else {
			defaultLayout.removeTrim(getStatusLineTrim());
			window.getStatusLineManager().getControl().setVisible(false);
		}

		/*if (heapStatus != null) {
			if (getShowHeapStatus()) {
				if (heapStatus.getLayoutData() == null) {
					heapStatusTrim.setWidthHint(heapStatus.computeSize(
							SWT.DEFAULT, SWT.DEFAULT).x);
					heapStatusTrim
							.setHeightHint(getStatusLineManager().getControl()
									.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
				}

				if (defaultLayout.getTrim(heapStatusTrim.getId()) == null) {
					defaultLayout.addTrim(SWT.BOTTOM, heapStatusTrim);
				}
				heapStatus.setVisible(true);
			} else {

				defaultLayout.removeTrim(heapStatusTrim);
				heapStatus.setVisible(false);
			}
		}*/

		if (progressRegion != null) {
			if (configurer.getShowProgressIndicator()) {
				if (defaultLayout.getTrim(progressRegion.getId()) == null) {
					defaultLayout.addTrim(SWT.BOTTOM, progressRegion);
				}
				progressRegion.getControl().setVisible(true);
			} else {
				defaultLayout.removeTrim(progressRegion);
				progressRegion.getControl().setVisible(false);
			}
		}
		
		defaultLayout.setCenterControl(pageComposite);
	}

    @SuppressWarnings("restriction")
	private IWindowTrim getStatusLineTrim() {
		if (statusLineTrim == null) {
			statusLineTrim = new WindowTrimProxy(
					window.getStatusLineManager().getControl(),
					"org.eclipse.jface.action.StatusLineManager", //$NON-NLS-1$
					WorkbenchMessages.TrimCommon_StatusLine_TrimName, SWT.NONE,
					true);
		}
		return statusLineTrim;
	}
    private  void setToorbar(){
    	if(headerText == null){
    		headerText = Activator.getHeaderText();
    	}
        
    	Object[] childrens = this.getWindowConfigurer().getWindow().getShell().getChildren();
    	int n = childrens.length;
    	int c = -1;
    	for(int i = 0 ; i < n ; i++){
    		String className = childrens[i].getClass().getName();
    		if(className.endsWith("CBanner")){
    			c = i;
    			break;
    		}
    		/*if(className.endsWith("ToolBar")){
    			((Composite)childrens[i]).setBackground(ColorCache.getInstance().getColor(182, 206, 238));
    		}*/
    	}
    	if(c < 0){
    		return;
    	}
    	final CBanner ban = (CBanner)childrens[c];
		ban.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		ban.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		ban.setBackgroundMode(SWT.INHERIT_FORCE); 
		//ban.set(SWT.INHERIT_FORCE); 
		ban.addPaintListener(new PaintListener(){

			@Override
			public void paintControl(PaintEvent e) {
				 Object o = e.getSource();
				 if(!(o instanceof CBanner)){
					 return;
				 }
				 CBanner  me= (CBanner)o;
				 GC gc = e.gc; 
				 int x = me.getSize().x;
				 int y = me.getSize().y;
				 barX = x;
				 barY = y;
				 int oldLineWidth = gc.getLineWidth();
				 Color oldForeground = gc.getForeground();
				 gc.setLineWidth(x>y?x:y); 
				 gc.setForeground(Display.getDefault().getSystemColor( 
				 SWT.COLOR_BLACK)); 
				 gc.drawRectangle(0, 0, x, y); 
				 gc.setForeground(oldForeground);
				 gc.setLineWidth(oldLineWidth);
				 
				 gc.dispose(); 

			}
			
		});
		
	    Control left = ban.getLeft();
	    Control right = ban.getRight();
	    
		//String leftClassName = left.getClass().getName();
		if(!(left instanceof Composite)){
			return;
		}
		/*left.addPaintListener(new PaintListener(){

			@Override
			public void paintControl(PaintEvent e) {
				 GC gc = e.gc; 
				 
				 gc.setLineWidth(10); 
				 gc.setForeground(Display.getDefault().getSystemColor( 
				 SWT.COLOR_BLUE)); 
				 gc.drawRectangle(0, 0, left.getSize().x, left.getSize().y); 
				 gc.dispose(); 

			}
			
		});*/
		left.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		c = -1;
		childrens = ((Composite)left).getChildren();
		n = childrens.length;
		//Object[] ggChildrens = null;
		for(int i = 0 ; i < n ; i++){
    		String gclassName = childrens[i].getClass().getName();
    		if(gclassName.endsWith("CoolBar")){
    			c = i;
    			break;
    		}

    	}
		
		if(c < 0){
			return;
		}
		CoolBar coolbar = (CoolBar)childrens[c];
		//REMOVE COOLBAR DOT DOT DOT
		coolbar.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		coolbar.addPaintListener(new PaintListener(){

			@Override
			public void paintControl(PaintEvent e) {
				Object o = e.getSource();
				 if(!(o instanceof CoolBar)){
					 return;
				 }
				 CoolBar  me= (CoolBar)o;
				 int x = me.getSize().x;
				 int y = me.getSize().y;
				 
				 GC gc = e.gc; 
				 int oldLineWidth = gc.getLineWidth();
				 Color oldForeground = gc.getForeground();
				 
				 gc.setLineWidth(x>y?x:y); 
				 gc.setForeground(Display.getDefault().getSystemColor( 
				 SWT.COLOR_BLACK)); 
				 gc.drawRectangle(0, 0,x, y); 
				 gc.setForeground(oldForeground);
				 gc.setLineWidth(oldLineWidth);
				 gc.dispose(); 

			}
			
		});
		
		c = -1;
		childrens = ((Composite)coolbar).getChildren();
		n = childrens.length;
		for(int i = 0 ; i < n ; i++){
    		String gclassName = childrens[i].getClass().getName();
    		if(gclassName.endsWith("ToolBar")){
    			c = i;
    			break;
    		}

    	}
		
		if(c < 0){
			return;
		}

		ToolBar toolbar = (ToolBar)childrens[c];
		
		toolbar.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		//int toolbarStyle = toolbar.getStyle();
		//toolbarStyle = toolbarStyle | SWT.RIGHT;
		//toolbar.add
		//toolbar.add
		Listener[] ls = toolbar.getListeners(SWT.Paint);
		for(Listener l:ls){
			toolbar.removeListener(SWT.Paint, l);
		}
		toolbar.addPaintListener(new PaintListener(){

			@Override
			public void paintControl(PaintEvent e) {
				Object o = e.getSource();
				 if(!(o instanceof ToolBar)){
					 return;
				 }
				 ToolBar  me= (ToolBar)o;
				 int x = me.getSize().x;
				 int y = me.getSize().y;
				 
				 GC gc = e.gc; 
				 //gc.setLineWidth(x>y?x:y); 
				 Font oldFont = gc.getFont();
				 
				 if(newFont == null){
					 //fd  = new FontData();
					 //fd.setHeight(barY - 5);
					// newFont = new Font(gc.getDevice(),fd);
					 newFont = new Font(gc.getDevice(), "Default", barY, SWT.BOLD);
					 headerTextDimension =  FigureUtilities.getTextExtents(headerText, newFont);
					 barRect = shell.getDisplay().map(ban.getParent(),null,ban.getBounds());
					 toolRect = shell.getDisplay().map(me.getParent(),null,me.getBounds());
					 Y1 = toolRect.y - barRect.y;
					 X1 = toolRect.x - barRect.x;
					 Y2 = barRect.height - toolRect.height - Y1;
					 X2 = barRect.width - toolRect.width - X1;
				 }
				 //int oldLineWidth = gc.getLineWidth();
				 Color oldForeground = gc.getForeground();
				 gc.setForeground(ColorCache.getInstance().getColor(255, 255, 0));
				 //Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW)); 
				 gc.setFont(newFont);
				// gc.drawString(string, x, y);
				 
				 
				 gc.drawText(headerText, (barX-headerTextDimension.width)/2-X1, ((barY-headerTextDimension.height)/2)-Y1, true);
				 //gc.drawRectangle(0, 0,x, y); 
				 gc.setFont(oldFont);
				 //newFont.dispose();
				 gc.setForeground(oldForeground);
				 
				 //gc.dispose(); 

			}
			
		});
		for(Listener l:ls){
			toolbar.addListener(SWT.Paint, l);
		}
		//toolbar.update();
		/*c = -1;
		childrens = ((ToolBar)toolbar).getItems();
		n = childrens.length;
		for(int i = 0 ; i < n ; i++){
    		String gclassName = childrens[i].getClass().getName();
    		if(gclassName.endsWith("ToolItem")){
    			ToolItem item = (ToolItem)childrens[i];
    			//item.getControl().setForeground(ColorCache.getInstance().getColor(255, 255, 255));
    		}

    	}
		*/
    }
	public void postWindowCreate(){
    	super.postWindowCreate();
    	//this.getWindowConfigurer().getWindow().getShell().setFullScreen(true);
    	this.getWindowConfigurer().getWindow().getShell().setMaximized(true);
    	
    	//window.getShell().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
    	//window.getShell().setBackgroundMode(SWT.INHERIT_FORCE); 
    	PlatformUI.getWorkbench().getDisplay().addFilter(SWT.MenuDetect,new Listener(){

			@Override
			public void handleEvent(Event event) {
				if(event.widget instanceof ToolBar){
					//event.x = event.y = -1000;
					for(Listener listener:event.widget.getListeners(SWT.MenuDetect)){
						event.widget.removeListener(SWT.MenuDetect, listener);
					}
					
				}
			}
    		
    	});
    	PlatformUI.getWorkbench().getDisplay().addFilter(SWT.Paint,new Listener(){

			@Override
			public void handleEvent(Event event) {
				
				if(event.widget instanceof ToolItem ||
						event.widget instanceof ToolBar){
					GC gc = event.gc;
					gc.setForeground(ColorCache.getInstance().getColor(255, 255, 255));
					//gc.setBackground(ColorCache.getInstance().getColor(255, 255, 255));
				}
			}
    		
    	});
    	
    	setToorbar();
    	//this.getWindowConfigurer().getWindow().getShell().redraw();
    }
}
