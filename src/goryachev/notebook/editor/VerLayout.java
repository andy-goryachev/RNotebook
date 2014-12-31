package goryachev.notebook.editor;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;


public class VerLayout
    implements LayoutManager
{
	/**
	* The horizontal alignment constant that designates centering. Also used to designate center anchoring.
	*/
	public final static int CENTER = 0;
	/**
	* The horizontal alignment constant that designates right justification.
	*/
	public final static int RIGHT = 1;
	/**
	* The horizontal alignment constant that designates left justification.
	*/
	public final static int LEFT = 2;
	/**
	* The horizontal alignment constant that designates stretching the component horizontally.
	*/
	public final static int BOTH = 3;
	/**
	* The anchoring constant that designates anchoring to the top of the display area
	*/
	public final static int TOP = 1;
	/**
	* The anchoring constant that designates anchoring to the bottom of the display area
	*/
	public final static int BOTTOM = 2;
	
	private int gap;
	private int alignment; // LEFT, RIGHT, CENTER or BOTH...how the components are justified
	private int anchor; // TOP, BOTTOM or CENTER ...where are the components positioned in an overlarge space


	//Constructors
	/**
	* Constructs an instance of VerticalLayout with a vertical vgap of 5 pixels, horizontal centering and anchored to
	* the top of the display area.
	*/
	public VerLayout()
	{
		this(5, CENTER, TOP);
	}


	/**
	* Constructs a VerticalLayout instance with horizontal centering, anchored to the top with the specified vgap
	*
	* @param vgap An int value indicating the vertical seperation of the components
	*/
	public VerLayout(int vgap)
	{
		this(vgap, CENTER, TOP);
	}


	/**
	* Constructs a VerticalLayout instance anchored to the top with the specified vgap and horizontal alignment
	*
	* @param vgap An int value indicating the vertical seperation of the components
	* @param alignment An int value which is one of <code>RIGHT, LEFT, CENTER, BOTH</code> for the horizontal alignment.
	*/
	public VerLayout(int vgap, int alignment)
	{
		this(vgap, alignment, TOP);
	}


	/**
	* Constructs a VerticalLayout instance with the specified vgap, horizontal alignment and anchoring
	*
	* @param vgap An int value indicating the vertical seperation of the components
	* @param alignment An int value which is one of <code>RIGHT, LEFT, CENTER, BOTH</code> for the horizontal alignment.
	* @param anchor An int value which is one of <code>TOP, BOTTOM, CENTER</code> indicating where the components are
	* to appear if the display area exceeds the minimum necessary.
	*/
	public VerLayout(int vgap, int alignment, int anchor)
	{
		this.gap = vgap;
		this.alignment = alignment;
		this.anchor = anchor;
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
		Insets m = parent.getInsets();
		
		synchronized(parent.getTreeLock())
		{
			int sz = parent.getComponentCount();
			Dimension pd = parent.getSize();
			int y = 0;
			
			// work out the total size
			for(int i=0; i<sz; i++)
			{
				Component c = parent.getComponent(i);
				Dimension d = c.getPreferredSize();
				y += d.height + gap;
			}
			
			y -= gap; // otherwise there's a vgap too many
			
			// work out the anchor paint
			if(anchor == TOP)
			{
				y = m.top;
			}
			else if(anchor == CENTER)
			{
				y = (pd.height - y) / 2;
			}
			else
			{
				y = pd.height - y - m.bottom;
			}
			
			// do layout
			for(int i=0; i<sz; i++)
			{
				Component c = parent.getComponent(i);
				Dimension d = c.getPreferredSize();
				int x = m.left;
				int w = d.width;
				
				if(alignment == CENTER)
				{
					x = (pd.width - d.width) / 2;
				}
				else if(alignment == RIGHT)
				{
					x = pd.width - d.width - m.right;
				}
				else if(alignment == BOTH)
				{
					w = pd.width - m.left - m.right;
				}
				
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
		return "VerLayout [vgap=" + gap + " align=" + alignment + " anchor=" + anchor + "]";
	}
}
