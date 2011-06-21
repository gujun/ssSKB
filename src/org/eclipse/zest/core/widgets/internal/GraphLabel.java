/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.widgets.internal;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Overrides the Draw2D Label Figure class to ensure that the text is never
 * truncated. Also draws a rounded rectangle border.
 * 
 * @author Chris Callendar
 */
public class GraphLabel extends CachedLabel {

	private Color borderColor;
	private int borderWidth;
	private int arcWidth;

	protected int widthExt = 0;
	protected int heightExt = 0;
	private boolean painting = false;

	/**
	 * Creates a GraphLabel
	 * 
	 * @param cacheLabel
	 *            Determine if the text should be cached. This will make it
	 *            faster, but the text is not as clear
	 */
	public GraphLabel(boolean cacheLabel) {
		this("", cacheLabel);
	}

	/**
	 * Creates a graph label with text
	 * 
	 * @param text
	 *            The text
	 * @param cacheLabel
	 *            Determine if the text should be cached. This will make it
	 *            faster, but the
	 */
	public GraphLabel(String text, boolean cacheLabel) {
		this("", null, PositionConstants.SOUTH,cacheLabel);
	}

	/**
	 * Creates the graph label with an image
	 * 
	 * @param i
	 *            The Image
	 * @param cacheLabel
	 *            Determine if the text should be cached. This will make it
	 *            faster, but the
	 */
	public GraphLabel(Image i, boolean cacheLabel) {
		this("", i,PositionConstants.SOUTH, cacheLabel);
	}

	public GraphLabel(String text, Image i, boolean cacheLabel) {
		this(text, i,PositionConstants.SOUTH, cacheLabel);
	}
	/**
	 * Creates a graph label with an image and text
	 * 
	 * @param text
	 *            The text
	 * @param i
	 *            The Image
	 * @param cacheLabel
	 *            Determine if the text should be cached. This will make it
	 *            faster, but the
	 */
	public GraphLabel(String text, Image i,int textPosition, boolean cacheLabel) {
		super(cacheLabel);
		initLabel(textPosition);
		setText(text);
		setIcon(i);
		adjustBoundsToFit();
		
	}

