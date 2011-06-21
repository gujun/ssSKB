package com.cari.voip.keyboard.soft.controller;

import java.io.File;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class localSave {
	public static void asXLS(final Table tb,final String file,final IProgressMonitor monitor) throws Exception{
	
		int cols = tb.getColumnCount();
		int rows = tb.getItemCount();
		
		monitor.beginTask("µ¼³öxls", rows+5);
		
		
		WritableWorkbook book = Workbook.createWorkbook(new File(file));
		WritableSheet sheet = book.createSheet("sheet1", 0);
		
		monitor.worked(1);
		//add header name
		for(int i = 0; i< cols; i++){
			TableColumn column = tb.getColumn(i);
			jxl.write.Label head = new jxl.write.Label(i,0,column.getText());
			sheet.addCell(head);
			
		}
		monitor.worked(2);
		for(int j = 0;j < rows;j++){
			TableItem item = tb.getItem(j);
			for(int i = 0; i< cols;i++){
				jxl.write.Label content = new jxl.write.Label(i,j+1,item.getText(i));
				sheet.addCell(content);
				
			}
			monitor.worked(j+3);
		}
		
		book.write();
		
		monitor.worked(rows+4);
		
		book.close();
		
		monitor.done();
	}
}
