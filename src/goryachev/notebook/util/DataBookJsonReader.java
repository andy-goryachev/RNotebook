// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.CList;
import goryachev.json.JsonDecoder;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import goryachev.notebook.Schema;
import goryachev.notebook.js.JsError;
import goryachev.notebook.js.JsUtil;
import goryachev.notebook.js.classes.DPlot;
import goryachev.notebook.js.classes.DTable;
import java.io.Reader;


/** Reads DataBook JSON file */
public class DataBookJsonReader
	extends JsonDecoder
{
	public DataBookJsonReader(Reader rd)
	{
		super(rd, true);
	}
	
	
	public DataBook read() throws Exception
	{
		DataBook b = new DataBook();
		String type = null;
		int ver = -1;
		
		beginObject();
		while(inObject())
		{
			String s = nextName();
			if(Schema.KEY_CELLS.equals(s))
			{
				// validate file type
				if(!Schema.TYPE.equals(type))
				{
					throw new Exception("Wrong format: expecting " + Schema.TYPE);
				}
				
				// check version
				if(Schema.VERSION == ver)
				{
					// ok
				}
				else
				{
					throw new Exception("Wrong version: " + ver);
				}
				
				readCells(b);
			}
			else if(Schema.KEY_TYPE.equals(s))
			{
				type = nextString();
			}
			else if(Schema.KEY_VERSION.equals(s))
			{
				ver = nextInt();
			}
		}
		endObject();
		
		return b;
	}


	protected void readCells(DataBook b) throws Exception
	{
		beginArray();
		while(inArray())
		{
			readCell(b);
		}
		endArray();
	}


	protected void readCell(DataBook b) throws Exception
	{
		String type = null;
		String text = null;
		int seq = -1;
		CList<Object> results = null;

		beginObject();
		while(inObject())
		{
			String s = nextName();
			if(Schema.KEY_CELL_TYPE.equals(s))
			{
				type = nextString();
			}
			else if(Schema.KEY_CELL_SOURCE.equals(s))
			{
				text = nextString();
			}
			else if(Schema.KEY_CELL_SEQUENCE.equals(s))
			{
				seq = nextInt();
			}
			else if(Schema.KEY_CELL_OUTPUT.equals(s))
			{
				results = readResults();
			}
		}
		endObject();
		
		CellType t = CellType.parse(type);
		b.addCell(t, text, seq, results);
	}


	protected CList<Object> readResults() throws Exception
	{
		CList<Object> results = null;
		
		beginArray();
		while(inArray())
		{
			Object r = readResult();
			if(results == null)
			{
				results = new CList();
			}
			results.add(r);
		}
		endArray();
		
		return results;
	}
	
	
	protected Object readResult() throws Exception
	{
		String type = null;
		byte[] image = null;
		String text = null;
		DTable table = null;
		DPlot plot = null;
		
		beginObject();
		while(inObject())
		{
			String s = nextName();
			if(Schema.KEY_OUTPUT_TYPE.equals(s))
			{
				type = nextString();
			}
			else if(Schema.KEY_OUTPUT_IMAGE.equals(s))
			{
				image = nextByteArray();
			}
			else if(Schema.KEY_OUTPUT_TEXT.equals(s))
			{
				text = nextString();
			}
			else if(Schema.KEY_OUTPUT_TABLE_COLUMNS.equals(s))
			{
				if(table == null)
				{
					table = new DTable();
				}
				
				beginArray();
				while(inArray())
				{
					String name = nextString(); 
					table.addColumn(name);
				}
				endArray();
			}
			else if(Schema.KEY_OUTPUT_TABLE_ROWS.equals(s))
			{
				if(table == null)
				{
					table = new DTable();
				}
				
				beginArray();
				while(inArray())
				{
					// read row
					CList<Object> row = new CList();
					beginArray();
					while(inArray())
					{
						String cv = nextString();
						Object v = JsUtil.decodeTableCell(cv);
						row.add(v);
					}
					endArray();
					table.addRow(row.toArray());
				}
				endArray();
			}
		}
		endObject();
		
		// result
		if(Schema.RESULT_IMAGE.equals(type))
		{
			return ImageTools.read(image);
		}
		else if(Schema.RESULT_PLOT.equals(type))
		{
			// FIX 
			plot = new DPlot();
			
			return plot;
		}
		else if(Schema.RESULT_ERROR.equals(type))
		{
			return new JsError(text);
		}
		else if(Schema.RESULT_TEXT.equals(type))
		{
			return text;
		}
		else if(Schema.RESULT_TABLE.equals(type))
		{
			return table;
		}
		else
		{
			throw new Exception("unknown result type: " + type);
		}
	}
}
