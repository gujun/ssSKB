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
    	exitAction.setText("�˳�");
    	exitAction.setToolTipText("�Ͽ������˳�");
    	exitAction.setImageDescriptor(
    			Activator.getImageDescriptor("icons/delete_obj.gif"));
    	register(exitAction);
    	
    	aboutAction = ActionFactory.ABOUT.create(window);
    	aboutAction.setText("����");
    	aboutAction.setToolTipText("����");
    	aboutAction.setImageDescriptor(
    			Activator.getImageDescriptor("icons/e_home.gif"));
    	register(aboutAction);
    	//views = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
    	
    	open = new OpenAction(window);
    	open.setText("��");
    	open.setToolTipText("��");
    	open.setImageDescriptor(Activator.getImageDescriptor("icons/e_index_view.gif"));
    	
    	register(open);
    	
    	refresh = new RefreshAction();
    	refresh.setText("ˢ��");
    	refresh.setToolTipText("ˢ���û���Ϣ");
    	refresh.setImageDescriptor(
				Activator.getImageDescriptor("icons/refresh_remote(1).gif"));
    	refresh.setDisabledImageDescriptor(
				Activator.getImageDescriptor("icons/refresh_remote.gif"));
    	register(refresh);
    	
    	reconnect = new ReconnectAction(window);
    	reconnect.setText("����");
    	
    	reconnect.setToolTipText("�Ͽ���ǰ���ӣ�����������");
    	reconnect.setImageDescriptor(
				Activator.getImageDescriptor("icons/osprc_obj.gif"));//48.png
    	register(reconnect);
    	
    	disconnect = new DisconnectAction(window);
    	disconnect.setText("�Ͽ�");
    	disconnect.setToolTipText("�Ͽ�");
    	disconnect.setImageDescriptor(
				Activator.getImageDescriptor("icons/osprct_obj.gif"));
		
    	register(disconnect);
    	
    	aboutus = new AboutusAction(window);
    	aboutus.setText("����");
    	aboutus.setToolTipText("����");
    	aboutus.setImageDescriptor(
				Activator.getImageDescriptor("icons/e_home.gif"));
    	register(aboutus);
    	
    	screen = new FullScreenAction(window);
    	//screen.checkScreen();
    	screen.setText("ȫ��");
    	screen.setToolTipText("����ȫ����ʾģʽ");
    	screen.setImageDescriptor(Activator.getImageDescriptor("icons/full.gif"));
    	register(screen);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	MenuManager ctrMenu = new MenuManager("&����","����");
    	//ctrMenu.add(refresh);
    	
    	//ctrMenu.add(new Separator());
    	ctrMenu.add(reconnect);
    	//ctrMenu.add(disconnect);
    	//ctrMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    	ctrMenu.add(new Separator());
    	//ctrMenu.add(open);
    	ctrMenu.add(exitAction);
    	menuBar.add(ctrMenu);
    	
    	MenuManager windowMenu = new MenuManager("&����","����");
    	//windowMenu.add(views);
    	menuBar.add(windowMenu);
    	
    	MenuManager helpMenu = new MenuManager("&����","����");
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
