// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JPanel;
import javax.swing.border.Border;


public abstract class SectionPanel
	extends JPanel
{
	public abstract String getText();
	
	//
	
	public SectionPanel()
	{
		setLayout(new SectionLayout());
		setOpaque(false);
		setBorder(new SectionBorder());
	}
	
	
	public void setActive(boolean on)
	{
		Border b = getBorder();
		if(b instanceof SectionBorder)
		{
			((SectionBorder)b).setActive(on);
			repaint();
		}
	}
	
	
	protected void setTop(Component c)
	{
		add(c);
	}
	
	
	protected void setLeft(Component c)
	{
		add(c, SectionLayout.LEFT);
	}
	
	
	protected void setRight(Component c)
	{
		add(c, SectionLayout.RIGHT);
	}
	
	
	protected void setCenter(Component c)
	{
		// this is the case of a single full-width component
		removeAll();
		add(c, SectionLayout.CENTER);
	}


	public static SectionPanel findParent(Object x)
	{
		if(x instanceof Container)
		{
			Container c = (Container)x;
			while(c != null)
			{
				if(c instanceof SectionPanel)
				{
					return (SectionPanel)c;
				}
				
				c = c.getParent();
			}
		}
		return null;
	}
}
