package com.cari.voip.keyboard.soft.adapter.switchUsers;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.cari.voip.keyboard.soft.Activator;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUser;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersGroup;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersManager;
import com.cari.voip.keyboard.soft.model.switchUsers.SwitchUsersSession;

public class SwitchUsersAdapterFactory implements IAdapterFactory {


	
	private IWorkbenchAdapter rootAdapter = new IWorkbenchAdapter(){
		public Object getParent(Object o){
			return null;
		}
		public String getLabel(Object o){
			
			//int entriesNum = ((SwitchUsersManager)o).groupSize() + 1;
			
			//return "·Ö×é["+Integer.toString(entriesNum)+"]";
			return null;
		}
		public ImageDescriptor getImageDescriptor(Object object){
			//return Activator.getImageDescriptor("icons/all_sc_obj.gif");
			return null;
		}
		public Object[] getChildren(Object o){
			return ((SwitchUsersSession)o).toGroupsArray();
		}
	};
	
	private ImageDescriptor groupImageDescriptor = 
		Activator.getImageDescriptor("icons/all_sc_obj.gif");
	
	private IWorkbenchAdapter managerAdapter = new IWorkbenchAdapter(){
		public Object getParent(Object o){
			return null;
		}
		public String getLabel(Object o){
			
			return ((SwitchUsersManager)o).getName();
		}
		public ImageDescriptor getImageDescriptor(Object object){
			//return Activator.getImageDescriptor("icons/all_sc_obj.gif");
			return groupImageDescriptor;
		}
		public Object[] getChildren(Object o){
			return ((SwitchUsersManager)o).toGroupsArray();
		}
	};
	

	
	private IWorkbenchAdapter groupAdapter = new IWorkbenchAdapter(){
		public Object getParent(Object o){
			return ((SwitchUsersGroup)o).getParent();
		}
		public String getLabel(Object o){
			/*
			int entriesNum = ((SwitchUsersGroup)o).size();
			
			return ((SwitchUsersGroup)o).getName()+"["+Integer.toString(entriesNum)+"]";
			*/
			String label = ((SwitchUsersGroup)o).getName();
			if(label.length() > 8){
				label = label.substring(0, 7);
				label = label.concat("..");
			}
			return label;
		}
		public ImageDescriptor getImageDescriptor(Object object){
			return groupImageDescriptor;
		}
		public Object[] getChildren(Object o){
			return ((SwitchUsersGroup)o).getSwitchUsersArray();
		}
	};
	
	private ImageDescriptor entryImageDescriptor = 
		Activator.getImageDescriptor("icons/telephone16.png");
	
	private IWorkbenchAdapter entryAdapter = new IWorkbenchAdapter(){
		public Object getParent(Object o){
			return null;
		}
		public String getLabel(Object o){
			String label = ((SwitchUser)o).getName();
			if(label.length() > 8){
				label = label.substring(0, 7);
				label = label.concat("..");
			}
			return label;
			
		}
		public ImageDescriptor getImageDescriptor(Object object){
			return entryImageDescriptor;
		}
		public Object[] getChildren(Object o){
			return new Object[0];
		}
	};
	
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IWorkbenchAdapter.class){
			if(adaptableObject instanceof SwitchUsersSession){
				return this.rootAdapter;
			}
			if(adaptableObject instanceof SwitchUsersManager ){
				return this.managerAdapter;
			}
			if(adaptableObject instanceof SwitchUsersGroup){
				return this.groupAdapter;
			}
			if(adaptableObject instanceof SwitchUser){
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
