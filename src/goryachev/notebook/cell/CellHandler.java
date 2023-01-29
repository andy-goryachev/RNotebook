// Copyright © 2015-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.swing.CPopupMenu;
import goryachev.swing.CPopupMenuController;
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
