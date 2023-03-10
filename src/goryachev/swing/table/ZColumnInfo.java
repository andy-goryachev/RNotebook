// Copyright © 2012-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.table;
import goryachev.swing.CAlignment;
import javax.swing.table.TableCellRenderer;


public class ZColumnInfo
{
	public String name;
	public CAlignment align;
	public int min = -1;
	public int max = -1;
	public int pref = -1;
	private boolean editable;
	public String tooltip;
	public Object renderer;
	public TableCellRenderer headerRenderer;
	public Object editor;
	public ElasticColumnHandler handler;
	
	
	public void setEditable(boolean on)
	{
		editable = on;
	}
	
	
	public boolean isEditable()
	{
		return editable;
	}
}