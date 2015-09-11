// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.table;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.table.CTableRowHeader;
import goryachev.notebook.cell.CellHandler;
import goryachev.notebook.js.classes.DTable;
import java.awt.Dimension;
import javax.swing.JTable;


public class DTableViewer
	extends CPanel
{
	public final DTableModel model;
	public final JTable table;
	public final CScrollPane scroll;
	
	
	public DTableViewer(DTable t, CellHandler h)
	{
		model = new DTableModel(t);

		table = new JTable(model);
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setBackground(Theme.TEXT_BG);
		table.setDefaultRenderer(Object.class, new DTableRenderer());
		table.addMouseListener(h);
		UI.resizeToContent(table, 250);

		scroll = new CScrollPane(table);
		scroll.setRowHeaderView(new CTableRowHeader(table));	
		scroll.setTrackComponentDimensions(true);
		scroll.setMaximumSize(new Dimension(-1, 300));
		scroll.getViewport().addMouseListener(h);
		setCenter(scroll);
	}
}
