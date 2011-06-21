package com.cari.voip.keyboard.soft.adapter;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.cari.voip.keyboard.soft.model.recording;

public class recordingsLabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String ret ="";
		if(element instanceof recording){
			recording rec = (recording)element;
			
			switch(columnIndex){
			case 0:
				ret = rec.caller_id_number;
				break;
			case 1:
				ret = rec.caller_id_name;
				break;
			case 2:
				if(rec.bound != null){
					if(rec.bound.startsWith("in")){
						ret = "Ö÷";
					} else {
						ret = "±»";
					}
				}
				break;
				
			case 3:
				ret = rec.start_stamp;
				break;
			case 4:
				ret = rec.file_path.substring(rec.file_path.lastIndexOf(".")+1);
				break;
			case 5:
				ret = String.valueOf(rec.file_len);
				break;
			case 6:
				if(rec.flags > 0){
					ret = "ÒÑÉ¾";
				}
				
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
