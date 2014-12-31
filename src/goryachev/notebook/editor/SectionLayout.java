// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.util.CException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;


/**
 * Section: left label, top text editor, bottom result, right margin label.
 */
public class SectionLayout
    implements LayoutManager2
{
	public static final Object LEFT = new Object();
	public static final Object RIGHT = new Object();

	private int leftMargin = 100;
	private int rightMargin = 75;
	private Component left;
	private Component right;
	

	public SectionLayout()
	{
	}


	public void addLayoutComponent(Component c, Object key)
	{
		synchronized(c.getTreeLock())
		{
			if(key == LEFT)
			{
				left = c;
			}
			else if(key == RIGHT)
			{
				right = c;
			}
		}
	}


	@Deprecated
	public void addLayoutComponent(String name, Component comp)
	{
		throw new CException();
	}


	public void removeLayoutComponent(Component c)
	{
		synchronized(c.getTreeLock())
		{
			if(c == right)
			{
				right = null;
			}
			else if(c == left)
			{
				left = null;
			}
		}
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

	
	protected boolean isLeftOrRight(Component c)
	{
		return (left == c) || (right == c);
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
		synchronized(parent.getTreeLock())
		{
			int h = 0;
			int min = 0;
			
			int sz = parent.getComponentCount();
			for(int i=0; i<sz; i++)
			{
				Component c = parent.getComponent(i);
				Dimension d = minimum ? c.getMinimumSize() : c.getPreferredSize();
				
				if(isLeftOrRight(c))
				{
					if(min < d.height)
					{
						min = d.height;
					}
				}
				else
				{
					h += d.height;
				}
			}
			
			if(h < min)
			{
				h = min;
			}

			Insets m = parent.getInsets();
			int w = parent.getWidth(); // - m.left - m.right;
			h += (m.top + m.bottom);
			
			return new Dimension(w, h);
		}
	}


	public void layoutContainer(Container parent)
	{
		synchronized(parent.getTreeLock())
		{
			Insets m = parent.getInsets();
			int y0 = m.top;
			int ym = parent.getHeight() - m.bottom;
			int x0 = m.left;
			int xm = parent.getWidth() - m.right;
			int y = y0;
			
			int sz = parent.getComponentCount();
			for(int i=0; i<sz; i++)
			{
				Component c = parent.getComponent(i);
				Dimension d = c.getPreferredSize();
				
				if(c == left)
				{
					c.setBounds(x0, y0, leftMargin, d.height);
				}
				else if(c == right)
				{
					c.setBounds(xm - rightMargin, y0, rightMargin, d.height);
				}
				else
				{
					c.setBounds(x0 + leftMargin, y, xm - x0 - leftMargin - rightMargin, d.height);
					y += d.height;
				}
			}
		}
	}
}
