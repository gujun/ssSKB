package com.cari.voip.keyboard.soft.adapter;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class dtLabelProvider implements ITableLabelProvider {

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
			String a[] = input.split("/");
			if(a != null && a.length > 1 && a[1] != null){
				
				if(a[1].equals("internal")){
					
					switch(columnIndex){
					case 0:
						ret = "本地";
						break;
					case 1:
						if(a.length > 2 && a[2] != null){
							ret = a[2];
						}
						break;
					default:
							break;
					}
					
				}else if(a[1].equals("gateway")){
					
					switch(columnIndex){
					case 0:
						ret = "网关";
						if(a.length > 2 && a[2] != null){
							ret = ret.concat("("+a[2]+")");
						}
						break;
					case 1:
						if(a.length > 3 && a[3] != null){
							ret = a[3];
						}
						break;
					default:
							break;
					}
				}else {
					ret = "未定义";
				}
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
