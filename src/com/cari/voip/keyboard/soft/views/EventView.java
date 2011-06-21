package com.cari.voip.keyboard.soft.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;
import com.cari.voip.keyboard.stack.CCKPConnection;
import com.cari.voip.keyboard.stack.ConnectionListener;
import com.cari.voip.keyboard.stack.events.Packet;
import com.cari.voip.keyboard.stack.events.ReplyListener;
import com.cari.voip.keyboard.stack.events.TrapEventListener;

public class EventView extends ViewPart {
	public static final String ID_VIEW = 
		"com.cari.voip.keyboard.soft.views.EventView";
	//private TableViewer tableViewer;
	private Table table;
	
	//private List revealListeners = null;
	
	private TrapEventListener listener;
	
	private final int MaxItemsNum = 50;
	private ReplyListener replyListener;
	
	public EventView() {
		super();
		this.listener = new TrapEventListener(){
			public void processTrap(final Packet event){
				//revealListeners.add(event);
				Activator.getDisplay().asyncExec(new Runnable() {
				      public void run() {
				    	  if(table != null && event != null){
				    		  	String[] des = event.getDiscription();
				    		  	if(des != null){
								if(table.getItemCount() > MaxItemsNum){
									table.remove(0);
								}
								TableItem row = new TableItem(table,SWT.NONE);
								
								row.setText(des);
								
								table.setTopIndex(table.getItemCount()-1);
				    		  	}
							}
					}
				   });
			}
		};
		this.replyListener = new ReplyListener(){
			public void processReply(final Packet command,final Packet reply){
				Activator.getDisplay().asyncExec(new Runnable() {
				      public void run() {
				    	  if(reply != null && table != null){
				    		  String[] des = reply.getDiscription();
				    		  	if(des != null){
								if(table.getItemCount() > MaxItemsNum){
									table.remove(0);
								}
								TableItem row = new TableItem(table,SWT.NONE);
								row.setText(des);
								table.setTopIndex(table.getItemCount()-1);
				    		  	}
							}
					}
				   });
			}
		};
		
		//revealListeners = new ArrayList(1);
	}

	@Override
	public void createPartControl(Composite parent) {
		//parent.setLayout(new GridLayout(1,true));
		
		createTableViewer(parent);

		attachListener();
	}

	private void attachListener() {
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null){
			CCKPConnection connection = session.getConnection();
			if(connection != null){
				
				connection.addTrapEventListeners(this.listener, null);
				connection.addReplyListeners(this.replyListener, null);
				
			}
		}
	}

	private void detachListener(){
		 SwitchUsersSession session = Activator.getSwitchUsersSession();
			if(session != null){
				CCKPConnection connection = session.getConnection();
				if(connection != null){
					connection.removeReplyListeners(replyListener);
					connection.removeTrapEventListeners(listener);
				}
			}
	}
	
	private void createTableViewer(Composite parent) {
		
		table = new Table(parent,
				SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		//table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		/*table.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (!revealListeners.isEmpty()) {
					// Go through the reveal list and let everyone know that the
					// view is now available. Remove the listeners so they are
					// only
					// called once!
					Iterator iterator = revealListeners.iterator();
					while (iterator.hasNext()) {
						Packet event = (Packet) iterator.next();
						if(event != null && table != null){
							if(table.getItemCount() > MaxItemsNum){
								table.remove(0);
							}
							TableItem row = new TableItem(table,SWT.NONE);
							row.setText(event.getDiscription());
							table.setTopIndex(table.getItemCount()-1);
						}
						iterator.remove();
					}
				}
				
			}
		});
		*/
		//table.setTopIndex(5);
		
		TableColumn  colEventType = new TableColumn(this.table,SWT.NONE);
		colEventType.setText("      ");
		//colEventType.setWidth(this.table.getSize().x/4);
		
		TableColumn  colEventContent = new TableColumn(this.table,SWT.NONE);
		colEventContent.setText("      ");
		//colEventContent.setWidth(this.table.getSize().x/4);
		
		
		TableColumn  colEventDiscription = new TableColumn(this.table,SWT.NONE);
		colEventDiscription.setText("            ");
		//colEventDiscription.setWidth(this.table.getSize().x-colEventType.getWidth()-colEventContent.getWidth()-1);
		
		
		colEventType.pack();
		colEventContent.pack();
		colEventDiscription.pack();
		
		/*TableItem row = new TableItem(table,SWT.NONE);
		row.setText(new String[]{"trap","1000注册"});
		for(int i = 0;i<60;i++){
			if(this.table.getItemCount() > this.MaxItemsNum){
				this.table.remove(0);
			}
			row = new TableItem(table,SWT.NONE);
			//row.setText(new String[]{"replyreplyreply"+String.valueOf(i),"调度ok"});
			row.setText("调度ok");
		}
		
		table.setTopIndex(table.getItemCount()-1);*/
		
		
		
		
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		detachListener();
		 super.dispose();
		
	 }
}
