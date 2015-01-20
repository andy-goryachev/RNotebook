// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook;
import goryachev.common.util.CList;
import goryachev.notebook.cell.CellType;


public class DataBook
{
	public static class Cell
	{
		public final CellType type;
		public final String text;
		public final int sequence;
		public final CList<Object> results;


		public Cell(CellType type, String text, int seq, CList<Object> results)
		{
			this.type = type;
			this.text = text;
			this.sequence = seq;
			this.results = results;
		}
	}

	//

	private CList<Cell> cells = new CList();


	public DataBook()
	{
	}
	
	
	public void addCell(CellType type, String text)
	{
		addCell(type, text, -1, null);
	}
	
	
	public void addCell(CellType type, String text, int seq, CList<Object> results)
	{
		cells.add(new Cell(type, text, seq, results));
	}
	
	
	public int size()
	{
		return cells.size();
	}
	
	
	public DataBook.Cell getCell(int ix)
	{
		return cells.get(ix);
	}
}
