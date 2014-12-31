// Copyright (c) 2014 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.CPanel;
import java.awt.Component;
import java.awt.Container;
import javax.swing.border.Border;


public class SectionPanel
	extends CPanel
{
	public SectionPanel()
	{
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
