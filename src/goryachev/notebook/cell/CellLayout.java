// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.util.Rex;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;


/**
 * Section: left label, top text editor, bottom result, right margin label.
 */
public class CellLayout
    implements LayoutManager2
{
	public static final Object CENTER = new Object();
	public static final Object LEFT = new Object();
	public static final Object RIGHT = new Object();

	private static int leftMargin = 85;
	private static int rightMargin = 75;
	private static int rightGap = 1;
	private Component center;
	private Component left;
	private Component right;
	

	public CellLayout()
	{
	}
	
	
	public static int getLeftMargin()
	{
		return leftMargin;
	}
	
	
	public static int getRightMargin()
	{
		return rightMargin;
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
			else if(key == CENTER)
			{
				center = c;
			}
		}
	}


	public void addLayoutComponent(String name, Component comp)
	{
		throw new Rex();
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
			else if(c == center)
			{
				center = null;
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
			Insets m = parent.getInsets();

			if(center != null)
			{
				// assuming only center component is present
				Dimension d = minimum ? center.getMinimumSize() : center.getPreferredSize();
				d.height += (m.top + m.bottom);
				return d;
			}
			else
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
	
				h += (m.top + m.bottom);

				int w = parent.getWidth();
				return new Dimension(w, h);
			}
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
			
			if(center != null)
			{
				Dimension d = center.getPreferredSize();
				center.setBounds(x0, y0, xm - x0, d.height);
			}
			else
			{
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
						c.setBounds(x0 + leftMargin, y, xm - x0 - leftMargin - rightMargin - rightGap, d.height);
						y += d.height;
					}
				}
			}
		}
	}
}
