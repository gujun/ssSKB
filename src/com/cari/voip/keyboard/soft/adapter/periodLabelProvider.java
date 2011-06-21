package com.cari.voip.keyboard.soft.adapter;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class periodLabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String ret ="";
		if(element instanceof String){
			String input = (String)element;
			String a[] = input.split("-");
			switch(columnIndex){
			case 0:
				if(a != null && a.length>0 && a[0] != null){
					ret = a[0];
				}
				
				break;
			case 1:
				if(a != null && a.length>1 && a[1] != null){
					ret = a[1];
				}
				break;
			default:
				break;
			}
		}
		return ret;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
