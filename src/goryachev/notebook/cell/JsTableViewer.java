// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.table.CTableRowHeader;
import goryachev.common.ui.table.ZTable;
import goryachev.notebook.js.classes.DTable;
import java.awt.Dimension;
import javax.swing.JTable;


public class JsTableViewer
	extends CPanel
{
	public final JsTableModel model;
	public final ZTable table;
	public final CScrollPane scroll;
	
	
	public JsTableViewer(DTable t)
	{
		model = new JsTableModel(t);

		// TODO alignment, data coloring
		table = new ZTable(model);
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setBackground(Theme.textBG());
		UI.resizeToContent(table, 250);

		scroll = new CScrollPane(table);
		scroll.setRowHeaderView(new CTableRowHeader(table));	
		scroll.setTrackComponentDimensions(true);
		scroll.setMaximumSize(new Dimension(-1, 300));
		setCenter(scroll);
	}
}
