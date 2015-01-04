// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.notebook.util;
import goryachev.common.ui.ImageTools;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.FileTools;
import goryachev.common.util.Parsers;
import goryachev.json.JsonEncoder;
import goryachev.notebook.CellType;
import goryachev.notebook.DataBook;
import goryachev.notebook.Schema;
import goryachev.notebook.js.JsError;
import java.awt.image.BufferedImage;
import java.io.File;


/** DataBook writer */
public class DataBookJsonWriter
{
	private DataBookJsonWriter()
	{
	}


	private static void writeCell(DataBook.Cell c, JsonEncoder wr) throws Exception
	{
		wr.beginObject();
		{
			wr.write(Schema.KEY_CELL_TYPE, CellType.toSectionCode(c.type));
			wr.write(Schema.KEY_CELL_SOURCE, c.text);
			
			if(c.sequence > 0)
			{
				wr.write(Schema.KEY_CELL_SEQUENCE, c.sequence);
			}
			
			// output
			CList<Object> rs = c.results;
			if((rs != null) && (rs.size() > 0))
			{
				wr.name(Schema.KEY_CELL_OUTPUT);
				wr.beginArray();
				{
					for(Object r: rs)
					{
						writeResult(r, wr);
					}
				}
				wr.endArray();
			}
		}
		wr.endObject();
	}
	
	
	private static void writeResult(Object x, JsonEncoder wr) throws Exception
	{
		wr.beginObject();
		{
			if(x instanceof BufferedImage)
			{
				byte[] b = ImageTools.toPNG((BufferedImage)x);
				wr.write(Schema.KEY_OUTPUT_TYPE, Schema.RESULT_IMAGE);
				wr.writeByteArray(Schema.KEY_OUTPUT_IMAGE, b);
			}
			else if(x instanceof JsError)
			{
				String msg = ((JsError)x).error;
				wr.write(Schema.KEY_OUTPUT_TYPE, Schema.RESULT_ERROR);
				wr.write(Schema.KEY_OUTPUT_TEXT, msg);
			}
			else 
			{
				wr.write(Schema.KEY_OUTPUT_TYPE, Schema.RESULT_TEXT);
				wr.write(Schema.KEY_OUTPUT_TEXT, Parsers.parseString(x));
			}
		}
		wr.endObject();
	}
	
	
	public static void saveJSON(DataBook b, File f) throws Exception
	{
		FileTools.createBackup(f);
		
		// write
		JsonEncoder wr = new JsonEncoder(f);
		try
		{
			wr.beginObject();
			{
				if(b != null)
				{
					// file attributes
					wr.write(Schema.KEY_TYPE, Schema.TYPE);
					wr.write(Schema.KEY_VERSION, Schema.VERSION);

					// sections
					wr.name(Schema.KEY_CELLS);
					wr.beginArray();
					{
						int sz = b.size();
						for(int i=0; i<sz; i++)
						{
							DataBook.Cell c = b.getCell(i);
							writeCell(c, wr);
						}
					}
					wr.endArray();
				}
			}
			wr.endObject();
		}
		finally
		{
			CKit.close(wr);
		}
	}
}
