package com.cari.voip.keyboard.soft.views.zest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.image.NodeImage;
import com.cari.voip.keyboard.soft.model.Presence;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchEntity;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;

public class SwitchGraphNode extends GraphNode implements Comparable,PropertyChangeListener {
	private SwitchEntity model = null;
	private PropertyChangeListener presenceListener = null;
	private Presence newPresence;
	private Presence oldPresence;
	public SwitchGraphNode(SwitchEntity model,IContainer graphModel, int style) {
		this(model,graphModel, style, null);
	}

	public SwitchGraphNode(SwitchEntity model,IContainer graphModel, int style, Object data) {
		this(model,graphModel.getGraph(), style, "" /*text*/, null /*image*/, data);
	}

	public SwitchGraphNode(SwitchEntity model,IContainer graphModel, int style, String text) {
		this(model,graphModel, style, text, null);
	}

	public SwitchGraphNode(SwitchEntity model,IContainer graphModel, int style, String text, Object data) {
		this(model,graphModel.getGraph(), style, text, null /*image*/, data);
	}

	public SwitchGraphNode(SwitchEntity model,IContainer graphModel, int style, String text, Image image) {
		this(model,graphModel, style, text, image, null);
	}

	public SwitchGraphNode(SwitchEntity model,IContainer graphModel, int style, String text, Image image, Object data) {
		super(graphModel,style,text,image,data);
		
		
		this.model = model;
		
		if(this.model != null){
			/*this.presenceListener = new  PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent evt){
					if(evt.getPropertyName().equals(SwitchUser.PRESENCE_PROP)){
						changePresence(evt.getNewValue());
					}
				}
			};*/
			this.model.addPropertyChangeListener(this);
			this.setTooltip(new Label(this.model.getTooltipString()));
		}
		
		/*this.addListener(SWT.Paint, new Listener(){
			public void handleEvent (Event event){
				if(newPresence == Presence.OFF_LINE){
					setImage(NodeImage.SwitchUserOffLine);
				}
				else if(newPresence == Presence.ON_LINE){
					setImage(NodeImage.SwitchUserOnLine);
				}
			}
		});*/
		
	}
	
	public SwitchEntity getModel(){
		
		return this.model;
	}
	public void refreshTooltip(){
		IFigure figure = this.getTooltip();
		if(figure != null && figure instanceof Label){
			Label label = (Label)figure;
			label.setText(this.model.getTooltipString());
		}
	}
	protected IFigure createFigureForModel() {
		/*GraphNode node = this;
		boolean cacheLabel = (this).cacheLabel();
		GraphLabel label = new GraphLabel(node.getText(), node.getImage(), cacheLabel);
		label.setFont(this.font);
		if (checkStyle(ZestStyles.NODES_HIDE_TEXT)) {
			label.setText("");
		}
		updateFigureForModel(label);*/
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

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		
	}
	

	@Override
	public int compareTo(Object o) {
		if(o instanceof SwitchGraphNode){
			SwitchGraphNode n2 = (SwitchGraphNode)o;
			SwitchEntity e2 = n2.getModel();
			if(this.model != null && this.model instanceof Comparable &&
					e2 != null){
				Comparable cmp = (Comparable)this.model;
				return cmp.compareTo(e2);
			}
		}
		return 0;
	}


}