	/**
	 * Initialises the border colour, border width, and sets the layout manager.
	 * Also sets the font to be the default system font.
	 */
	protected void initLabel(int textPosition) {
		super.setFont(Display.getDefault().getSystemFont());
		this.borderColor = ColorConstants.black;
		this.borderWidth = 0;
		this.arcWidth = 8;
		this.setLayoutManager(new StackLayout());
		this.setBorder(new MarginBorder(1));
		this.setTextPlacement(textPosition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#setFont(org.eclipse.swt.graphics.Font)
	 */
	public void setFont(Font f) {
		super.setFont(f);
		adjustBoundsToFit();
	}
	public void setWidthExt(int widthExt){
		this.widthExt = widthExt;
	}
	public void setHeightExt(int heightExt){
		this.heightExt = heightExt;
	}
	/**
	 * Adjust the bounds to make the text fit without truncation.
	 */
	protected void adjustBoundsToFit() {
		/*String text = getText();
		int safeBorderWidth = borderWidth > 0 ? borderWidth : 1;
		if ((text != null)) {
			Font font = getFont();
				Dimension minSize = FigureUtilities.getTextExtents(text, font);
				if (getIcon() != null) {
					org.eclipse.swt.graphics.Rectangle imageRect = getIcon().getBounds();
					int textPlacement = this.getTextPlacement();
					if( textPlacement == PositionConstants.EAST ||
							textPlacement == PositionConstants.WEST){
						
						int expandHeight = Math.max(imageRect.height -minSize.height, 0);
						minSize.expand(imageRect.width, expandHeight);
					}
					else{
						int expandWidth = Math.max(imageRect.width - minSize.width, 0);
						minSize.expand(expandWidth, imageRect.height);
					}
				}
				minSize.expand(10 + (2 * safeBorderWidth), 4 + (2 * safeBorderWidth));
				setBounds(new Rectangle(getLocation(), minSize));
			
		}*/
		Dimension d = new Dimension(6,4);
		d.expand(this.widthExt,this.heightExt);
		int safeBorderWidth = borderWidth > 0 ? borderWidth : 1;
		d.expand((2 * safeBorderWidth),(2 * safeBorderWidth));
		
		
		org.eclipse.swt.graphics.Rectangle imageRect = null;
		if (getIcon() != null) {
			imageRect = getIcon().getBounds();
			d.expand(imageRect.width,imageRect.height);
		}
		
		String text = getText();
		if ((text != null)) {
			Font font = getFont();
			Dimension minSize = FigureUtilities.getTextExtents(text, font);
			int textPlacement = this.getTextPlacement();
			if( textPlacement == PositionConstants.EAST ||
					textPlacement == PositionConstants.WEST){
					int expandHeight = Math.max(
					(imageRect != null?(minSize.height-imageRect.height ):minSize.height),
									0);
					d.expand(minSize.width, expandHeight);

				
			}
			else{
				int expandWidth = Math.max(
					(imageRect != null?(minSize.width - imageRect.width):minSize.width),
								0);
				d.expand(expandWidth,minSize.height);
			}
		}
		
		Rectangle oldBounds = this.getBounds();
		
		if(oldBounds.height > d.height){
			d.height = oldBounds.height;
		}
		if(oldBounds.width > d.width){
			d.width = oldBounds.width;
		}
		
		setBounds(new Rectangle(getLocation(), d));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Label#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	public void paint(Graphics graphics) {
		int blue = getBackgroundColor().getBlue();
		blue = (int) (blue - (blue * 0.20));
		blue = blue > 0 ? blue : 0;

		int red = getBackgroundColor().getRed();
		red = (int) (red - (red * 0.20));
		red = red > 0 ? red : 0;

		int green = getBackgroundColor().getGreen();
		green = (int) (green - (green * 0.20));
		green = green > 0 ? green : 0;

		Color lightenColor = new Color(Display.getCurrent(), new RGB(red, green, blue));
		graphics.setForegroundColor(lightenColor);
		graphics.setBackgroundColor(getBackgroundColor());

		int safeBorderWidth = borderWidth > 0 ? borderWidth : 1;
		graphics.pushState();
		double scale = 1;

		if (graphics instanceof ScaledGraphics) {
			scale = ((ScaledGraphics) graphics).getAbsoluteScale();
		}
		// Top part inside the border (as fillGradient does not allow to fill a rectangle with round corners).
		Rectangle rect = getBounds().getCopy();
		rect.height /= 2;
		graphics.setForegroundColor(getBackgroundColor());
		graphics.setBackgroundColor(getBackgroundColor());
		graphics.fillRoundRectangle(rect, arcWidth * safeBorderWidth, arcWidth * 2 * safeBorderWidth);

		// Bottom part inside the border.
		rect.y = rect.y + rect.height;
		rect.height += 1; // Not sure why it is needed, but it is needed ;-)
		graphics.setForegroundColor(lightenColor);
		graphics.setBackgroundColor(lightenColor);
		graphics.fillRoundRectangle(rect, arcWidth * safeBorderWidth, arcWidth * 2 * safeBorderWidth);

		// Now fill the middle part of top and bottom part with a gradient.
		rect = bounds.getCopy();
		rect.height -= 2;
		rect.y += (safeBorderWidth) / 2;
		rect.y += (arcWidth / 2);
		rect.height -= arcWidth / 2;
		rect.height -= safeBorderWidth;
		graphics.setBackgroundColor(lightenColor);
		graphics.setForegroundColor(getBackgroundColor());
		graphics.fillGradient(rect, true);

		// Paint the border
		if (borderWidth > 0) {
			rect = getBounds().getCopy();
			rect.x += safeBorderWidth / 2;
			rect.y += safeBorderWidth / 2;
			rect.width -= safeBorderWidth;
			rect.height -= safeBorderWidth;
			graphics.setForegroundColor(borderColor);
			graphics.setBackgroundColor(borderColor);
			graphics.setLineWidth((int) (safeBorderWidth * scale));
			graphics.drawRoundRectangle(rect, arcWidth, arcWidth);
		}

		super.paint(graphics);

		graphics.popState();

		lightenColor.dispose();
	}

	protected Color getBackgroundTextColor() {
		return getBackgroundColor();
	}

	/**
	 * This method is overridden to ensure that it doesn't get called while the
	 * super.paintFigure() is being called. Otherwise NullPointerExceptions can
	 * occur because the icon or text locations are cleared *after* they were
	 * calculated.
	 * 
	 * @see org.eclipse.draw2d.Label#invalidate()
	 */
	public void invalidate() {
		if (!painting) {
			super.invalidate();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Label#setText(java.lang.String)
	 */
	public void setText(String s) {
		if (!s.equals("")) {
			super.setText(s);

		} else {
			super.setText("");
		}
		adjustBoundsToFit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Label#setIcon(org.eclipse.swt.graphics.Image)
	 */
	public void setIcon(Image image) {
		super.setIcon(image);
		//adjustBoundsToFit();
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color c) {
		this.borderColor = c;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int width) {
		this.borderWidth = width;
	}

	public int getArcWidth() {
		return arcWidth;
	}

	public void setArcWidth(int arcWidth) {
		this.arcWidth = arcWidth;
	}

	public void setBounds(Rectangle rect) {
		// TODO Auto-generated method stub
		super.setBounds(rect);
	}

}
