package com.cari.voip.keyboard.soft.adapter;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.cari.voip.keyboard.soft.model.sms;

public class smsLabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String ret ="";
		if(element instanceof sms){
			sms rec = (sms)element;
			
			switch(columnIndex){
			case 0:
				if(rec.proto != null && rec.proto.length() > 0){
					ret = rec.proto+":"+rec.sms_from;
				}else {
					ret = rec.sms_from;
				}
				break;
			case 1:
				if(rec.chat != null && rec.chat.length() > 0){
					ret = rec.chat+":"+rec.sms_to;
				}else {
					ret = rec.sms_to;
				}
				break;
			case 2:
				ret = rec.body;
				break;
				
			case 3:
				ret = rec.sms_type;
				break;
			case 4:
				ret = rec.start_stamp;
				break;
			
			default:
				ret = "";
				break;
			}
			
		}
		// TODO Auto-generated method stub
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
