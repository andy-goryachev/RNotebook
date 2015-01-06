// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.Theme;
import goryachev.common.ui.table.ZColumnHandler;
import goryachev.common.ui.table.ZTableRenderer;
import goryachev.notebook.Styles;
import goryachev.notebook.js.classes.DTable;
import javax.swing.table.AbstractTableModel;


public class JsTableModel
	extends AbstractTableModel
{
	private DTable data;
	public static final ZColumnHandler handler = createColumnHandler();

	
	public JsTableModel(DTable t)
	{
		this.data = t;
	}
	

	private static ZColumnHandler createColumnHandler()
	{
		return new ZColumnHandler()
		{
			public Object getCellValue(Object x)
			{
				return x;
			}
			
			
			public void decorate(Object x, ZTableRenderer r)
			{
				boolean number = (x instanceof Number);
				r.setHorizontalAlignment(number ? ZTableRenderer.RIGHT : ZTableRenderer.LEFT);
				r.setForeground(number ? Styles.numberColor : Theme.textFG());
			}
		};
	}


	public int getRowCount()
	{
		return data.getRowCount();
	}


	public int getColumnCount()
	{
		return data.getColumnCount();
	}
	
	
	public String getColumnName(int col)
	{
		return data.getColumnName(col);
	}


	public Object getValueAt(int row, int col)
	{
		return data.getValue(row, col);
	}
}
