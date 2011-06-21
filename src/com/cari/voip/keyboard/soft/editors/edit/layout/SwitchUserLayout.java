package com.cari.voip.keyboard.soft.editors.edit.layout;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;


public class SwitchUserLayout extends XYLayout {
	
	 private final double MIN_ENTITY_SIZE = 4.0;
	 
	 protected double rowPadding = 6.0;
	 protected double colPadding = 6.0;

	 int rows=0, cols=0, numChildren=0; 
	 int totalProgress=0;
	 double h=0, w=0;
	 double colWidth, rowHeight, offsetX, offsetY;
	   
	 protected Comparator comparator;
	 
	protected int[] calculateNumberOfRowsAndCols (int numChildren, 
			double nodeWidth, double nodeHeight, 
			 double x, double y,
			double boundWidth, double boundHeight) {
		int cols = 0;
		int rows = 0;
		
		cols = Math.max(1,(int)((boundWidth-x)/(nodeWidth+colPadding)));
		rows = (numChildren+cols-1)/cols;
		
		int[] result = {cols, rows};
		return result;
	}
	
	protected void layoutAlgorithm(IFigure[] entitiesToLayout,double x, double y,
			double width, double height) {
		
		numChildren = entitiesToLayout.length;
		if (numChildren < 1) return;
		
		for(IFigure node :entitiesToLayout){
			double ht = node.getSize().height;
			double wt = node.getSize().width;
			if(h < ht){
				h = ht;
			}
			if(w < wt){
				w = wt;
			}
		}
		w = Math.max(w, MIN_ENTITY_SIZE);
		h =  Math.max(h, MIN_ENTITY_SIZE);
		
		colWidth = w+colPadding;	
		rowHeight = h+rowPadding;	
		
		offsetX = colPadding/2.0; // half of the space between columns
		offsetY = rowPadding/2.0; // half of the space between rows
		
		int[] colsAndRows = calculateNumberOfRowsAndCols(numChildren, w, h, x, y, width, height);
		cols = colsAndRows[0];
		rows = colsAndRows[1];
		
		totalProgress = rows + 2;

		
		// sort the entities
		if (comparator != null) {
            Arrays.sort(entitiesToLayout, comparator);
		} else {
			Arrays.sort(entitiesToLayout);
		}
		
		int index = 0;
		for( int i = 0; i < rows; i++ ) {
			for( int j = 0; j < cols; j++ ) {
				if( index < numChildren ) {
					// find new position for child
					double xmove = x + j * colWidth + offsetX;
					double ymove = y + i * rowHeight + offsetY;
					IFigure sn = entitiesToLayout[index++];
					Rectangle rect = new Rectangle((int)xmove,(int)ymove,(int)w,(int)h);
					this.setConstraint(sn, rect);
					sn.setBounds(rect);
				}
				else{
					break;
				}
			}
			//fireProgressEvent (2 + i, totalProgress);
		}	
		//updateLayoutLocations(entitiesToLayout);
		//fireProgressEvent (totalProgress, totalProgress);
		
	}
	
	public void layout(IFigure parent) {
		if(parent == null){
			return;
		}
		int size = parent.getChildren().size();
		if(size < 1){
			return;
		}
		Rectangle rect = parent.getBounds();
		double x = rect.x;
		double y = rect.y;
		double width = rect.width;
		double height = rect.height;
		
		ArrayList<IFigure> entities = new ArrayList<IFigure>(size);
		
		Iterator children = parent.getChildren().iterator();
		
		Point offset = getOrigin(parent);
		IFigure f;
		while (children.hasNext()) {
			f = (IFigure)children.next();
			entities.add(f);
			/*
			Rectangle bounds = (Rectangle)getConstraint(f);
			if (bounds == null) continue;

			if (bounds.width == -1 || bounds.height == -1) {
				Dimension preferredSize = f.getPreferredSize(bounds.width, bounds.height);
				bounds = bounds.getCopy();
				if (bounds.width == -1)
					bounds.width = preferredSize.width;
				if (bounds.height == -1)
					bounds.height = preferredSize.height;
			}
			bounds = bounds.getTranslated(offset);
			f.setBounds(bounds);*/
			
		}
		IFigure[] entitiesToLayout = (IFigure[])entities.toArray();
		this.layoutAlgorithm(entitiesToLayout, x, y, width, height);
	}
	

}
