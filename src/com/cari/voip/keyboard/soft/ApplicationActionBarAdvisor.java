package com.cari.voip.keyboard.soft;

//import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
//import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.cari.voip.keyboard.soft.actions.*;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    private IWorkbenchAction exitAction;
    private IWorkbenchAction aboutAction;
    private IWorkbenchAction aboutus;
   // private IContributionItem views;
    private IWorkbenchAction refresh;
    private IWorkbenchAction reconnect;
    private IWorkbenchAction disconnect;
    private IWorkbenchAction open;
    private FullScreenAction screen;
    
    protected void makeActions(IWorkbenchWindow window) {
    	//exitAction = ActionFactory.QUIT.create(window);
    	exitAction = new ExitAction(window);
    	exitAction.setText("退出");
    	exitAction.setToolTipText("断开，并退出");
    	exitAction.setImageDescriptor(
    			Activator.getImageDescriptor("icons/delete_obj.gif"));
    	register(exitAction);
    	
    	aboutAction = ActionFactory.ABOUT.create(window);
    	aboutAction.setText("关于");
    	aboutAction.setToolTipText("关于");
    	aboutAction.setImageDescriptor(
    			Activator.getImageDescriptor("icons/e_home.gif"));
    	register(aboutAction);
    	//views = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
    	
    	open = new OpenAction(window);
    	open.setText("打开");
    	open.setToolTipText("打开");
    	open.setImageDescriptor(Activator.getImageDescriptor("icons/e_index_view.gif"));
    	
    	register(open);
    	
    	refresh = new RefreshAction();
    	refresh.setText("刷新");
    	refresh.setToolTipText("刷新用户信息");
    	refresh.setImageDescriptor(
				Activator.getImageDescriptor("icons/refresh_remote(1).gif"));
    	refresh.setDisabledImageDescriptor(
				Activator.getImageDescriptor("icons/refresh_remote.gif"));
    	register(refresh);
    	
    	reconnect = new ReconnectAction(window);
    	reconnect.setText("连接");
    	
    	reconnect.setToolTipText("断开当前连接，并重新连接");
    	reconnect.setImageDescriptor(
				Activator.getImageDescriptor("icons/osprc_obj.gif"));//48.png
    	register(reconnect);
    	
    	disconnect = new DisconnectAction(window);
    	disconnect.setText("断开");
    	disconnect.setToolTipText("断开");
    	disconnect.setImageDescriptor(
				Activator.getImageDescriptor("icons/osprct_obj.gif"));
		
    	register(disconnect);
    	
    	aboutus = new AboutusAction(window);
    	aboutus.setText("关于");
    	aboutus.setToolTipText("关于");
    	aboutus.setImageDescriptor(
				Activator.getImageDescriptor("icons/e_home.gif"));
    	register(aboutus);
    	
    	screen = new FullScreenAction(window);
    	//screen.checkScreen();
    	screen.setText("全屏");
    	screen.setToolTipText("设置全屏显示模式");
    	screen.setImageDescriptor(Activator.getImageDescriptor("icons/full.gif"));
    	register(screen);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	MenuManager ctrMenu = new MenuManager("&程序","程序");
    	//ctrMenu.add(refresh);
    	
    	//ctrMenu.add(new Separator());
    	ctrMenu.add(reconnect);
    	//ctrMenu.add(disconnect);
    	//ctrMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    	ctrMenu.add(new Separator());
    	//ctrMenu.add(open);
    	ctrMenu.add(exitAction);
    	menuBar.add(ctrMenu);
    	
    	MenuManager windowMenu = new MenuManager("&窗口","窗口");
    	//windowMenu.add(views);
    	menuBar.add(windowMenu);
    	
    	MenuManager helpMenu = new MenuManager("&帮助","帮助");
    	helpMenu.add(aboutus);
    	menuBar.add(helpMenu);
    	
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
    	
    	IToolBarManager toolbarProgram = new myToolBarManager(coolBar.getStyle()|SWT.BOTTOM);
    	

    	
    	ActionContributionItem aboutusCI = new ActionContributionItem(this.aboutus);
    	aboutusCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);

    	toolbarProgram.add(aboutusCI);
    	
    	ActionContributionItem screenCI = new ActionContributionItem(this.screen);
    	screenCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
    	
    	toolbarProgram.add(screenCI);
    	
    	
    	//toolbarProgram.add(exitAction);
    	ActionContributionItem reconnectCI = new ActionContributionItem(this.reconnect);
    	reconnectCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
    	
    	toolbarProgram.add(reconnectCI);
    	
    	

    	ActionContributionItem addContactCI = new ActionContributionItem(this.exitAction);
    	addContactCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
    	
    	//ActionContributionItem.setUseColorIconsInToolbars(true);
    	toolbarProgram.add(addContactCI);

    	

    	//toolbarProgram.add(this.exitAction);
    	coolBar.add(toolbarProgram);
    	
    	
        //IToolBarManager toolbarConnection = new ToolBarManager(coolBar.getStyle()|SWT.BOTTOM);
       // toolbarConnection.add(reconnect);
        
        //toolbarConnection.add(disconnect);
       // toolbarConnection.add(new Separator());
        //coolBar.add(toolbarConnection);
        
        
        //IToolBarManager toolbarStatus = new ToolBarManager(coolBar.getStyle());
        //toolbarStatus.add(refresh);
        //coolBar.add(toolbarStatus);
        
        
     }
    
    
}
