package com.cari.voip.keyboard.soft.views.zest;

import java.util.Arrays;

import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

public class LooseGridLayoutAlgorithm extends AbstractLayoutAlgorithm {
	

	 protected double rowPadding = 6.0;
	 protected double colPadding = 6.0;
	 
	 int rows=0, cols=0, numChildren=0; 
	 int totalProgress=0;
	 double h=0, w=0;
	 double colWidth, rowHeight,offsetX ,offsetY;
	   
	 protected double X = colPadding/2.0; // half of the space between columns
	 protected double Y = rowPadding/2.0; // half of the space between rows
	 
	public LooseGridLayoutAlgorithm(int styles){
		super(styles);
	}
	
	public LooseGridLayoutAlgorithm(){
		this(LayoutStyles.NONE);
	}

	public void setRowPadding(int rowPadding){
		this.rowPadding = rowPadding;
	}
	public void setColPadding(int colPadding){
		this.colPadding = colPadding;
	}

	public void setOffsetX(int offsetX){
		this.X = offsetX;
	}
	public double getOffsetX(){
		return this.X;
	}
	public void setOffsetY(int offsetY){
		this.Y = offsetY;
	}
	public double getOffsetY(){
		return this.Y;
	}
	
	@Override
	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider) {
		// TODO Auto-generated method stub
		
	}

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
	@Override
	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider, double x, double y,
			double width, double height) {
		
		numChildren = entitiesToLayout.length;
		if (numChildren < 1) return;
		
		for(InternalNode node :entitiesToLayout){
			double ht = node.getHeightInLayout();
			double wt = node.getWidthInLayout();
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
		
		offsetX = getOffsetX();
		offsetY = getOffsetY();

		
		int[] colsAndRows = calculateNumberOfRowsAndCols(numChildren, w, h, x, y, width, height);
		cols = colsAndRows[0];
		rows = colsAndRows[1];
		
		totalProgress = rows + 2;
		fireProgressEvent (1, totalProgress);
		
		// sort the entities
		if (comparator != null) {
            Arrays.sort(entitiesToLayout, comparator);
		} else {
			Arrays.sort(entitiesToLayout);
		}
		fireProgressEvent (2, totalProgress);
		
		
		
	}
	@Override
	protected void applyLayoutInternal(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider, double boundsX,
			double boundsY, double boundsWidth, double boundsHeight) {
		if (numChildren < 1) return;
		
		int index = 0;
		for( int i = 0; i < rows; i++ ) {
			for( int j = 0; j < cols; j++ ) {
				if( index < numChildren ) {
					// find new position for child
					double xmove = boundsX + j * colWidth + offsetX;
					double ymove = boundsY + i * rowHeight + offsetY;
					InternalNode sn = entitiesToLayout[index++];
					sn.setInternalLocation( xmove, ymove );
					sn.setInternalSize(w, h );
				}
				else{
					break;
				}
			}
			fireProgressEvent (2 + i, totalProgress);
		}	
		updateLayoutLocations(entitiesToLayout);
		fireProgressEvent (totalProgress, totalProgress);
	}

	@Override
	protected int getCurrentLayoutStep() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getTotalNumberOfLayoutSteps() {
		
		return totalProgress;
	}

	@Override
	protected boolean isValidConfiguration(boolean asynchronous,
			boolean continuous) {
		if ( asynchronous && continuous ) return false;
		else if ( asynchronous && !continuous ) return true;
		else if ( !asynchronous && continuous ) return false;
		else if ( !asynchronous && !continuous ) return true;
		
		return false;
	}


	@Override
	public void setLayoutArea(double x, double y, double width, double height) {
		throw new RuntimeException("Operation not implemented");
		
	}
}
