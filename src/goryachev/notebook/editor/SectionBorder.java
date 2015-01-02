// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.notebook.Styles;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;


public class SectionBorder
    extends AbstractBorder
{
	private boolean active;
	
	
	public SectionBorder()
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
			g.drawRect(x, y, w-1, h-1);
		}
	}


	public Insets getBorderInsets(Component c)
	{
		return new Insets(2, 2, 2, 2);
	}


	public boolean isBorderOpaque()
	{
		return false;
	}
}
