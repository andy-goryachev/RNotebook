// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.editor;
import goryachev.common.ui.UI;
import goryachev.notebook.DataBook;
import goryachev.notebook.SectionType;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;


public abstract class SectionPanel
	extends JPanel
{
	public abstract JTextComponent getEditor();
	
	public abstract String getText();
	
	public abstract void initialize(NotebookPanel np);
	
	public abstract void saveSection(DataBook b);
	
	public abstract SectionType getType();
	
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
	
	
	public void focusLater()
	{
		UI.later(new Runnable()
		{
			public void run()
			{
				JTextComponent c = getEditor();
				if(c != null)
				{
					c.requestFocusInWindow();
				}
			}
		});
	}
	
	
	public static SectionPanel create(SectionType type, String text)
	{
		switch(type)
		{
		case CODE:
			return new CodePanel(text);
		case H1:
		case H2:
		case H3:
			return new HeaderPanel(text);
		case TEXT:
		default:
			return new TextPanel(text);
		}
	}
}
