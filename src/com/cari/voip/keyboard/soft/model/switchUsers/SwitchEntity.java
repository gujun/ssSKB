package com.cari.voip.keyboard.soft.model.switchUsers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public abstract class SwitchEntity implements IPropertySource {

	private PropertyChangeSupport pcsDelegate = new PropertyChangeSupport(this);
	private long refreshTimer = 0;
	private long dispatStamp = 0;
	
	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPropertyValue(Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub

	}

	
	public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
		if (l == null) {
			throw new IllegalArgumentException();
		}
		pcsDelegate.addPropertyChangeListener(l);
	}

	public synchronized void addPropertyChangeListener(String property,PropertyChangeListener l) {
		if (l == null) {
			throw new IllegalArgumentException();
		}
		pcsDelegate.addPropertyChangeListener(property,l);
	}
	
	public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
		if (l != null) {
			pcsDelegate.removePropertyChangeListener(l);
		}
	}
	
	public synchronized void removePropertyChangeListener(String property,PropertyChangeListener l) {
		if (l != null) {
			pcsDelegate.removePropertyChangeListener(property,l);
		}
	}
	
	
	protected void firePropertyChange(String property, Object oldValue, Object newValue) {
		if (pcsDelegate.hasListeners(property)) {
			pcsDelegate.firePropertyChange(property, oldValue, newValue);
		}
	}
	
	public abstract String getName();
	public  String getTooltipString(){
		return getName();
	}
	public long getRefreshTimer(){
		return this.refreshTimer;
	}
	public long getDispatStamp(){
		return this.dispatStamp;
	}
	public void setRefreshTimer(long newValue){
		this.refreshTimer = newValue;
	}
	public void setDispatStamp(long newValue){
		this.dispatStamp = newValue;
	}
	public long refreshTimerInc(){
		this.refreshTimer++;
		return this.refreshTimer;
	}
	public long dispatStampInc(){
		this.dispatStamp++;
		return this.dispatStamp;
	}
	//public abstract void click();
}
