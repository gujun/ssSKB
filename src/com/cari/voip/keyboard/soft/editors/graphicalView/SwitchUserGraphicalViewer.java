package com.cari.voip.keyboard.soft.editors.graphicalView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.internal.RevealListener;
import org.eclipse.zest.layouts.LayoutAlgorithm;

import com.cari.voip.keyboard.soft.views.zest.LooseGridLayoutAlgorithm;

public class SwitchUserGraphicalViewer extends ScrollingGraphicalViewer {

	public static final int ANIMATION_TIME = 500;
	
	private double colPadding = 6.0;
	private double rowPadding = 6.0;
	
	protected Comparator comparator;
	
	private interface RevealListener {
		
		public void revealed( Control c );

	}
	
	interface MyRunnable extends Runnable {
		public boolean isVisible();
	}
	
	private LayoutAlgorithm layoutAlgorithm = new LooseGridLayoutAlgorithm();
	
	private List<RevealListener> revealListeners = 
		new ArrayList<RevealListener>(1);
	
	public SwitchUserGraphicalViewer(){
		super();
	}
	
	public void setLayoutAlgorithm(LayoutAlgorithm layoutAlgorithm,boolean applay){
		this.layoutAlgorithm = layoutAlgorithm;
		
		
	}
	
	public LayoutAlgorithm getLayoutAlgorithm(){
		return this.layoutAlgorithm;
	}
	

	private void addRevealListener(final RevealListener revealListener) {

		MyRunnable myRunnable = new MyRunnable() {
			boolean isVisible;

			public boolean isVisible() {
				return this.isVisible;
			}

			public void run() {
				isVisible = SwitchUserGraphicalViewer.this.getControl().isVisible();
			}

		};
		Display.getDefault().syncExec(myRunnable);

		if (myRunnable.isVisible()) {
			revealListener.revealed(SwitchUserGraphicalViewer.this.getControl());
		} else {
			revealListeners.add(revealListener);
		}
	}
	
	public void hookPaintListener(){
		Control control = this.getControl();

		if(control != null){
			control.addPaintListener(new PaintListener(){
				public void paintControl(PaintEvent e){
					if (!revealListeners.isEmpty()) {
						Iterator iterator = revealListeners.iterator();
						while (iterator.hasNext()) {
							RevealListener reveallisetner = (RevealListener) iterator.next();
							reveallisetner.revealed(SwitchUserGraphicalViewer.this.getControl());
							iterator.remove();
						}
					}
				}
			});
		}
		
	}
	
	protected int[] calculateNumberOfRowsAndCols (int numChildren, 
			double nodeWidth, double nodeHeight, 
			 double x, double y,
			double boundWidth, double boundHeight) {
		int cols = 0;
		int rows = 0;
		
		cols = Math.max(1,(int)((boundWidth-x-10)/(nodeWidth+colPadding)));
		rows = (numChildren+cols-1)/cols;
		
		int[] result = {cols, rows};
		return result;
	}
	
	protected void layoutAlgorithm(Object[] entitiesToLayout,double x, double y,
			double width, double height) {
		
		int numChildren = entitiesToLayout.length;
		if (numChildren < 1) return;
		
		double h =0,w=0,MIN_ENTITY_SIZE=4.0;
		for(Object entity :entitiesToLayout){
			if(entity instanceof IFigure){
				IFigure node = (IFigure)entity;
			double ht = node.getSize().height;
			double wt = node.getSize().width;
			if(h < ht){
				h = ht;
			}
			if(w < wt){
				w = wt;
			}
			}
		}
		w = Math.max(w, MIN_ENTITY_SIZE);
		h =  Math.max(h, MIN_ENTITY_SIZE);
		
		double colWidth = w+colPadding;	
		double rowHeight = h+rowPadding;	
		
		double offsetX = colPadding/2.0; // half of the space between columns
		double offsetY = rowPadding/2.0; // half of the space between rows
		
		int[] colsAndRows = calculateNumberOfRowsAndCols(numChildren, w, h, x, y, width, height);
		int cols = colsAndRows[0];
		int rows = colsAndRows[1];
		
	

		
		// sort the entities
		/*if (comparator != null) {
            Arrays.sort(entitiesToLayout, comparator);
		} else {
			Arrays.sort(entitiesToLayout);
		}
		*/
		int index = 0;
		for( int i = 0; i < rows; i++ ) {
			for( int j = 0; j < cols; j++ ) {
				if( index < numChildren ) {
					// find new position for child
					double xmove = x + j * colWidth + offsetX;
					double ymove = y + i * rowHeight + offsetY;
					Object node = entitiesToLayout[index++];
					if(node instanceof IFigure){
					IFigure sn = (IFigure)node;
					Rectangle rect = new Rectangle((int)xmove,(int)ymove,(int)w,(int)h);
					sn.getParent().setConstraint(sn, rect);
					sn.setBounds(rect);
					}
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
	
	public void applyLayout(){
		this.addRevealListener(new RevealListener() {
			public void revealed(Control c) {
				Display.getDefault().asyncExec(new Runnable() {

					public void run() {
						applyLayoutInternal();
					}
				});
			}
		});
	}
	
	protected void applyLayoutInternal(){
		Control c = SwitchUserGraphicalViewer.this.getControl();
		if(c instanceof FigureCanvas){
			FigureCanvas canvas = (FigureCanvas)c;
			Dimension size = canvas.getViewport().getSize();
			
			double width = size.width;
			double height = size.height;
			
			List editpartChildren = this.getRootEditPart().getContents().getChildren();
			int  num = editpartChildren.size();
			if(num < 1){
				return;
			}
			ArrayList<IFigure> entities = new ArrayList<IFigure>(num);
			
			IFigure f;
			Iterator children = editpartChildren.iterator();
			while(children.hasNext()){
				AbstractGraphicalEditPart p = (AbstractGraphicalEditPart)children.next();
				f = p.getFigure();
				if(f != null && f instanceof IFigure){
					entities.add(f);
				}
		
				
			}
			Object[] entitiesToLayout = entities.toArray();
			//Animation.markBegin();
			this.layoutAlgorithm(entitiesToLayout,2,2,width,height);
			//Animation.run(ANIMATION_TIME);
			this.getLightweightSystem().getUpdateManager().performUpdate();
		}
	}
	
}
