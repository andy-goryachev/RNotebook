package goryachev.notebook.editor;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;


public class VerLayout
    implements LayoutManager
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


	private Dimension layoutSize(Container parent, boolean minimum)
	{
		Dimension dim = new Dimension(0, 0);
		
		synchronized(parent.getTreeLock())
		{
			int sz = parent.getComponentCount();
			for(int i=0; i<sz; i++)
			{
				Component c = parent.getComponent(i);
				if(c.isVisible())
				{
					Dimension d = minimum ? c.getMinimumSize() : c.getPreferredSize();
					dim.width = Math.max(dim.width, d.width);
					dim.height += d.height;
					if(i > 0)
					{
						dim.height += gap;
					}
				}
			}
		}
		
		Insets m = parent.getInsets();
		dim.width += m.left + m.right;
		dim.height += m.top + m.bottom + gap + gap;
		return dim;
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
				Dimension d = c.getPreferredSize();
				
				c.setBounds(x, y, w, d.height);
				y += (d.height + gap);
			}
		}
	}


	public Dimension minimumLayoutSize(Container parent)
	{
		return layoutSize(parent, false);
	}


	public Dimension preferredLayoutSize(Container parent)
	{
		return layoutSize(parent, false);
	}


	public void addLayoutComponent(String name, Component comp)
	{
	}


	public void removeLayoutComponent(Component comp)
	{
	}


	public String toString()
	{
		return "VerLayout [vgap=" + gap + "]";
	}
}
