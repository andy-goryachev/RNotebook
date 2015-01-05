// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.notebook.Styles;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;


public class CellBorder
    extends AbstractBorder
{
	public static final int GAP = 3;
	public static final Insets INSETS = new Insets(1, 1, 1 + GAP, 1);
	private boolean active;
	
	
	public CellBorder()
	{
	}
	
	
	public void setActive(boolean on)
	{
		active = on;
	}


	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
	{
		if(active)
		{
			g.setColor(Styles.sectionBorderColor);
			g.drawRect(x, y, w-1, h-1-GAP);
		}
	}


	public Insets getBorderInsets(Component c)
	{
		return INSETS;
	}


	public boolean isBorderOpaque()
	{
		return false;
	}
}
