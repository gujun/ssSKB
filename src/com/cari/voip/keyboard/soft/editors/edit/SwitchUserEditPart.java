package com.cari.voip.keyboard.soft.editors.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.zest.core.widgets.internal.GraphLabel;

import com.cari.voip.keyboard.soft.image.NodeImage;
import com.cari.voip.keyboard.soft.model.Presence;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;


public class SwitchUserEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	private static int i = 0;
	private int index = -1;
	//private Point p = new Point(0,0);
	private Dimension size = new Dimension(50,50);
	
	private SwitchUser user= null;
	private PropertyChangeListener presenceListener = null;
	private GraphLabel gf= null;
	private boolean isSel = false;
	private static int ie = 0;
	@Override
	protected IFigure createFigure() {
		Button f;
		if(user != null){
			f = new Button(user.getUserId(),NodeImage.SwitchUserOnLine);
		}
		else{
			f = new Button("lala",NodeImage.SwitchUserOnLine);
		}
		f.setSize(100, 50);
		return f;
		
	/*	if(gf == null){
			    gf = new GraphLabel(NodeImage.getImageFromSwitchUser(user),false);
				gf.setForegroundColor(ColorConstants.black);
				
				//f.setTextPlacement(PositionConstants.SOUTH);
				gf.setTextPlacement(PositionConstants.SOUTH);
				
				Object model = this.getModel();
				if(model instanceof SwitchUser){
					//f.setText(((SwitchUser)model).getUserId());
					gf.setText(((SwitchUser)model).getUserId());
				}
				
				
				
				if((ie%5)== 0){
					gf.setEnabled(false);
				}
				ie++;
				gf.addMouseListener(new MouseListener.Stub(){
					public void mouseReleased(MouseEvent me){
						if(user != null){
							if(user.getPresence() == Presence.OFF_LINE){
								user.setPresence(Presence.ON_LINE);
							}
							else if(user.getPresence() == Presence.ON_LINE){
								user.setPresence(Presence.OFF_LINE);
							}
						}
					}
					
						
				});
				gf.addFocusListener(new FocusListener.Stub(){
					
					public void focusGained(FocusEvent fe) { 
						isSel = true;
						
						updateFigureBackgroudColor();
					}
					
					public void focusLost(FocusEvent fe) { 
						isSel = false;
						
						updateFigureBackgroudColor();
						
					}
				});
				
				
		}
		
		
		return gf;*/
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
	}

	public void setModel(Object model) {
		super.setModel(model);
		
		if(this.getModel() != null){
			if(this.user != null &&
					this.presenceListener != null){
				this.user.removePropertyChangeListener(SwitchUser.PRESENCE_PROP, 
						this.presenceListener);
			}
			
			if(this.getModel() instanceof SwitchUser){

				this.user = (SwitchUser)this.getModel();
				
				if(this.presenceListener == null){
					this.presenceListener = new  PropertyChangeListener(){
						public void propertyChange(PropertyChangeEvent evt){
							if(evt.getPropertyName().equals(SwitchUser.PRESENCE_PROP)){
								changePresence(evt.getNewValue());
							}
						}
					};
				}
				
				this.user.addPropertyChangeListener(SwitchUser.PRESENCE_PROP, 
						this.presenceListener);
			}
		}
		
	}
	
	protected void changePresence(Object newValue) {
		GraphLabel label;
		IFigure figure;
		if(newValue instanceof Presence){
			Presence newPresence = (Presence)newValue;
			if(newPresence == Presence.OFF_LINE){
				
				figure = this.getFigure();
				if(figure != null && figure instanceof GraphLabel){
					label = (GraphLabel)figure;
					label.setIcon(NodeImage.SwitchUserOffLine);
				}
				
			}
			else if(newPresence == Presence.ON_LINE){
				
				figure = this.getFigure();
				
				if(figure != null && figure instanceof GraphLabel){
					label = (GraphLabel)figure;
					label.setIcon(NodeImage.SwitchUserOnLine);
				}
			}
		}
	}

	protected void refreshVisuals() {
		/*
		IFigure figure = getFigure();
		if(figure == null || figure.getParent() == null){
			return;
		}
		Rectangle bounds = getFigure().getBounds();
		
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
	*/
	}
	
	protected void updateFigureBackgroudColor(){
		if(this.isSel){
			gf.setBackgroundColor(ColorConstants.yellow);
		}
		else{
			gf.setBackgroundColor(ColorConstants.listBackground);
		}
	}
}
