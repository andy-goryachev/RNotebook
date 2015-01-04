// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.CList;
import goryachev.json.JsonDecoder;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import goryachev.notebook.Schema;
import java.io.Reader;


/** Reads DataBook JSON file */
public class DataBookJsonReader
	extends JsonDecoder
{
	public DataBookJsonReader(Reader rd)
	{
		super(rd);
	}
	
	
	public DataBook read() throws Exception
	{
		DataBook b = new DataBook();
		String type = null;
		String ver = null;
		
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
				if(Schema.VERSION.equals(ver))
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
				ver = nextString();
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
		CList<Object> results = null;

		beginObject();
		while(inObject())
		{
			String s = nextName();
			if(Schema.KEY_CELL_TYPE.equals(s))
			{
				type = nextString();
			}
			else if(Schema.KEY_TEXT.equals(s))
			{
				text = nextString();
			}
			else if(Schema.KEY_CELL_OUTPUT.equals(s))
			{
				results = readResults();
			}
		}
		endObject();
		
		CellType t = parseCellType(type);
		b.addCell(t, text, results);
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
		}
		endObject();
		
		// result
		if(Schema.RESULT_IMAGE.equals(type))
		{
			return ImageTools.read(image);
		}
		else if(Schema.RESULT_ERROR.equals(type))
		{
			// FIX will be different stack trace!
			return new Throwable(text);
		}
		else if(Schema.RESULT_TEXT.equals(type))
		{
			return text;
		}
		else
		{
			throw new Exception("unknown result type: " + type);
		}
	}


	protected CellType parseCellType(String s) throws Exception
	{
		if(Schema.CELL_TYPE_CODE.equals(s))
		{
			return CellType.CODE;
		}
		else if(Schema.CELL_TYPE_H1.equals(s))
		{
			return CellType.H1;
		}
		else if(Schema.CELL_TYPE_TEXT.equals(s))
		{
			return CellType.TEXT;
		}
		else
		{
			throw new Exception("unknown section type: " + s);
		}
	}
}
