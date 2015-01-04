// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.table.CTableRowHeader;
import goryachev.common.ui.table.ZTable;
import goryachev.notebook.js.table.JsTable;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class JsTableViewer
	extends CPanel
{
	public final JsTableModel model;
	public final ZTable table;
	public final CScrollPane scroll;
	
	
	public JsTableViewer(JsTable t)
	{
		model = new JsTableModel();
		// FIX
		model.addColumn("Column 1");
		model.addColumn("Column 2");
		model.addColumn("C3");
		model.setValueAt(1, 0, 0);
		model.setValueAt(2, 1, 0);
		model.setValueAt(3, 2, 0);
		model.setValueAt("Some Text", 3, 0);
		model.setValueAt(5, 4, 0);
		model.setValueAt(5, 400, 0);

		// TODO alignment, data coloring
		table = new ZTable(model);
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				table.getTableHeader().repaint();
			}
		});
		
		UI.setTableHeaderHighlight(table, Theme.hoverColor());

		scroll = new CScrollPane(table)
		{
			public Dimension getPreferredSize()
			{
				return new Dimension(-1, getPreferredHeight());
			}
		};
		scroll.setRowHeaderView(new CTableRowHeader(table));		
		setCenter(scroll);
	}


	protected int getPreferredHeight()
	{
		int h = 0;
		int rowCount = table.getRowCount();
		if((rowCount > 0) && (table.getColumnCount() > 0))
		{
			Rectangle r = table.getCellRect(rowCount - 1, 0, true);
			h = r.y + r.height;
		}
		
		h += table.getTableHeader().getHeight();
		
		if(h > 300)
		{
			h = 300;
		}
		
		return h;
	}
}
