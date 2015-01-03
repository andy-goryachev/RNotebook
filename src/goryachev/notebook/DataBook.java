// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.util.CList;


public class DataBook
{
	public class Cell
	{
		public CellType type;
		public String text;
		
		public Cell(CellType type, String text)
		{
			this.type = type;
			this.text = text;
		}
	}
	
	//
	
	private CList<Cell> sections = new CList();

	
	public DataBook()
	{
	}
	
	
	public void addCell(CellType type, String text)
	{
		sections.add(new Cell(type, text));
	}
	
	
	public int size()
	{
		return sections.size();
	}
	
	
	public CellType getType(int ix)
	{
		return sections.get(ix).type;
	}
	
	
	public String getText(int ix)
	{
		return sections.get(ix).text;
	}
}
