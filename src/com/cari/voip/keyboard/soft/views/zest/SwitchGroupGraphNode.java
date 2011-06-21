package com.cari.voip.keyboard.soft.views.zest;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.IContainer;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchEntity;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;

public class SwitchGroupGraphNode extends SwitchGraphNode {
	public SwitchGroupGraphNode(SwitchEntity model,IContainer graphModel, int style) {
		this(model,graphModel, style, null);
	}

	public SwitchGroupGraphNode(SwitchEntity model,IContainer graphModel, int style, Object data) {
		this(model,graphModel.getGraph(), style, "" /*text*/, null /*image*/, data);
	}

	public SwitchGroupGraphNode(SwitchEntity model,IContainer graphModel, int style, String text) {
		this(model,graphModel, style, text, null);
	}

	public SwitchGroupGraphNode(SwitchEntity model,IContainer graphModel, int style, String text, Object data) {
		this(model,graphModel.getGraph(), style, text, null /*image*/, data);
	}

	public SwitchGroupGraphNode(SwitchEntity model,IContainer graphModel, int style, String text, Image image) {
		this(model,graphModel, style, text, image, null);
	}

	public SwitchGroupGraphNode(SwitchEntity model,IContainer graphModel, int style, String text, Image image, Object data) {
		super(model,graphModel,style,text,image,data);
	}
	
	public void propertyChange(final PropertyChangeEvent evt) {
		Activator.getDisplay().asyncExec(new Runnable() {
		      public void run() {
		    	  
		    	  if(evt.getPropertyName().equals(SwitchUsersGroup.PROP_GROUP_REMOVE)){
		    		  
		    		  removeGroup(evt.getNewValue());
		    	  }else{
		    		  refreshTooltip();
		    	  }
		    	 
			}
		   });
	}

	protected void removeGroup(Object newValue) {
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
}
