package com.cari.voip.keyboard.soft.views.zest.widget;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.widgets.internal.GraphLabel;

public class GraphButton extends Clickable {

	public GraphButton(IFigure contents, int style){
		super(contents,style);
	}
	public GraphButton(String text,Image image, int style){
		super(new GraphLabel(text,image,PositionConstants.EAST,false),style);
	}
	public GraphButton(String text,Image image){
		this(text,image,STYLE_BUTTON);
	}
	
	public GraphButton(String text, int style){
		this(text,null,style);
	}
	public GraphButton(Image image, int style){
		this("",image,style);
	}
	
	public GraphButton(String text){
		this(text,null,STYLE_BUTTON);
	}
	public GraphButton(Image image){
		this("",image,STYLE_BUTTON);
	}
	
	public GraphButton(int style){
		super();
		setStyle(style);

	}
	public GraphButton(){
		super();
		setStyle(STYLE_BUTTON);

	}
	
	
	protected void init() {
		super.init();
		//setBackgroundColor(ColorConstants.button);
	}
	
	protected void setContents(IFigure contents) {
		super.setContents(contents);
		this.setBounds(contents.getBounds());
	}

	
}
