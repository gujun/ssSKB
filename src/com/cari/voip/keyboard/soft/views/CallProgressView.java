package com.cari.voip.keyboard.soft.views;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
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
import com.cari.voip.keyboard.stack.events.PacketFilter;
import com.cari.voip.keyboard.stack.events.TrapEventListener;

public class CallProgressView extends ViewPart {
	public static final String ID_VIEW = 
		"com.cari.voip.keyboard.soft.views.CallProgressView";
	private Table table;
	private Timer timer = new Timer();
	private TimerTask ttask;
	private boolean isTimerRunning;
	final Object lock = new Object();
	//private List revealListeners = null;
	
	private TrapEventListener progresslistener;
	private TrapEventListener answerlistener;
	private TrapEventListener hanguplistener;
	
	private ConnectionListener connectionListener;
	//private final int MaxItemsNum = 50;
	private boolean isRestarted;
	public CallProgressView(){
		super();
		isRestarted = false;
		isTimerRunning = false;
		this.ttask = new TimerTask(){
			public void run(){
				Activator.getDisplay().asyncExec(new Runnable() {
					 public void run() {
						 refreshTime();
					 }
				});
				
			}
		};
		this.hanguplistener = new TrapEventListener(){
			public void processTrap(final Packet event){
				//revealListeners.add(event);
				Activator.getDisplay().asyncExec(new Runnable() {
				      public void run() {
				    	  if(table != null && event != null){
				    		  String caller_id_number = (String) event.getProperty("caller_id_number");
				    		  	String caller_id_name = (String) event.getProperty("caller_id_name");
				    		  	String uuid = (String) event.getProperty("uuid");
				    		  	if(caller_id_number != null && uuid != null){
				    		  		synchronized(lock) {
				    		  		int rows = table.getItemCount();
				    		  		int idx = -1;
				    		  		TableItem item;
				    		  		for(int i = 0; i< rows; i++){
				    		  			 item = table.getItem(i);
				    		  			
				    		  			if(item.getText(0).equals(uuid)){
				    		  				idx = i;
				    		  			}
				    		  		}
				    		  		if(idx >=0){
				    		  			table.remove(idx);
				    		  		}
				    		  		idx = table.getItemCount();
				    		  		if(idx > 0){
				    		  			table.setTopIndex(table.getItemCount()-1);
				    		  			
				    		  		}
				    		  		}
				    		  		
				    		  	}
				    		  	
							}
					}
				   });
			}
		};
		this.answerlistener = new TrapEventListener(){
			public void processTrap(final Packet event){
				//revealListeners.add(event);
				Activator.getDisplay().asyncExec(new Runnable() {
				      public void run() {
				    	  
				    	  if(table != null && event != null){
				    		  String caller_id_number = (String) event.getProperty("caller_id_number");
				    		  	String caller_id_name = (String) event.getProperty("caller_id_name");
				    		  	String uuid = (String) event.getProperty("uuid");
				    		  	if(caller_id_number != null && uuid != null){
				    		  		synchronized(lock) {
				    		  		int rows = table.getItemCount();
				    		  		int idx = -1;
				    		  		TableItem item =null;
				    		  	
				    		  		for(int i = 0; i< rows; i++){
				    		  			 item = table.getItem(i);
				    		  			
				    		  			if(item.getText(0).equals(uuid)){
				    		  				idx = i;
				    		  			}
				    		  		}
				    		  		if(idx >=0){
				    		  			item = table.getItem(idx);
				    		  			item.setText(2, "通话");
				    		  			item.setText(3, "0");
				    		  		}
				    		  		else{
				    		  			TableItem row = new TableItem(table,SWT.NONE);
										
										row.setText(new String[]{uuid,caller_id_number,"通话","0"});
				    		  		}
								
				    		  		idx = table.getItemCount();
				    		  		if(idx > 0){
				    		  			table.setTopIndex(table.getItemCount()-1);
				    		  			if(!isTimerRunning){
				    		  				isTimerRunning= true;
				    		  				//ttask.cancel();
				    		  				timer.scheduleAtFixedRate(ttask, 1000, 1000);
				    		  				
				    		  			}
				    		  		}
				    		  		}
				    		  	}
				    		  	
				    		  	}

					}
				   });
			}
		};
	
		this.progresslistener = new TrapEventListener(){
			public void processTrap(final Packet event){
				//revealListeners.add(event);
				Activator.getDisplay().asyncExec(new Runnable() {
				      public void run() {
				    	  
				    	  if(table != null && event != null){
				    		  String caller_id_number = (String) event.getProperty("caller_id_number");
				    		  	String caller_id_name = (String) event.getProperty("caller_id_name");
				    		  	String uuid = (String) event.getProperty("uuid");
				    		  	if(caller_id_number != null && uuid != null){
				    		  		synchronized(lock) {
				    		  		int rows = table.getItemCount();
				    		  		int idx = -1;
				    		  		TableItem item =null;
				    		  	
				    		  		for(int i = 0; i< rows; i++){
				    		  			 item = table.getItem(i);
				    		  			
				    		  			if(item.getText(0).equals(uuid)){
				    		  				idx = i;
				    		  			}
				    		  		}
				    		  		if(idx >=0){
				    		  			item = table.getItem(idx);
				    		  			item.setText(2, "振铃");
				    		  			item.setText(3, "0");
				    		  		}
				    		  		else{
				    		  			TableItem row = new TableItem(table,SWT.NONE);
										
										row.setText(new String[]{uuid,caller_id_number,"振铃","0"});
				    		  		}
								
				    		  		idx = table.getItemCount();
				    		  		if(idx > 0){
				    		  			table.setTopIndex(table.getItemCount()-1);
				    		  			if(!isTimerRunning){
				    		  				isTimerRunning= true;
				    		  				//ttask.cancel();
				    		  				timer.scheduleAtFixedRate(ttask, 1000, 1000);
				    		  				
				    		  			}
				    		  		}
				    		  		}
				    		  	}
				    		  	
				    		  	}

					}
				   });
			}
		};
		
		this.connectionListener = new ConnectionListener(){
			//connectionListener = new ConnectionListener(){
				 public void connectionClosed(){
					 Activator.getDisplay().asyncExec(new Runnable() {
					      public void run() {
					    	  	restartApp();
					      }
					 });
				 }
				 
				 public void connectionClosedOnError(Exception e){
					 Activator.getDisplay().asyncExec(new Runnable() {
					      public void run() {
					    	  	restartApp();
					      }
					 });
				 }
				 public void socketConnectSuccessful(){
					
				 }
				 public void authenticationSuccessful(){
					
				 }
			};
			
	}
	@Override
	public void createPartControl(Composite parent) {
		createTableViewer(parent);

		attachListener();

	}
private void createTableViewer(Composite parent) {
		
		table = new Table(parent,
				SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		//table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		
		TableColumn  uuid = new TableColumn(this.table,SWT.NONE);
		uuid.setText("");
		//colEventType.setWidth(this.table.getSize().x/4);
		
		TableColumn   number = new TableColumn(this.table,SWT.NONE);
		number.setText("  号码  ");
		//colEventContent.setWidth(this.table.getSize().x/4);
		
		
		TableColumn  name = new TableColumn(this.table,SWT.NONE);
		name.setText("  状态  ");
		//colEventDiscription.setWidth(this.table.getSize().x-colEventType.getWidth()-colEventContent.getWidth()-1);
		TableColumn  time = new TableColumn(this.table,SWT.NONE);
		time.setText("时间（秒）");
		
		number.pack();
		name.pack();
		time.pack();
		
		
	}

	protected void refreshTime() {
		int rows = -1;
		TableItem item = null;
		synchronized (lock) {
			if (this.isTimerRunning) {
				try {
					if (table != null && (rows = table.getItemCount()) > 0) {
						for (int i = 0; i < rows; i++) {
							item = table.getItem(i);
							int t = 0;
							String tm = item.getText(3);

							t = Integer.parseInt(tm);
							t++;
							item.setText(3, String.valueOf(t));

						}
					}
				} catch (Exception e) {

				}
			}
		}
	}
	private void attachListener() {
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null){
			CCKPConnection connection = session.getConnection();
			if(connection != null){
				connection.addConnectionListener(this.connectionListener);
				connection.addTrapEventListeners(this.progresslistener, new PacketFilter(){
					public boolean accept(Packet packet){
						boolean ret = false;
						if(packet.getPacketType() == Packet.TYPE_TRAP_CALL_DISPATCHER_PROGRESS){
							ret = true;
						}
						return ret;
					}
				});
				connection.addTrapEventListeners(this.answerlistener, new PacketFilter(){
					public boolean accept(Packet packet){
						boolean ret = false;
						if(packet.getPacketType() == Packet.TYPE_TRAP_CALL_DISPATCHER_ANSWER){
							ret = true;
						}
						return ret;
					}
				});
				connection.addTrapEventListeners(this.hanguplistener, new PacketFilter(){
					public boolean accept(Packet packet){
						boolean ret = false;
						if(packet.getPacketType() == Packet.TYPE_TRAP_CALL_DISPATCHER_HANGUP){
							ret = true;
						}
						return ret;
					}
				});
				
				if(!connection.isConnect()){
					restartApp();
				}
			}
		}
	}
	private void detachListener() {
		SwitchUsersSession session = Activator.getSwitchUsersSession();
		if(session != null){
			CCKPConnection connection = session.getConnection();
			if(connection != null){
				connection.removeTrapEventListeners(progresslistener);
				connection.removeTrapEventListeners(answerlistener);
				connection.removeTrapEventListeners(hanguplistener);
				
				connection.removeConnectionListener(connectionListener);
			}
		}
	}
	protected void restartApp(){
		if(isRestarted)
			return;
		isRestarted = true;
		try{
			synchronized(lock) {
			if(this.timer != null){
				 this.timer.cancel();
				 this.isTimerRunning = false;
			 }
			}
			
			this.getSite().getPage().getWorkbenchWindow().getWorkbench().restart();
			
		}catch(Exception e){
			
		}
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		if(this.table != null){
			this.table.setFocus();
		}
	}
	public void dispose() {
		
		synchronized(lock) {
		 if(this.timer != null){
			 this.timer.cancel();
			 this.isTimerRunning = false;
		 }
		}
		detachListener();
		 super.dispose();
		
		 
	 }
}
