package com.cari.voip.keyboard.soft.adapter.conf;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.model.Presence;
import com.cari.voip.keyboard.soft.model.conf.ContactsEntry;
import com.cari.voip.keyboard.soft.model.conf.ContactsGroup;
import com.cari.voip.keyboard.soft.model.conf.IADsEntry;
import com.cari.voip.keyboard.soft.model.conf.SwitchsEntry;

public class ConfAdapterFactory implements IAdapterFactory {

	private IWorkbenchAdapter groupAdapter = new IWorkbenchAdapter(){
		public Object getParent(Object o){
			return ((ContactsGroup)o).getParent();
		}
		public String getLabel(Object o){
			
			int entriesNum = ((ContactsGroup)o).getEntries().length;
			
			return ((ContactsGroup)o).getName()+"["+Integer.toString(entriesNum)+"]";
		}
		public ImageDescriptor getImageDescriptor(Object object){
			return Activator.getImageDescriptor("icons/all_sc_obj.gif");
		}
		public Object[] getChildren(Object o){
			return ((ContactsGroup)o).getEntries();
		}
	};
	
	private IWorkbenchAdapter entryAdapter = new IWorkbenchAdapter(){
		public Object getParent(Object o){
			return ((ContactsEntry)o).getParent();
		}
		public String getLabel(Object o){
			return ((ContactsEntry)o).getName()+"("+((ContactsEntry)o).getUrl()+")";
		}
		public ImageDescriptor getImageDescriptor(Object object){
			String path = "icons/drive.png";
			if(object instanceof SwitchsEntry){
				path = "icons/server.png";
				if(((SwitchsEntry)object).getPresence() == Presence.ON_LINE){
					path =  "icons/server.png";
				}else if(((SwitchsEntry)object).getPresence() == Presence.OFF_LINE){
					path = "icons/server_delete.png";
				}
				return Activator.getImageDescriptor(path);
			}
			if(object instanceof IADsEntry){
				return Activator.getImageDescriptor("icons/cup.png");
			}
			return Activator.getImageDescriptor("icons/drive.png");		}
		public Object[] getChildren(Object o){
			return new Object[0];
		}
	};
	
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IWorkbenchAdapter.class){
			if(adaptableObject instanceof ContactsGroup){
				return this.groupAdapter;
			}
			if(adaptableObject instanceof ContactsEntry){
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
