// Copyright © 2012-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.table;
import goryachev.notebook.js.classes.DTable;
import javax.swing.table.AbstractTableModel;


public class DTableModel
	extends AbstractTableModel
{
	private DTable data;

	
	public DTableModel(DTable t)
	{
		this.data = t;
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
