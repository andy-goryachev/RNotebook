// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.UI;
import goryachev.common.util.CList;
import goryachev.common.util.TextTools;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;


public abstract class CellPanel
	extends JPanel
{
	public abstract JTextComponent getEditor();
	
	public abstract String getText();
		
	public abstract void saveCell(DataBook b);
	
	public abstract CellType getType();
	
	protected abstract JPopupMenu createPopupMenu(Component c, JPopupMenu m);
	
	//
	
	protected final CellHandler handler;
	
	
	public CellPanel()
	{
		setLayout(new CellLayout());
		setOpaque(false);
		setBorder(new CellBorder());
		
		handler = new CellHandler();
		addMouseListener(handler);
	}
	
	
	public void setActive(boolean on)
	{
		Border b = getBorder();
		if(b instanceof CellBorder)
		{
			((CellBorder)b).setActive(on);
			repaint();
		}
	}
	
	
	protected void setTop(Component c)
	{
		add(c);
	}
	
	
	protected void setLeft(Component c)
	{
		add(c, CellLayout.LEFT);
	}
	
	
	protected void setRight(Component c)
	{
		add(c, CellLayout.RIGHT);
	}
	
	
	protected void setCenter(Component c)
	{
		// this is the case of a single full-width component
		removeAll();
		add(c, CellLayout.CENTER);
	}


	public static CellPanel findParent(Object x)
	{
		if(x instanceof Container)
		{
			Container c = (Container)x;
			while(c != null)
			{
				if(c instanceof CellPanel)
				{
					return (CellPanel)c;
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
	
	
	public String toString()
	{
		return getClass().getSimpleName() + " " + TextTools.trimNicely(getText(), 20);
	}
	
	
	public static CellPanel create(CellType type, String text, int seq, CList<Object> results)
	{
		switch(type)
		{
		case CODE:
			return new CodePanel(text, seq, results);
		case H1:
		case H2:
		case H3:
			return new HeaderPanel(text, type);
		case TEXT:
		default:
			return new TextPanel(text);
		}
	}
}
