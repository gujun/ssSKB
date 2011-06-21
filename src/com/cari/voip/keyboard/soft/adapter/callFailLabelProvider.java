package com.cari.voip.keyboard.soft.adapter;

import java.util.ResourceBundle;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.cari.voip.keyboard.soft.model.cdr;

public class callFailLabelProvider implements ITableLabelProvider {

	public static ResourceBundle hangup_case_string;//= ResourceBundle.getBundle("com.cari.voip.keyboard.soft.model.hangup_cause");
	public static  ResourceBundle context_extension_string;// = ResourceBundle.getBundle("com.cari.voip.keyboard.soft.model.context_extension");
		
	static{
		try{
			 hangup_case_string= ResourceBundle.getBundle("com.cari.voip.keyboard.soft.model.hangup_cause");
			 context_extension_string= ResourceBundle.getBundle("com.cari.voip.keyboard.soft.model.context_extension");
		}
		catch(Exception e){

		}
	}
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String ret ="";
		if(element instanceof cdr){
			cdr rec = (cdr)element;
			
			switch(columnIndex){
			case 0:
				ret = rec.caller_id_number;
				break;
			case 1:
				ret = rec.caller_id_name;
				break;
			case 2:
				ret = rec.start_stamp;
				break;
			case 3:
				ret = "";
				if(hangup_case_string != null){
				try{
					ret = hangup_case_string.getString(rec.hangup_cause);
				}
				catch(Exception e){
					ret ="无法接通";
					
				}
				}
				break;
			default:
				ret = "";
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
