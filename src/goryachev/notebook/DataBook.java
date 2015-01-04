// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.util.CList;


public class DataBook
{
	public class Cell
	{
		public CellType type;
		public String text;
		public CList<Object> results;
	}
	
	//
	
	private CList<Cell> cells = new CList();

	
	public DataBook()
	{
	}
	
	
	public void addCell(CellType type, String text)
	{
		Cell c = new Cell();
		c.type = type;
		c.text = text;
		cells.add(c);
	}
	
	
	public void addCell(CellType type, String text, CList<Object> results)
	{
		Cell c = new Cell();
		c.type = type;
		c.text = text;
		c.results = results;
		cells.add(c);
	}
	
	
	public int size()
	{
		return cells.size();
	}
	
	
	public CellType getType(int ix)
	{
		return cells.get(ix).type;
	}
	
	
	public String getText(int ix)
	{
		return cells.get(ix).text;
	}
	
	
	public CList<Object> getResults(int ix)
	{
		return cells.get(ix).results;
	}
}
