package com.cari.voip.keyboard.soft.adapter.switchUsers;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.cari.voip.keyboard.soft.Activator;

import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersTreeEntry;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersTreeGroup;

public class SwitchUsersTreeAdapterFactory implements IAdapterFactory {

	private IWorkbenchAdapter groupAdapter = new IWorkbenchAdapter(){
		public Object getParent(Object o){
			//return ((SwitchUsersTreeGroup)o).getParent();
			return null;
		}
		public String getLabel(Object o){
			
			int entriesNum = ((SwitchUsersTreeGroup)o).getEntries().length;
			
			return ((SwitchUsersTreeGroup)o).getName()+"["+Integer.toString(entriesNum)+"]";
		}
		public ImageDescriptor getImageDescriptor(Object object){
			return Activator.getImageDescriptor("icons/all_sc_obj.gif");
		}
		public Object[] getChildren(Object o){
			return ((SwitchUsersTreeGroup)o).getEntries();
		}
	};
	
	private IWorkbenchAdapter entryAdapter = new IWorkbenchAdapter(){
		public Object getParent(Object o){
			//return ((SwitchUsersTreeEntry)o).getParent();
			return null;
		}
		public String getLabel(Object o){
			return ((SwitchUsersTreeEntry)o).getName();
		}
		public ImageDescriptor getImageDescriptor(Object object){
			return Activator.getImageDescriptor("icons/telephone16.png");		
		}
		public Object[] getChildren(Object o){
			return new Object[0];
		}
	};
	
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IWorkbenchAdapter.class){
			if(adaptableObject instanceof SwitchUsersTreeGroup){
				return this.groupAdapter;
			}
			if(adaptableObject instanceof SwitchUsersTreeEntry){
				return this.entryAdapter;
			}
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {

		return new Class[]{IWorkbenchAdapter.class};
	}

}
