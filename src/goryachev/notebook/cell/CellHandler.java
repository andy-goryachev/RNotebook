// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CPopupMenu;
import goryachev.common.ui.CPopupMenuController;
import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;


public class CellHandler
	extends CPopupMenuController
{
	public CellHandler()
	{
	}


	public void mousePressed(MouseEvent ev)
	{
		updateActiveCell((JComponent)ev.getSource());
		
		super.mousePressed(ev);
	}


	public JPopupMenu constructPopupMenu()
    {
		Component c = getSourceComponent();
		CellPanel p = CellPanel.findParent(c);
		if(p == null)
		{
			return null;
		}
		
		CPopupMenu m = new CPopupMenu();
		return p.createPopupMenu(c, m);
    }
	
	
	protected void updateActiveCell(JComponent c)
	{
		CellPanel p = CellPanel.findParent(c);
		if(p != null)
		{
			NotebookPanel np = NotebookPanel.find(p); 
			if(np != null)
			{
				np.setActiveCell(p);
			}
		}
	}
}
