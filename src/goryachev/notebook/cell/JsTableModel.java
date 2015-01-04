// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.cell;
import goryachev.common.ui.Theme;
import goryachev.common.ui.table.ElasticTableModel;
import goryachev.common.ui.table.ZColumnHandler;
import goryachev.common.ui.table.ZTableRenderer;
import goryachev.common.util.Parsers;
import goryachev.notebook.Styles;


public class JsTableModel
	extends ElasticTableModel
{
	public static final ZColumnHandler handler = createColumnHandler();

	
	public JsTableModel()
	{
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


	public void addColumn(String name)
	{
		super.addColumn(name);
		setHandler(handler);
	}
	
	
	public void setValueAt(Object x, int r, int c)
	{
		Object v = parse(x);
		super.setValueAt(v, r, c);
	}
	
	
	public void setRawValueAt(Object v, int r, int c)
	{
		super.setValueAt(v, r, c);
	}


	protected Object parse(Object x)
	{
		if(x instanceof Number)
		{
			return x;
		}
		else if(x != null)
		{
			String s = Parsers.parseString(x);
			return parseString(s);
		}
		else
		{
			return null;
		}
	}
	
	
	protected Object parseString(String s)
	{
		if(s.startsWith("'"))
		{
			if(isPrefixedNumber(s))
			{
				return s.substring(1);
			}
			else
			{
				return s;
			}
		}
		else
		{
			String trimmed = s.trim();
			
			try
			{
				return Integer.parseInt(trimmed);
			}
			catch(Exception e)
			{ }
			
			try
			{
				return Double.parseDouble(trimmed);
			}
			catch(Exception e)
			{ }
			
			return s;
		}
	}
	
	
	protected boolean isPrefixedNumber(String s)
	{
		int sz = s.length();
		for(int i=1; i<sz; i++)
		{
			char c = s.charAt(i);
			if("0123456789.-+eE".indexOf(c) < 0)
			{
				return false;
			}
		}
		return true;
	}
}
