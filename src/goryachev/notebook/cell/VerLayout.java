// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.util.CException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;


public class VerLayout
	implements LayoutManager2
{
	private int gap;


	public VerLayout()
	{
		this(0);
	}


	public VerLayout(int gap)
	{
		this.gap = gap;
	}
	

	public void addLayoutComponent(Component c, Object constraints)
	{
	}


	@Deprecated
	public void addLayoutComponent(String name, Component comp)
	{
		throw new CException();
	}


	public void removeLayoutComponent(Component c)
	{
	}


	public Dimension maximumLayoutSize(Container parent)
	{
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}


	public float getLayoutAlignmentX(Container parent)
	{
		return 0.5f;
	}


	public float getLayoutAlignmentY(Container parent)
	{
		return 0.5f;
	}


	public void invalidateLayout(Container parent)
	{
	}
	
	public Dimension minimumLayoutSize(Container parent)
	{
		return computeSize(parent, true);
	}


	public Dimension preferredLayoutSize(Container parent)
	{
		return computeSize(parent, false);
	}


	protected Dimension computeSize(Container parent, boolean minimum)
	{
		int w = 0;
		int h = 0;
		
		synchronized(parent.getTreeLock())
		{
			int sz = parent.getComponentCount();
			for(int i=0; i<sz; i++)
			{
				Component c = parent.getComponent(i);
				if(c.isVisible())
				{
					Dimension d = minimum ? c.getMinimumSize() : c.getPreferredSize();
					
					if(w < d.width)
					{
						w = d.width;
					}
					
					h += d.height;
					
					if(i > 0)
					{
						h += gap;
					}
				}
			}
		}
		
		Insets m = parent.getInsets();
		w += (m.left + m.right);
		h += (m.top + m.bottom + gap + gap);
		
		return new Dimension(w, h);
	}


	public void layoutContainer(Container parent)
	{		
		synchronized(parent.getTreeLock())
		{
			Insets m = parent.getInsets();
			int x = m.left;
			int y = m.top;
			int w = parent.getWidth() - m.left - m.right;
			
			int sz = parent.getComponentCount();
			for(int i=0; i<sz; i++)
			{
				Component c = parent.getComponent(i);
				if(c.isVisible())
				{
					Dimension d = c.getPreferredSize();
					
					c.setBounds(x, y, w, d.height);
					y += (d.height + gap);
				}
			}
		}
	}
}
