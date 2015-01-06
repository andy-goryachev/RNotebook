// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.table.CTableRowHeader;
import goryachev.common.ui.table.ZTable;
import goryachev.common.ui.table.ZTableRenderer;
import goryachev.notebook.Styles;
import goryachev.notebook.js.classes.DTable;
import java.awt.Dimension;
import javax.swing.JTable;


public class DTableViewer
	extends CPanel
{
	public final DTableModel model;
	public final ZTable table;
	public final CScrollPane scroll;
	
	
	public DTableViewer(DTable t)
	{
		model = new DTableModel(t);

		// TODO alignment, data coloring, proper border
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
	
	
	// TODO
	public void decorate(Object x, ZTableRenderer r)
	{
		boolean number = (x instanceof Number);
		r.setHorizontalAlignment(number ? ZTableRenderer.RIGHT : ZTableRenderer.LEFT);
		r.setForeground(number ? Styles.numberColor : Theme.textFG());
	}
}
