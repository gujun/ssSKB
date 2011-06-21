package com.cari.voip.keyboard.soft.views.zest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;


import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.model.IWorkbenchAdapter;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;
import org.eclipse.zest.core.widgets.internal.GraphLabel;



import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.image.NodeImage;
import com.cari.voip.keyboard.soft.model.Presence;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchEntity;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;

public class SwitchUserGraphNode extends SwitchGraphNode implements Comparable,PropertyChangeListener {

	//private SwitchEntity model = null;
	private PropertyChangeListener presenceListener = null;
	private Presence newPresence;
	private Presence oldPresence;
	public SwitchUserGraphNode(SwitchEntity model,IContainer graphModel, int style) {
		this(model,graphModel, style, null);
	}

	public SwitchUserGraphNode(SwitchEntity model,IContainer graphModel, int style, Object data) {
		this(model,graphModel.getGraph(), style, "" /*text*/, null /*image*/, data);
	}

	public SwitchUserGraphNode(SwitchEntity model,IContainer graphModel, int style, String text) {
		this(model,graphModel, style, text, null);
	}

	public SwitchUserGraphNode(SwitchEntity model,IContainer graphModel, int style, String text, Object data) {
		this(model,graphModel.getGraph(), style, text, null /*image*/, data);
	}

	public SwitchUserGraphNode(SwitchEntity model,IContainer graphModel, int style, String text, Image image) {
		this(model,graphModel, style, text, image, null);
	}

	public SwitchUserGraphNode(SwitchEntity model,IContainer graphModel, int style, String text, Image image, Object data) {
		super(model,graphModel,style,text,image,data);
		
	}
	/*
	public SwitchEntity getModel(){
		
		return this.model;
	}
	
	protected IFigure createFigureForModel() {
		
		IFigure label =  super.createFigureForModel();
		label.setCursor(new Cursor(Activator.getDisplay(),SWT.CURSOR_HAND));
		return label;
	}
	

	public void dispose(){
		if(this.model != null &&
				this.presenceListener != null){
			this.model.removePropertyChangeListener(this);
		}
		super.dispose();
	}
	
	public void setEnabled(boolean value){
		if(this.nodeFigure != null){
			this.nodeFigure.setEnabled(value);
		}
	}
	public boolean isEnabled(){
		if(this.nodeFigure != null){
			return this.nodeFigure.isEnabled();
		}
		return false;
	}
	
	public void highlight() {
		if(this.isEnabled()){
			super.highlight();
		}
	}
	
	public void unhighlight() {

			super.unhighlight();
		
	}
	*/
	public void nameChange(){
		SwitchEntity m = this.getModel();
		
		if(m instanceof SwitchUser && Activator.usersAdapterFactory != null){
			IWorkbenchAdapter entryAdapter = 
				(IWorkbenchAdapter) Activator.usersAdapterFactory.getAdapter(m, IWorkbenchAdapter.class);
			if(entryAdapter != null){
				String label = entryAdapter.getLabel(m);
				if(label != null){
					this.setText(label);
				}
			}

		}
	}
	public void presenceChange(Object newValue){
		if(newValue instanceof Presence){
			Presence newPresence = (Presence)newValue;
			/*if(newPresence == Presence.OFF_LINE){
				this.setImage(NodeImage.SwitchUserOffLine);
			}
			else if(newPresence == Presence.ON_LINE){
				this.setImage(NodeImage.SwitchUserOnLine);
			}
			else{
				this.setImage(NodeImage.SwitchUserCallSpeak);
			}
			*/
			SwitchEntity m = this.getModel();
			if(m instanceof SwitchUser){
				this.setImage(NodeImage.getImageFromSwitchUserPresence((SwitchUser)m));
			}
			//this.setImage(NodeImage.getImageFromSwitchUserPresence(newPresence));
			this.newPresence = (Presence)newPresence;
			
		}
	}
	public void removeFromGroup(Object newValue){
		if(newValue instanceof String ){
			Graph  graph = this.getGraphModel();
			if(graph instanceof SwitchGraph){
				SwitchGraph groupGraph = (SwitchGraph)graph;
				SwitchEntity entity = groupGraph.getModel();
				if(entity != null && entity.getName().equals((String)newValue)){
					List selectList = groupGraph.getSelection();
					boolean nullSelect = false;
					if(selectList != null && selectList.size() > 0){
						Iterator iter = selectList.iterator();
						while(iter.hasNext()){
							GraphItem item = (GraphItem)iter.next();
							if(item != null && item == this){
								nullSelect = true;
								
								break;
							}
						}
					}
					if(nullSelect){
						groupGraph.setSelection(new GraphItem[]{});
						groupGraph.fireWidgetSelectedEvent(null);
					}
					this.dispose();
				}
			}
		}
	}
	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		  Activator.getDisplay().asyncExec(new Runnable() {
		      public void run() {
		    	  
		    	  if(evt.getPropertyName().equals(SwitchUser.PRESENCE_PROP)){
		    		  presenceChange(evt.getNewValue());
		    		  refreshTooltip();
		    	  }
		    	  else if(evt.getPropertyName().equals(SwitchUser.PROP_REMOVE_FROM_GROUP)){
		    		  removeFromGroup(evt.getNewValue());
		    	  }else if(evt.getPropertyName().equals(SwitchUser.PROP_USER_DESC)){
		    		  nameChange();
		    		  refreshTooltip();
		    	  }
		    	  else{
		    	  
		    		  refreshTooltip();
		    	  }
		    	  
			}
		   });
		
	}
	
	/*public void changePresence(){
		if(model.getPropertyValue(SwitchUser.PRESENCE_PROP) == Presence.OFF_LINE){
			model.setPropertyValue(SwitchUser.PRESENCE_PROP,Presence.ON_LINE);
		}else{
			model.setPropertyValue(SwitchUser.PRESENCE_PROP,Presence.OFF_LINE);
		}
	}*/

	/*@Override
	public int compareTo(Object o) {
		if(o instanceof SwitchUserGraphNode){
			SwitchUserGraphNode n2 = (SwitchUserGraphNode)o;
			SwitchEntity e2 = n2.getModel();
			if(this.model != null && this.model instanceof Comparable &&
					e2 != null){
				Comparable cmp = (Comparable)this.model;
				return cmp.compareTo(e2);
			}
		}
		return 0;
	}*/


	
}

