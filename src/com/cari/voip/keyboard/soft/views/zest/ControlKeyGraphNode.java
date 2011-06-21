package com.cari.voip.keyboard.soft.views.zest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;
import org.eclipse.zest.core.widgets.internal.GraphLabel;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.image.NodeImage;
import com.cari.voip.keyboard.soft.model.Presence;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchDispatchCtrl;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;
import com.cari.voip.keyboard.soft.views.zest.widget.GraphButton;

public class ControlKeyGraphNode extends GraphNode implements PropertyChangeListener{

	private static int ie = 0;
	private GraphLabel label;
	private SwitchDispatchCtrl model;
	public ControlKeyGraphNode(SwitchDispatchCtrl model,IContainer graphModel, int style) {
		this(model,graphModel, style, null);
	}

	public ControlKeyGraphNode(SwitchDispatchCtrl model,IContainer graphModel, int style, Object data) {
		this(model,graphModel, style, "" /*text*/, null /*image*/, data);
	}

	public ControlKeyGraphNode(SwitchDispatchCtrl model,IContainer graphModel, int style, String text) {
		this(model,graphModel, style, text, null);
	}

	public ControlKeyGraphNode(SwitchDispatchCtrl model,IContainer graphModel, int style, String text, Object data) {
		this(model,graphModel, style, text, null /*image*/, data);
	}

	public ControlKeyGraphNode(SwitchDispatchCtrl model,IContainer graphModel, int style, String text, Image image) {
		this(model,graphModel, style, text, image, null);
	}

	public ControlKeyGraphNode(SwitchDispatchCtrl model,IContainer graphModel, int style, String text, Image image, Object data) {
		super(graphModel,style,text,image,data);
		this.model = model;
		if(model != null){
			this.model.addPropertyChangeListener(this);
			this.setEnabled(this.model.isEnable());
		}
		
	}
	

	protected IFigure createFigureForModel() {
		
		label = new GraphLabel(getText(),getImage(),PositionConstants.EAST,false);
		label.setHeightExt(10);
		label.setBorderWidth(1);
		label.setBackgroundColor(ColorConstants.button);
		label.setCursor(new Cursor(Activator.getDisplay(),SWT.CURSOR_HAND));
		//GraphButton b = new GraphButton(label,Clickable.STYLE_BUTTON);
		
		//GraphLabel b = new GraphLabel(getText(),getImage(),PositionConstants.EAST,false);
		//b.setSize(50, 100);
		updateFigureForModel(label);
		return label;
	}
	
	public void presenceChange(Object newValue){
		if(newValue instanceof Boolean){
			boolean enable = ((Boolean)newValue).booleanValue();
			if(enable){
				this.setEnabled(true);
			}
			else {
				this.unhighlight();
				this.setEnabled(false);
			}
		}
	}
	public void dispose(){
		super.dispose();
	}
	
	public void setEnabled(boolean value){
		if(this.nodeFigure != null){
			this.nodeFigure.setEnabled(value);
			updateFigureForModel(getNodeFigure());
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
	
	protected void updateFigureForModel(IFigure currentFigure) {
		if(label != null){
			super.updateFigureForModel(label);
		}
		else{
			super.updateFigureForModel(currentFigure);
		}
		
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		  Activator.getDisplay().asyncExec(new Runnable() {
		      public void run() {
		    	  if(evt.getPropertyName().equals(SwitchDispatchCtrl.PROP_CTRL_ENABLE)){
		    		  presenceChange(evt.getNewValue());
		    	  }
		    	  else if(evt.getPropertyName().equals(SwitchDispatchCtrl.PROP_CTRL_SELECT)){
		    		  selectChange(evt.getNewValue());
		    	  }
			}
		   });
		
	}

	private void selectChange(Object newValue) {
		if(newValue instanceof Boolean){
			boolean select = ((Boolean)newValue).booleanValue();
			if(select){
				//this.unhighlight();
				this.highlight();
			}
			else {
				this.unhighlight();
			}
		}
	}
	public SwitchDispatchCtrl getModel(){
		return this.model;
	}
	
	
}
