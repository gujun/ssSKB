package com.cari.voip.keyboard.soft.editors.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.graphics.Color;

public class SwitchUserFigure extends Figure {

	
	public SwitchUserFigure(){
		
	}
	
	protected void paintFigure(Graphics g) {
		g.setBackgroundColor(new Color(null, 123,174,148));
		g.setForegroundColor(new Color(null, 200, 200, 240));
		g.fillRectangle(0, 0, 10, 20);
	}
	
	public String toString(){
		return "SwitchUserFigure";
	}
}
