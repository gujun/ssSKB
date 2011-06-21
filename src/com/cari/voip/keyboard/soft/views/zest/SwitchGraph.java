package com.cari.voip.keyboard.soft.views.zest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;

import com.cari.voip.keyboard.soft.model.switchUsers.SwitchEntity;

public class SwitchGraph extends Graph implements PropertyChangeListener {

	private SwitchEntity model;
	
	public SwitchGraph(Composite parent, int style){
		super(parent,style);
	}
	
	public SwitchEntity getModel(){
		return this.model;
	}
	public void setModel(SwitchEntity newModel){
		this.model = newModel;
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub

	}

}
