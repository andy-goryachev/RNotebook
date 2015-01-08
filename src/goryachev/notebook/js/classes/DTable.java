// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.js.classes;
import goryachev.common.util.CList;
import goryachev.common.util.Noobfuscate;
import goryachev.common.util.SB;
import java.util.Arrays;


@Noobfuscate
public class DTable
{
	protected CList<String> columns;
	protected CList<Object[]> data = new CList();
	private int width;
	
	
	public DTable(String ... columns)
	{
		for(String c: columns)
		{
			addColumn(c);
		}
	}
	
	
	protected DTable(DTable t)
	{
		width = t.width;
		int sz = t.getRowCount();
		for(int i=0; i<sz; i++)
		{
			Object[] rs = t.data.get(i);
			data.add(rs == null ? null : Arrays.copyOf(rs, width));
		}
		
		columns = (t.columns == null ? null : new CList(t.columns));
	}
	
	
	public DTable copy()
	{
		return new DTable(this);
	}


	public int getColumnCount()
	{
		if(columns == null)
		{
			return width;
		}
		else
		{
			return columns.size();
		}
	}
	
	
	public DTable setColumns(String[] cs)
	{
		columns = new CList(cs);
		return this;
	}
	
	
	public DTable addColumn(String name)
	{
		if(columns == null)
		{
			columns = new CList();
		}
		columns.add(name);
		return this;
	}
	
	
	public String getColumnName(int ix)
	{
		if(columns == null)
		{
			return "C" + (ix + 1);
		}
		else
		{
			return columns.get(ix);
		}
	}


	public int getRowCount()
	{
		return data.size();
	}


	public DTable addRow(Object ... xs)
	{
		if(width < xs.length)
		{
			width = xs.length;
		}
		data.add(Arrays.copyOf(xs, xs.length));
		return this;
	}


	public Object getValue(int row, int col)
	{
		if(row < data.size())
		{
			Object[] r = data.get(row);
			if(r != null)
			{
				if(col < r.length)
				{
					return r[col];
				}
			}
		}
		return null;
	}
	
	
	public Object[] getDataRow(int row)
	{
		if(row < data.size())
		{
			return data.get(row);
		}
		return null;
	}

	
	public DTable setValue(int row, int col, Object value)
	{
		while(row >= data.size())
		{
			data.add(null);
		}
		
		Object[] r = data.get(row);
		if(r == null)
		{
			r = new Object[col+8];
			data.set(row, r);
		}
		else if(col >= r.length)
		{
			r = Arrays.copyOf(r, col + 8);
			data.set(row, r);
		}
		
		r[col] = value;
		
		if(col >= width)
		{
			width = col+1;
		}
		
		return this;
	}
	
	
	protected String dump()
	{
		SB sb = new SB();
		for(int r=0; r<getRowCount(); r++)
		{
			for(int c=0; c<getColumnCount(); c++)
			{
				if(c > 0)
				{
					sb.a(',');
				}
				sb.a(getValue(r, c));
			}
			sb.a('\n');
		}
		return sb.toString();
	}
}
